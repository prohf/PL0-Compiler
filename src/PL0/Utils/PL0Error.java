package PL0.Utils;

/**
 * Created by apple on 2017/1/8.
 */
public class PL0Error {
    private Word errorWord;
    private int errorKind;
    private String error;

    public PL0Error(Word errorWord, int errorKind, String error) {
        this.errorWord = errorWord;
        this.errorKind = errorKind;
        this.error = error;
    }

    public Word getErrorWord() {
        return errorWord;
    }

    public void setErrorWord(Word errorWord) {
        this.errorWord = errorWord;
    }

    public int getErrorKind() {
        return errorKind;
    }

    public void setErrorKind(int errorKind) {
        this.errorKind = errorKind;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
