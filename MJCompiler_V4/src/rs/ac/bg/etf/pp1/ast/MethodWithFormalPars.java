// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class MethodWithFormalPars extends MethodFormPars {

    private FormPars FormPars;

    public MethodWithFormalPars (FormPars FormPars) {
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodWithFormalPars(\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodWithFormalPars]");
        return buffer.toString();
    }
}
