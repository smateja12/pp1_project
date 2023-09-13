// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class FormParamOneComma extends FormParamElemComma {

    private FormalParameter FormalParameter;

    public FormParamOneComma (FormalParameter FormalParameter) {
        this.FormalParameter=FormalParameter;
        if(FormalParameter!=null) FormalParameter.setParent(this);
    }

    public FormalParameter getFormalParameter() {
        return FormalParameter;
    }

    public void setFormalParameter(FormalParameter FormalParameter) {
        this.FormalParameter=FormalParameter;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormalParameter!=null) FormalParameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormalParameter!=null) FormalParameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormalParameter!=null) FormalParameter.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParamOneComma(\n");

        if(FormalParameter!=null)
            buffer.append(FormalParameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParamOneComma]");
        return buffer.toString();
    }
}
