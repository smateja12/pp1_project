// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class FindAndReplaceStmt extends Statement {

    private FindAndReplaceStart FindAndReplaceStart;
    private Expr Expr;
    private FindAndReplaceMiddle FindAndReplaceMiddle;
    private Expr Expr1;
    private FindAndReplaceEnd FindAndReplaceEnd;

    public FindAndReplaceStmt (FindAndReplaceStart FindAndReplaceStart, Expr Expr, FindAndReplaceMiddle FindAndReplaceMiddle, Expr Expr1, FindAndReplaceEnd FindAndReplaceEnd) {
        this.FindAndReplaceStart=FindAndReplaceStart;
        if(FindAndReplaceStart!=null) FindAndReplaceStart.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.FindAndReplaceMiddle=FindAndReplaceMiddle;
        if(FindAndReplaceMiddle!=null) FindAndReplaceMiddle.setParent(this);
        this.Expr1=Expr1;
        if(Expr1!=null) Expr1.setParent(this);
        this.FindAndReplaceEnd=FindAndReplaceEnd;
        if(FindAndReplaceEnd!=null) FindAndReplaceEnd.setParent(this);
    }

    public FindAndReplaceStart getFindAndReplaceStart() {
        return FindAndReplaceStart;
    }

    public void setFindAndReplaceStart(FindAndReplaceStart FindAndReplaceStart) {
        this.FindAndReplaceStart=FindAndReplaceStart;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public FindAndReplaceMiddle getFindAndReplaceMiddle() {
        return FindAndReplaceMiddle;
    }

    public void setFindAndReplaceMiddle(FindAndReplaceMiddle FindAndReplaceMiddle) {
        this.FindAndReplaceMiddle=FindAndReplaceMiddle;
    }

    public Expr getExpr1() {
        return Expr1;
    }

    public void setExpr1(Expr Expr1) {
        this.Expr1=Expr1;
    }

    public FindAndReplaceEnd getFindAndReplaceEnd() {
        return FindAndReplaceEnd;
    }

    public void setFindAndReplaceEnd(FindAndReplaceEnd FindAndReplaceEnd) {
        this.FindAndReplaceEnd=FindAndReplaceEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FindAndReplaceStart!=null) FindAndReplaceStart.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(FindAndReplaceMiddle!=null) FindAndReplaceMiddle.accept(visitor);
        if(Expr1!=null) Expr1.accept(visitor);
        if(FindAndReplaceEnd!=null) FindAndReplaceEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FindAndReplaceStart!=null) FindAndReplaceStart.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(FindAndReplaceMiddle!=null) FindAndReplaceMiddle.traverseTopDown(visitor);
        if(Expr1!=null) Expr1.traverseTopDown(visitor);
        if(FindAndReplaceEnd!=null) FindAndReplaceEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FindAndReplaceStart!=null) FindAndReplaceStart.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(FindAndReplaceMiddle!=null) FindAndReplaceMiddle.traverseBottomUp(visitor);
        if(Expr1!=null) Expr1.traverseBottomUp(visitor);
        if(FindAndReplaceEnd!=null) FindAndReplaceEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FindAndReplaceStmt(\n");

        if(FindAndReplaceStart!=null)
            buffer.append(FindAndReplaceStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FindAndReplaceMiddle!=null)
            buffer.append(FindAndReplaceMiddle.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr1!=null)
            buffer.append(Expr1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FindAndReplaceEnd!=null)
            buffer.append(FindAndReplaceEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FindAndReplaceStmt]");
        return buffer.toString();
    }
}
