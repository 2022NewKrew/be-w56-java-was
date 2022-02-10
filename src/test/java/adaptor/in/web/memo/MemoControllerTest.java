package adaptor.in.web.memo;

import application.in.memo.WriteMemoUseCase;
import application.in.session.GetSessionUseCase;
import domain.memo.Memo;
import infrastructure.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class MemoControllerTest {

    MemoController memoController;

    @Mock
    GetSessionUseCase getSessionUseCase;

    @Mock
    WriteMemoUseCase writeMemoUseCase;

    @BeforeEach
    void injectMock() {
        memoController = new MemoController(getSessionUseCase, writeMemoUseCase);
    }

    @AfterEach
    void checkAnotherCall() {
        verifyNoMoreInteractions(getSessionUseCase);
        verifyNoMoreInteractions(writeMemoUseCase);
    }

    @DisplayName("로그인한 사용자는 메모를 작성할 수 있다.")
    @Test
    void writeMemo() {
        // given
        HttpRequest httpRequest = new HttpRequest(
                new RequestLine(RequestMethod.POST, new Path(ContentType.UNKNOWN, "/memo")),
                HttpHeader.of(),
                new HttpStringBody("writer=binary&content=hello\nworld")
        );
        Memo memo = Memo.builder()
                .writer("binary")
                .content("hello\nworld")
                .createdAt(LocalDateTime.now())
                .build();

        // when
        memoController.handle(httpRequest);

        //then
    }
}