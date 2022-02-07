package http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpSessions {
    private final static Map<String, HttpSession> sessions;

    static{
        sessions = new HashMap<>();
    }

    public static HttpSession getSession(String sessionId){
        HttpSession session = sessions.get(sessionId);

        if(session == null){
            session = new HttpSession();
            sessions.put(sessionId, session);
        }

        return session;
    }
}
