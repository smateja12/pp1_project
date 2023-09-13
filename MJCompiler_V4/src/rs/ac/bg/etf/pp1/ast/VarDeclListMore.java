// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class VarDeclListMore extends VarDeclListTail {

    private VarDecl VarDecl;
    private VarDeclListTail VarDeclListTail;

    public VarDeclListMore (VarDecl VarDecl, VarDeclListTail VarDeclListTail) {
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.VarDeclListTail=VarDeclListTail;
        if(VarDeclListTail!=null) VarDeclListTail.setParent(this);
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public VarDeclListTail getVarDeclListTail() {
        return VarDeclListTail;
    }

    public void setVarDeclListTail(VarDeclListTail VarDeclListTail) {
        this.VarDeclListTail=VarDeclListTail;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(VarDeclListTail!=null) VarDeclListTail.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(VarDeclListTail!=null) VarDeclListTail.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(VarDeclListTail!=null) VarDeclListTail.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclListMore(\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListTail!=null)
            buffer.append(VarDeclListTail.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclListMore]");
        return buffer.toString();
    }
}
