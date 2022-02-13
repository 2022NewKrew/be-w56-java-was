package model.board;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Text {

    private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
