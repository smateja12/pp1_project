// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class DesignatorMoreExpr extends Designator {

    private DesignatorArrayName DesignatorArrayName;
    private Expr Expr;
    private DolarPart DolarPart;

    public DesignatorMoreExpr (DesignatorArrayName DesignatorArrayName, Expr Expr, DolarPart DolarPart) {
        this.DesignatorArrayName=DesignatorArrayName;
        if(DesignatorArrayName!=null) DesignatorArrayName.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.DolarPart=DolarPart;
        if(DolarPart!=null) DolarPart.setParent(this);
    }

    public DesignatorArrayName getDesignatorArrayName() {
        return DesignatorArrayName;
    }

    public void setDesignatorArrayName(DesignatorArrayName DesignatorArrayName) {
        this.DesignatorArrayName=DesignatorArrayName;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public DolarPart getDolarPart() {
        return DolarPart;
    }

    public void setDolarPart(DolarPart DolarPart) {
        this.DolarPart=DolarPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorArrayName!=null) DesignatorArrayName.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(DolarPart!=null) DolarPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorArrayName!=null) DesignatorArrayName.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(DolarPart!=null) DolarPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorArrayName!=null) DesignatorArrayName.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(DolarPart!=null) DolarPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorMoreExpr(\n");

        if(DesignatorArrayName!=null)
            buffer.append(DesignatorArrayName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DolarPart!=null)
            buffer.append(DolarPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorMoreExpr]");
        return buffer.toString();
    }
}
