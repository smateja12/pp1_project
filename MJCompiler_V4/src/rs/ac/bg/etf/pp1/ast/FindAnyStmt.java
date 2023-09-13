// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class FindAnyStmt extends Statement {

    private FindAnyPartFirst FindAnyPartFirst;
    private Expr Expr;
    private FindAnyCheckExpression FindAnyCheckExpression;
    private FindAnyPartLast FindAnyPartLast;

    public FindAnyStmt (FindAnyPartFirst FindAnyPartFirst, Expr Expr, FindAnyCheckExpression FindAnyCheckExpression, FindAnyPartLast FindAnyPartLast) {
        this.FindAnyPartFirst=FindAnyPartFirst;
        if(FindAnyPartFirst!=null) FindAnyPartFirst.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.FindAnyCheckExpression=FindAnyCheckExpression;
        if(FindAnyCheckExpression!=null) FindAnyCheckExpression.setParent(this);
        this.FindAnyPartLast=FindAnyPartLast;
        if(FindAnyPartLast!=null) FindAnyPartLast.setParent(this);
    }

    public FindAnyPartFirst getFindAnyPartFirst() {
        return FindAnyPartFirst;
    }

    public void setFindAnyPartFirst(FindAnyPartFirst FindAnyPartFirst) {
        this.FindAnyPartFirst=FindAnyPartFirst;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public FindAnyCheckExpression getFindAnyCheckExpression() {
        return FindAnyCheckExpression;
    }

    public void setFindAnyCheckExpression(FindAnyCheckExpression FindAnyCheckExpression) {
        this.FindAnyCheckExpression=FindAnyCheckExpression;
    }

    public FindAnyPartLast getFindAnyPartLast() {
        return FindAnyPartLast;
    }

    public void setFindAnyPartLast(FindAnyPartLast FindAnyPartLast) {
        this.FindAnyPartLast=FindAnyPartLast;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FindAnyPartFirst!=null) FindAnyPartFirst.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(FindAnyCheckExpression!=null) FindAnyCheckExpression.accept(visitor);
        if(FindAnyPartLast!=null) FindAnyPartLast.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FindAnyPartFirst!=null) FindAnyPartFirst.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(FindAnyCheckExpression!=null) FindAnyCheckExpression.traverseTopDown(visitor);
        if(FindAnyPartLast!=null) FindAnyPartLast.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FindAnyPartFirst!=null) FindAnyPartFirst.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(FindAnyCheckExpression!=null) FindAnyCheckExpression.traverseBottomUp(visitor);
        if(FindAnyPartLast!=null) FindAnyPartLast.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FindAnyStmt(\n");

        if(FindAnyPartFirst!=null)
            buffer.append(FindAnyPartFirst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FindAnyCheckExpression!=null)
            buffer.append(FindAnyCheckExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FindAnyPartLast!=null)
            buffer.append(FindAnyPartLast.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FindAnyStmt]");
        return buffer.toString();
    }
}
