// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class ReturnStmt extends Statement {

    private ExprParam ExprParam;

    public ReturnStmt (ExprParam ExprParam) {
        this.ExprParam=ExprParam;
        if(ExprParam!=null) ExprParam.setParent(this);
    }

    public ExprParam getExprParam() {
        return ExprParam;
    }

    public void setExprParam(ExprParam ExprParam) {
        this.ExprParam=ExprParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprParam!=null) ExprParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprParam!=null) ExprParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprParam!=null) ExprParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnStmt(\n");

        if(ExprParam!=null)
            buffer.append(ExprParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnStmt]");
        return buffer.toString();
    }
}
