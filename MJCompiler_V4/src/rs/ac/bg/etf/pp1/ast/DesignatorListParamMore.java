// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class DesignatorListParamMore extends DesignatorListParam {

    private DesignatorListParam DesignatorListParam;
    private DesignatorElement DesignatorElement;

    public DesignatorListParamMore (DesignatorListParam DesignatorListParam, DesignatorElement DesignatorElement) {
        this.DesignatorListParam=DesignatorListParam;
        if(DesignatorListParam!=null) DesignatorListParam.setParent(this);
        this.DesignatorElement=DesignatorElement;
        if(DesignatorElement!=null) DesignatorElement.setParent(this);
    }

    public DesignatorListParam getDesignatorListParam() {
        return DesignatorListParam;
    }

    public void setDesignatorListParam(DesignatorListParam DesignatorListParam) {
        this.DesignatorListParam=DesignatorListParam;
    }

    public DesignatorElement getDesignatorElement() {
        return DesignatorElement;
    }

    public void setDesignatorElement(DesignatorElement DesignatorElement) {
        this.DesignatorElement=DesignatorElement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorListParam!=null) DesignatorListParam.accept(visitor);
        if(DesignatorElement!=null) DesignatorElement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorListParam!=null) DesignatorListParam.traverseTopDown(visitor);
        if(DesignatorElement!=null) DesignatorElement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorListParam!=null) DesignatorListParam.traverseBottomUp(visitor);
        if(DesignatorElement!=null) DesignatorElement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorListParamMore(\n");

        if(DesignatorListParam!=null)
            buffer.append(DesignatorListParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorElement!=null)
            buffer.append(DesignatorElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorListParamMore]");
        return buffer.toString();
    }
}
