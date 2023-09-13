// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class WhileStmt extends Statement {

    private WhileStmtFirstPart WhileStmtFirstPart;
    private Condition Condition;
    private RparentIfCondition RparentIfCondition;
    private Statement Statement;
    private WhileStmtLastPart WhileStmtLastPart;

    public WhileStmt (WhileStmtFirstPart WhileStmtFirstPart, Condition Condition, RparentIfCondition RparentIfCondition, Statement Statement, WhileStmtLastPart WhileStmtLastPart) {
        this.WhileStmtFirstPart=WhileStmtFirstPart;
        if(WhileStmtFirstPart!=null) WhileStmtFirstPart.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.RparentIfCondition=RparentIfCondition;
        if(RparentIfCondition!=null) RparentIfCondition.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileStmtLastPart=WhileStmtLastPart;
        if(WhileStmtLastPart!=null) WhileStmtLastPart.setParent(this);
    }

    public WhileStmtFirstPart getWhileStmtFirstPart() {
        return WhileStmtFirstPart;
    }

    public void setWhileStmtFirstPart(WhileStmtFirstPart WhileStmtFirstPart) {
        this.WhileStmtFirstPart=WhileStmtFirstPart;
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

    public WhileStmtLastPart getWhileStmtLastPart() {
        return WhileStmtLastPart;
    }

    public void setWhileStmtLastPart(WhileStmtLastPart WhileStmtLastPart) {
        this.WhileStmtLastPart=WhileStmtLastPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(WhileStmtFirstPart!=null) WhileStmtFirstPart.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(RparentIfCondition!=null) RparentIfCondition.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileStmtLastPart!=null) WhileStmtLastPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(WhileStmtFirstPart!=null) WhileStmtFirstPart.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(RparentIfCondition!=null) RparentIfCondition.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileStmtLastPart!=null) WhileStmtLastPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(WhileStmtFirstPart!=null) WhileStmtFirstPart.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(RparentIfCondition!=null) RparentIfCondition.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileStmtLastPart!=null) WhileStmtLastPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("WhileStmt(\n");

        if(WhileStmtFirstPart!=null)
            buffer.append(WhileStmtFirstPart.toString("  "+tab));
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

        if(WhileStmtLastPart!=null)
            buffer.append(WhileStmtLastPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [WhileStmt]");
        return buffer.toString();
    }
}
