// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class ConstNumber extends ConstDeclaration {

    private String constName;
    private Integer n;

    public ConstNumber (String constName, Integer n) {
        this.constName=constName;
        this.n=n;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n=n;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstNumber(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(" "+tab+n);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNumber]");
        return buffer.toString();
    }
}
