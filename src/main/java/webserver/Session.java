package webserver;

import db.SessionDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.RandomUtils.getRandomID;

public class Session {
    private static final Logger log = LoggerFactory.getLogger(Session.class);
    final static private int EXPIRED_PERIOD = 100;

    private String sessionId;
    private String uid;
    private int createDate;
    private int expireDate;
    private int lastAccessDate;

    public Session(String uid) {
        this.sessionId = getRandomID();
        this.uid = uid;

        setDate();
    }

    public static String create(String uid){
        Session session = new Session(uid);
        log.info("New session created - id :{}", session.getSessionId());
        SessionDb.store(session);
        return session.getSessionId();
    }

    public String getSessionId(){
        return sessionId;
    }

    // todo : to be filled..
    private void setDate(){
        // 세션 생성 시간 기준으로 createDate 설정
        // expireDate = createDate + EXPIRED_PERIOD;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Session{");
        sb.append("sessionId='").append(sessionId).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
