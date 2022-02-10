package application.in.session;

import application.out.session.SessionAttributesPort;
import application.out.session.SessionPort;
import application.session.SetSessionService;
import domain.session.Session;
import domain.session.SessionAttributes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class SetSessionUseCaseTest {

    SetSessionUseCase setSessionUseCase;

    @Mock
    SessionPort sessionPort;

    @Mock
    SessionAttributesPort sessionAttributesPort;

    @BeforeEach
    void injectMock() {
        setSessionUseCase = new SetSessionService(sessionPort, sessionAttributesPort);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(sessionPort);
        verifyNoMoreInteractions(sessionAttributesPort);
    }

    @DisplayName("세션을 저장하면 세션과 세션 어트리뷰트가 저장된다.")
    @Test
    void setSession() {
        // given
        String attributeName = "userId";
        String attributeValue = "483759";
        SessionAttributes sessionAttributes = new SessionAttributes(1L, attributeName, attributeValue);
        given(sessionAttributesPort.get(attributeName))
                .willReturn(Optional.of(sessionAttributes));

        // when
        Long sessionId = setSessionUseCase.setSession(attributeName, attributeValue);

        //then
        assertThat(sessionId).isEqualTo(1L);
        verify(sessionAttributesPort).set(attributeName, attributeValue);
        verify(sessionAttributesPort).get(attributeName);
        verify(sessionPort).set(any(Session.class));
    }
}