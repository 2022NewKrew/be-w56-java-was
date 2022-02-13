package model.board;

import lombok.NoArgsConstructor;
import model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long boardId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Text text;

    private LocalDateTime time;

    public Board(Text text, LocalDateTime now) {
        this.text = text;
        this.time = now;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
