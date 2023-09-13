// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class FormParamMore extends FormParamElem {

    private FormParamElemComma FormParamElemComma;
    private FormParamElem FormParamElem;

    public FormParamMore (FormParamElemComma FormParamElemComma, FormParamElem FormParamElem) {
        this.FormParamElemComma=FormParamElemComma;
        if(FormParamElemComma!=null) FormParamElemComma.setParent(this);
        this.FormParamElem=FormParamElem;
        if(FormParamElem!=null) FormParamElem.setParent(this);
    }

    public FormParamElemComma getFormParamElemComma() {
        return FormParamElemComma;
    }

    public void setFormParamElemComma(FormParamElemComma FormParamElemComma) {
        this.FormParamElemComma=FormParamElemComma;
    }

    public FormParamElem getFormParamElem() {
        return FormParamElem;
    }

    public void setFormParamElem(FormParamElem FormParamElem) {
        this.FormParamElem=FormParamElem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParamElemComma!=null) FormParamElemComma.accept(visitor);
        if(FormParamElem!=null) FormParamElem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParamElemComma!=null) FormParamElemComma.traverseTopDown(visitor);
        if(FormParamElem!=null) FormParamElem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParamElemComma!=null) FormParamElemComma.traverseBottomUp(visitor);
        if(FormParamElem!=null) FormParamElem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParamMore(\n");

        if(FormParamElemComma!=null)
            buffer.append(FormParamElemComma.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParamElem!=null)
            buffer.append(FormParamElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParamMore]");
        return buffer.toString();
    }
}
