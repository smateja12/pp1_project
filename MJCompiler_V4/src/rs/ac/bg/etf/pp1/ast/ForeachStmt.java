// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class ForeachStmt extends Statement {

    private ForeachPartFirst ForeachPartFirst;
    private Statement Statement;
    private ForeachPartLast ForeachPartLast;

    public ForeachStmt (ForeachPartFirst ForeachPartFirst, Statement Statement, ForeachPartLast ForeachPartLast) {
        this.ForeachPartFirst=ForeachPartFirst;
        if(ForeachPartFirst!=null) ForeachPartFirst.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ForeachPartLast=ForeachPartLast;
        if(ForeachPartLast!=null) ForeachPartLast.setParent(this);
    }

    public ForeachPartFirst getForeachPartFirst() {
        return ForeachPartFirst;
    }

    public void setForeachPartFirst(ForeachPartFirst ForeachPartFirst) {
        this.ForeachPartFirst=ForeachPartFirst;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ForeachPartLast getForeachPartLast() {
        return ForeachPartLast;
    }

    public void setForeachPartLast(ForeachPartLast ForeachPartLast) {
        this.ForeachPartLast=ForeachPartLast;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForeachPartFirst!=null) ForeachPartFirst.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ForeachPartLast!=null) ForeachPartLast.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForeachPartFirst!=null) ForeachPartFirst.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ForeachPartLast!=null) ForeachPartLast.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForeachPartFirst!=null) ForeachPartFirst.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ForeachPartLast!=null) ForeachPartLast.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForeachStmt(\n");

        if(ForeachPartFirst!=null)
            buffer.append(ForeachPartFirst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForeachPartLast!=null)
            buffer.append(ForeachPartLast.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForeachStmt]");
        return buffer.toString();
    }
}
