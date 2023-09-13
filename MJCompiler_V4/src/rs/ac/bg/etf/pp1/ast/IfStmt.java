// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class IfStmt extends Statement {

    private BeforeCondition BeforeCondition;
    private Condition Condition;
    private RparentIfCondition RparentIfCondition;
    private Statement Statement;
    private IfStmtPartLast IfStmtPartLast;

    public IfStmt (BeforeCondition BeforeCondition, Condition Condition, RparentIfCondition RparentIfCondition, Statement Statement, IfStmtPartLast IfStmtPartLast) {
        this.BeforeCondition=BeforeCondition;
        if(BeforeCondition!=null) BeforeCondition.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.RparentIfCondition=RparentIfCondition;
        if(RparentIfCondition!=null) RparentIfCondition.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.IfStmtPartLast=IfStmtPartLast;
        if(IfStmtPartLast!=null) IfStmtPartLast.setParent(this);
    }

    public BeforeCondition getBeforeCondition() {
        return BeforeCondition;
    }

    public void setBeforeCondition(BeforeCondition BeforeCondition) {
        this.BeforeCondition=BeforeCondition;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public RparentIfCondition getRparentIfCondition() {
        return RparentIfCondition;
    }

    public void setRparentIfCondition(RparentIfCondition RparentIfCondition) {
        this.RparentIfCondition=RparentIfCondition;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public IfStmtPartLast getIfStmtPartLast() {
        return IfStmtPartLast;
    }

    public void setIfStmtPartLast(IfStmtPartLast IfStmtPartLast) {
        this.IfStmtPartLast=IfStmtPartLast;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BeforeCondition!=null) BeforeCondition.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(RparentIfCondition!=null) RparentIfCondition.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(IfStmtPartLast!=null) IfStmtPartLast.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BeforeCondition!=null) BeforeCondition.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(RparentIfCondition!=null) RparentIfCondition.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(IfStmtPartLast!=null) IfStmtPartLast.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BeforeCondition!=null) BeforeCondition.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(RparentIfCondition!=null) RparentIfCondition.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(IfStmtPartLast!=null) IfStmtPartLast.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStmt(\n");

        if(BeforeCondition!=null)
            buffer.append(BeforeCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RparentIfCondition!=null)
            buffer.append(RparentIfCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfStmtPartLast!=null)
            buffer.append(IfStmtPartLast.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStmt]");
        return buffer.toString();
    }
}
