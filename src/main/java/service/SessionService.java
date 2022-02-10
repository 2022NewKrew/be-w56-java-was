package service;

import repository.Repository;
import repository.session.SessionNoDbUseRepository;
import webserver.session.Session;

import java.util.Optional;

public class SessionService {
    private static final SessionService INSTANCE = new SessionService();
    private final Repository<Session, Session, String> sessionRepository;

    private SessionService(){
        sessionRepository = new SessionNoDbUseRepository();
    }

    public static synchronized SessionService getInstance(){
        if(INSTANCE == null)
            return new SessionService();

        return INSTANCE;
    }

    public String join(Session session){
        sessionRepository.save(session);

        return session.getSessionId();
    }

    public Optional<Session> findOne(String userId){
        return sessionRepository.findById(userId);
    }
}
