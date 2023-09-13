// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclList extends ConstDeclParam {

    private ConstDeclParamComma ConstDeclParamComma;
    private ConstDeclParam ConstDeclParam;

    public ConstDeclList (ConstDeclParamComma ConstDeclParamComma, ConstDeclParam ConstDeclParam) {
        this.ConstDeclParamComma=ConstDeclParamComma;
        if(ConstDeclParamComma!=null) ConstDeclParamComma.setParent(this);
        this.ConstDeclParam=ConstDeclParam;
        if(ConstDeclParam!=null) ConstDeclParam.setParent(this);
    }

    public ConstDeclParamComma getConstDeclParamComma() {
        return ConstDeclParamComma;
    }

    public void setConstDeclParamComma(ConstDeclParamComma ConstDeclParamComma) {
        this.ConstDeclParamComma=ConstDeclParamComma;
    }

    public ConstDeclParam getConstDeclParam() {
        return ConstDeclParam;
    }

    public void setConstDeclParam(ConstDeclParam ConstDeclParam) {
        this.ConstDeclParam=ConstDeclParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclParamComma!=null) ConstDeclParamComma.accept(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclParamComma!=null) ConstDeclParamComma.traverseTopDown(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclParamComma!=null) ConstDeclParamComma.traverseBottomUp(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclList(\n");

        if(ConstDeclParamComma!=null)
            buffer.append(ConstDeclParamComma.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclParam!=null)
            buffer.append(ConstDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclList]");
        return buffer.toString();
    }
}
