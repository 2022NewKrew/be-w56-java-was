package webserver.repository;

import db.JdbcRepository;
import model.board.Board;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepository {

    private final static BoardRepository boardRepository = new BoardRepository();
    private final EntityManager em;

    private BoardRepository() {
        em = JdbcRepository.getEntityManager();
    }

    public static BoardRepository getInstance() {
        return boardRepository;
    }

    public Board save(Board board) {
        em.getTransaction().begin();
        em.persist(board);
        em.getTransaction().commit();
        //em.close();
        return board;
    }

    public List<Board> findAll() {
        em.getTransaction().begin();
        List<Board> boards = em.createQuery("select b from Board b", Board.class).getResultList();
        em.getTransaction().commit();
        return boards;
    }
}
