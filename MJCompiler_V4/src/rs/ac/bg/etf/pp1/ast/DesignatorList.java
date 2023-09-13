// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class DesignatorList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private DesignatorListParam DesignatorListParam;

    public DesignatorList (DesignatorListParam DesignatorListParam) {
        this.DesignatorListParam=DesignatorListParam;
        if(DesignatorListParam!=null) DesignatorListParam.setParent(this);
    }

    public DesignatorListParam getDesignatorListParam() {
        return DesignatorListParam;
    }

    public void setDesignatorListParam(DesignatorListParam DesignatorListParam) {
        this.DesignatorListParam=DesignatorListParam;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorListParam!=null) DesignatorListParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorListParam!=null) DesignatorListParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorListParam!=null) DesignatorListParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorList(\n");

        if(DesignatorListParam!=null)
            buffer.append(DesignatorListParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorList]");
        return buffer.toString();
    }
}
