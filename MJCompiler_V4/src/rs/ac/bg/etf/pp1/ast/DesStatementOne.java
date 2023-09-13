// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class DesStatementOne extends DesignatorStatement {

    private DesignatorStmtParam DesignatorStmtParam;

    public DesStatementOne (DesignatorStmtParam DesignatorStmtParam) {
        this.DesignatorStmtParam=DesignatorStmtParam;
        if(DesignatorStmtParam!=null) DesignatorStmtParam.setParent(this);
    }

    public DesignatorStmtParam getDesignatorStmtParam() {
        return DesignatorStmtParam;
    }

    public void setDesignatorStmtParam(DesignatorStmtParam DesignatorStmtParam) {
        this.DesignatorStmtParam=DesignatorStmtParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStmtParam!=null) DesignatorStmtParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStmtParam!=null) DesignatorStmtParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStmtParam!=null) DesignatorStmtParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesStatementOne(\n");

        if(DesignatorStmtParam!=null)
            buffer.append(DesignatorStmtParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesStatementOne]");
        return buffer.toString();
    }
}
