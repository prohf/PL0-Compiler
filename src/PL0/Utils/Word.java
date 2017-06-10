package PL0.Utils;

/**
 * Created by apple on 2016/12/25.
 */
public class Word {
    private String word;
    private Integer type;
    private Integer line;
    private Integer position;

    public Word() {
    }

    public Word(String word, Integer type, Integer line, Integer position) {
        this.word = word;
        this.type = type;
        this.line = line;
        this.position = position;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
