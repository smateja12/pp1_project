// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class StatementListMore extends StatementListTail {

    private Statement Statement;
    private StatementListTail StatementListTail;

    public StatementListMore (Statement Statement, StatementListTail StatementListTail) {
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.StatementListTail=StatementListTail;
        if(StatementListTail!=null) StatementListTail.setParent(this);
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public StatementListTail getStatementListTail() {
        return StatementListTail;
    }

    public void setStatementListTail(StatementListTail StatementListTail) {
        this.StatementListTail=StatementListTail;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Statement!=null) Statement.accept(visitor);
        if(StatementListTail!=null) StatementListTail.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(StatementListTail!=null) StatementListTail.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(StatementListTail!=null) StatementListTail.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementListMore(\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementListTail!=null)
            buffer.append(StatementListTail.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementListMore]");
        return buffer.toString();
    }
}
