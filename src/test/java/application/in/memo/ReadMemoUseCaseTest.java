package application.in.memo;

import application.exception.memo.NonExistsMemoIdException;
import application.memo.ReadMemoService;
import application.out.memo.ReadMemoPort;
import domain.memo.Memo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ReadMemoUseCaseTest {

    ReadMemoUseCase readMemoUseCase;

    @Mock
    ReadMemoPort readMemoPort;

    @BeforeEach
    void injectMock() {
        readMemoUseCase = new ReadMemoService(readMemoPort);
    }

    @AfterEach
    void checkAnotherCall() { verifyNoMoreInteractions(readMemoPort); }

    @DisplayName("메모의 id로 조회할 수 있다.")
    @Test
    void assertReadMemo() {
        int memoId = 24;
        Memo memo = Memo.builder()
                .id(memoId)
                .content("Hello World")
                .createdAt(LocalDateTime.now())
                .build();
        given(readMemoPort.findOneById(memoId))
                .willReturn(Optional.ofNullable(memo));

        Memo readMemo = readMemoUseCase.readMemo(memoId);

        verify(readMemoPort).findOneById(memoId);
        assertThat(readMemo)
                .isEqualTo(memo);
    }

    @DisplayName("존재하지 않는 메모는 조회할 수 없다.")
    @Test
    void assertReadMemoWithInvalidMemoId() {
        int memoId = 25;
        given(readMemoPort.findOneById(memoId))
                .willReturn(Optional.empty());

        assertThrows(NonExistsMemoIdException.class, () -> readMemoUseCase.readMemo(memoId));

        verify(readMemoPort).findOneById(memoId);
    }

    @DisplayName("모든 메모를 조회할 수 있다.")
    @Test
    void assertReadAllMemos() {
        List<Memo> memos = List.of(
                new Memo(1, "Hello", LocalDateTime.now()),
                new Memo(2, "World", LocalDateTime.now()),
                new Memo(3, "!!!", LocalDateTime.now())
        );
        given(readMemoPort.findAll())
                .willReturn(memos);

        List<Memo> results = readMemoUseCase.readAllMemo();

        verify(readMemoPort).findAll();
        assertThat(results)
                .usingRecursiveComparison()
                .isEqualTo(memos);
    }
}