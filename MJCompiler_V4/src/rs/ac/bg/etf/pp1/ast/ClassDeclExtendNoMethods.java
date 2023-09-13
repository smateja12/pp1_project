// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclExtendNoMethods extends ClassDecl {

    private BaseClassName BaseClassName;
    private Type Type;
    private VarDeclList VarDeclList;

    public ClassDeclExtendNoMethods (BaseClassName BaseClassName, Type Type, VarDeclList VarDeclList) {
        this.BaseClassName=BaseClassName;
        if(BaseClassName!=null) BaseClassName.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public BaseClassName getBaseClassName() {
        return BaseClassName;
    }

    public void setBaseClassName(BaseClassName BaseClassName) {
        this.BaseClassName=BaseClassName;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BaseClassName!=null) BaseClassName.accept(visitor);
        if(Type!=null) Type.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BaseClassName!=null) BaseClassName.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BaseClassName!=null) BaseClassName.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclExtendNoMethods(\n");

        if(BaseClassName!=null)
            buffer.append(BaseClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclExtendNoMethods]");
        return buffer.toString();
    }
}
