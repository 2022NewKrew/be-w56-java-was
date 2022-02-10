package db;

import webserver.Session;

public class SessionDb {
    static Session session = null;

    public static void store(Session sessionId){
        session = sessionId;
    }

    public static String removeSession(){
        String sid = session.getSessionId();
        session = null;
        return sid;
    }

    public static boolean isExist(){
        return (session != null);
    }

}
