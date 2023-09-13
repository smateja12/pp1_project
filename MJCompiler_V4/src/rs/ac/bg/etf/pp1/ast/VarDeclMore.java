// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class VarDeclMore extends VarDeclParam {

    private VarDeclParamComma VarDeclParamComma;
    private VarDeclParam VarDeclParam;

    public VarDeclMore (VarDeclParamComma VarDeclParamComma, VarDeclParam VarDeclParam) {
        this.VarDeclParamComma=VarDeclParamComma;
        if(VarDeclParamComma!=null) VarDeclParamComma.setParent(this);
        this.VarDeclParam=VarDeclParam;
        if(VarDeclParam!=null) VarDeclParam.setParent(this);
    }

    public VarDeclParamComma getVarDeclParamComma() {
        return VarDeclParamComma;
    }

    public void setVarDeclParamComma(VarDeclParamComma VarDeclParamComma) {
        this.VarDeclParamComma=VarDeclParamComma;
    }

    public VarDeclParam getVarDeclParam() {
        return VarDeclParam;
    }

    public void setVarDeclParam(VarDeclParam VarDeclParam) {
        this.VarDeclParam=VarDeclParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclParamComma!=null) VarDeclParamComma.accept(visitor);
        if(VarDeclParam!=null) VarDeclParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclParamComma!=null) VarDeclParamComma.traverseTopDown(visitor);
        if(VarDeclParam!=null) VarDeclParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclParamComma!=null) VarDeclParamComma.traverseBottomUp(visitor);
        if(VarDeclParam!=null) VarDeclParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclMore(\n");

        if(VarDeclParamComma!=null)
            buffer.append(VarDeclParamComma.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclParam!=null)
            buffer.append(VarDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclMore]");
        return buffer.toString();
    }
}
