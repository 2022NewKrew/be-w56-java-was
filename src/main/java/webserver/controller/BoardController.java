package webserver.controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import model.board.Board;
import model.board.Text;
import webserver.Model;
import webserver.service.BoardService;
import webserver.web.request.Request;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class BoardController extends BaseController {

    private final static BoardController boardController = new BoardController();
    private final BoardService boardService;

    private BoardController() {
        this.boardService = BoardService.getInstance();
    }

    public static BoardController getInstance() {
        return boardController;
    }

    @PostMapping(url = "/board")
    public String postBoard(Request request, String text) {
        log.debug("{} 정상진입", this.getClass());
        String cookie = request.getHeader().getHeaders().get("Cookie");
        if (cookie == null || !cookie.contains("logined"))
            return "redirect:/user/login.html";
        String[] cookies = cookie.split("=");
        String userId = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].equals("logined")) {
                userId = cookies[i + 1];
                break;
            }
        }
        System.out.println("============================" + userId + "==============================");
        Board board = new Board(new Text(text), LocalDateTime.now());
        boardService.postBoard(board, userId);
        return "redirect:/board/list";
    }

    @GetMapping(url = "/board/list")
    public String showAllBoards(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAllAttribute("boards", boards);
        return "index.html";
    }
}
