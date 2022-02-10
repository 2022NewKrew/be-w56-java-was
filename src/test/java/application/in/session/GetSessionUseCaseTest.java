package application.in.session;

import application.exception.session.SessionExpiredException;
import application.out.session.SessionPort;
import application.session.GetSessionService;
import domain.session.Session;
import domain.session.SessionId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GetSessionUseCaseTest {

    GetSessionUseCase getSessionUseCase;

    @Mock
    SessionPort sessionPort;

    @BeforeEach
    void injectMock() {
        getSessionUseCase = new GetSessionService(sessionPort);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(sessionPort);
    }

    @DisplayName("세션 아이디로 세션 어트리뷰트 저장소에서 값을 꺼내올 수 있다.")
    @Test
    void getSession() {
        SessionId sessionId = SessionId.create();
        String attributeName = "loginId";
        String attributeValue = "483759";
        Session session = new Session(sessionId, attributeName, attributeValue, Duration.ofHours(3));
        given(sessionPort.get(sessionId.getValue()))
                .willReturn(Optional.of(session));

        Session<String> getSession = getSessionUseCase.getSession(sessionId.getValue());
        String loginId = getSession.getAttributeValue();

        assertThat(loginId).isEqualTo(attributeValue);
        verify(sessionPort).get(sessionId.getValue());
    }

    @DisplayName("만료된 세션으로 세션 어트리뷰트 저장소에 접근할 수 없다.")
    @Test
    void getSessionWithExpiredSession() {
        SessionId sessionId = SessionId.create();
        String attributeName = "loginId";
        String attributeValue = "483759";
        Session session = new Session(sessionId, attributeName, attributeValue, Duration.ZERO);
        given(sessionPort.get(sessionId.getValue()))
                .willReturn(Optional.of(session));

        assertThrows(SessionExpiredException.class, () -> getSessionUseCase.getSession(sessionId.getValue()));

        verify(sessionPort).get(sessionId.getValue());
        verify(sessionPort).remove(sessionId.getValue());
    }
}