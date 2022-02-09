package application.in.memo;

import application.exception.user.NonExistsUserIdException;
import application.memo.WriteMemoService;
import application.out.memo.WriteMemoPort;
import application.out.user.FindUserPort;
import domain.memo.Memo;
import domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WriteMemoUseCaseTest {

    WriteMemoUseCase writeMemoUseCase;

    @Mock
    WriteMemoPort writeMemoPort;

    @Mock
    FindUserPort findUserPort;

    @BeforeEach
    void injectMock() {
        writeMemoUseCase = new WriteMemoService(writeMemoPort, findUserPort);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(writeMemoPort);
        verifyNoMoreInteractions(findUserPort);
    }


    /**
     * 존재하는 사용자인지 확인한다(존재하지 않으면 에러를 반환)
     * 메모를 저장한다 (메모에 사용자 객체가 들어가야 할까? -> 나중에 구현하기)
     * */
    @DisplayName("사용자는 메모를 작성할 수 있다.")
    @Test
    void assertWriteMemo() {
        String userId = "id";
        User user = User.builder()
                .userId(userId)
                .password("password")
                .name("name")
                .email("email@kakao.com")
                .build();
        Memo memo = Memo.builder()
                .id(1)
                .content("Hello")
                .createdAt(LocalDateTime.now())
                .build();
        given(findUserPort.findByUserId(userId))
                .willReturn(Optional.of(user));

        writeMemoUseCase.write(userId, memo);

        verify(findUserPort).findByUserId(userId);
        verify(writeMemoPort).save(memo);
    }

    @DisplayName("계정이 없는 사용자는 메모를 작성할 수 없다.")
    @Test
    void assertWriteMemoWithWrongUser() {
        String userId = "wrongId";
        Memo memo = Memo.builder()
                .id(1)
                .content("Hello")
                .createdAt(LocalDateTime.now())
                .build();
        given(findUserPort.findByUserId(userId))
                .willReturn(Optional.empty());

        assertThrows(NonExistsUserIdException.class, () -> writeMemoUseCase.write(userId, memo));

        verify(findUserPort).findByUserId(userId);
        verify(writeMemoPort, never()).save(memo);
    }
}