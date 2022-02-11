package db;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DataBaseTest {
//    static Stream<Arguments> parseRanksArguments() {
//        return Stream.of(
//                Arguments.of(List.of(), Collections.emptyMap()),
//                Arguments.of(List.of(Rank.valueOf(6, false)), Map.of(Rank.FIRST, 1L)),
//                Arguments.of(List.of(Rank.valueOf(3, false)), Map.of(Rank.FIFTH, 1L)),
//                Arguments.of(List.of(Rank.valueOf(3, false), Rank.valueOf(3, false)), Map.of(Rank.FIFTH, 2L))
//        );
//    }
    void addUser() {
    }

    void findUserById() {
    }

    void findAll() {
    }
}