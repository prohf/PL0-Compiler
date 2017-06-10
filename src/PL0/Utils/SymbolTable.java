package PL0.Utils;

/**
 * Created by apple on 2016/12/25.
 */
public class SymbolTable {
    private String name;
    private String kind;
    private int value;
    private int level;
    private int adr;

    public SymbolTable(String name, String kind, int value, int level, int adr) {
        this.name = name;
        this.kind = kind;
        this.value = value;
        this.level = level;
        this.adr = adr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAdr() {
        return adr;
    }

    public void setAdr(int adr) {
        this.adr = adr;
    }

}
