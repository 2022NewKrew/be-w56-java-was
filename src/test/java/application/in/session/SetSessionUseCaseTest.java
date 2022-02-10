package application.in.session;

import application.out.session.SessionPort;
import application.session.SetSessionService;
import domain.session.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class SetSessionUseCaseTest {

    SetSessionUseCase setSessionUseCase;

    @Mock
    SessionPort sessionPort;

    @BeforeEach
    void injectMock() {
        setSessionUseCase = new SetSessionService(sessionPort);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(sessionPort);
    }

    @DisplayName("세션을 생성하여 저장할 수 있.")
    @Test
    void setSession() {
        String attributeName = "userId";
        String attributeValue = "483759";

        String sessionId = setSessionUseCase.setSession(attributeName, attributeValue);

        assertThat(sessionId).isNotNull();
        verify(sessionPort).set(any(Session.class));
    }
}