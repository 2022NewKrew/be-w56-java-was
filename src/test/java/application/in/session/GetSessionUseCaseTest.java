package application.in.session;

import application.exception.session.SessionExpiredException;
import application.out.session.SessionAttributesPort;
import application.out.session.SessionPort;
import application.session.GetSessionService;
import domain.user.Session;
import domain.user.SessionAttributes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GetSessionUseCaseTest {

    GetSessionUseCase getSessionUseCase;

    @Mock
    SessionPort sessionPort;

    @Mock
    SessionAttributesPort sessionAttributesPort;

    @BeforeEach
    void injectMock() {
        getSessionUseCase = new GetSessionService(sessionPort, sessionAttributesPort);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(sessionPort);
        verifyNoMoreInteractions(sessionAttributesPort);
    }

    @DisplayName("세션 아이디로 세션 어트리뷰트 저장소에서 값을 꺼내올 수 있다.")
    @Test
    void getSession() {
        String attributeName = "loginId";
        String attributeValue = "483759";
        Long sessionId = 1L;
        Session session = new Session(sessionId, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(2));
        SessionAttributes sessionAttributes = new SessionAttributes(1L, attributeName, attributeValue);
        given(sessionPort.get(sessionId))
                .willReturn(Optional.of(session));
        given(sessionAttributesPort.get(sessionId))
                .willReturn(Optional.of(sessionAttributes));

        String loginId = (String)getSessionUseCase.getSession(sessionId);

        assertThat(loginId).isEqualTo(attributeValue);
        verify(sessionAttributesPort).get(sessionId);
    }

    @DisplayName("만료된 세션으로 세션 어트리뷰트 저장소에 접근할 수 없다.")
    @Test
    void getSessionWithExpiredSession() {
        Long sessionId = 1L;
        Session session = new Session(sessionId, LocalDateTime.now().minusHours(4), LocalDateTime.now().minusHours(1));
        given(sessionPort.get(sessionId))
                .willReturn(Optional.of(session));

        assertThrows(SessionExpiredException.class, () -> getSessionUseCase.getSession(sessionId));

        verify(sessionAttributesPort, never()).get(sessionId);
    }
}