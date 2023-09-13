// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class DesignatorListParamOne extends DesignatorListParam {

    private DesignatorElement DesignatorElement;

    public DesignatorListParamOne (DesignatorElement DesignatorElement) {
        this.DesignatorElement=DesignatorElement;
        if(DesignatorElement!=null) DesignatorElement.setParent(this);
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
        if(DesignatorElement!=null) DesignatorElement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorElement!=null) DesignatorElement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorElement!=null) DesignatorElement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorListParamOne(\n");

        if(DesignatorElement!=null)
            buffer.append(DesignatorElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorListParamOne]");
        return buffer.toString();
    }
}
