package PL0.Utils;

/**
 * Created by apple on 2016/12/25.
 */
public class PCode {
    private String Operator;
    private int S1;
    private int S2;

    public PCode(String operator, int s1, int s2) {
        Operator = operator;
        S1 = s1;
        S2 = s2;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public int getS1() {
        return S1;
    }

    public void setS1(int s1) {
        S1 = s1;
    }

    public int getS2() {
        return S2;
    }

    public void setS2(int s2) {
        S2 = s2;
    }
}
