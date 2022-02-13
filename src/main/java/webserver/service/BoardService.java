package webserver.service;

import model.board.Board;
import model.user.User;
import webserver.repository.BoardRepository;
import webserver.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class BoardService {

    private final static BoardService boardService = new BoardService();
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private BoardService() {
        boardRepository = BoardRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    public static BoardService getInstance() {
        return boardService;
    }


    public Board postBoard(Board board, String userId) {
        User user = userRepository.findById(userId);
        if (user == null)
            throw new NoSuchElementException();
        board.setUser(user);
        return boardRepository.save(board);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}
