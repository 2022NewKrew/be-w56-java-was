package repository.session;

import model.UserAccount;
import model.UserAccountDTO;
import repository.Repository;
import webserver.session.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionNoDbUseRepository implements Repository<Session, Session, String> {
    private static final Map<String, Session> SESSION_ID_SESSION_DB = new ConcurrentHashMap<>();

    @Override
    public Session save(Session session) {
        SESSION_ID_SESSION_DB.put(session.getSessionId(), session);

        return SESSION_ID_SESSION_DB.get(session.getSessionId());
    }

    @Override
    public Optional<Session> findById(String id) {
        Session session = SESSION_ID_SESSION_DB.get(id);

        if(!Objects.isNull(session))
            if(session.getValidTime().isAfter(LocalDateTime.now())){
                return Optional.of(session);
            }

        return Optional.empty();
    }

    @Override
    public List<Session> findAll() {
        return null;
    }

    public void clearStore() {
        SESSION_ID_SESSION_DB.clear();
    }
}
