package application.in.session;

import domain.session.Session;

public interface GetSessionUseCase {
    Session getSession(String sessionId);
}
