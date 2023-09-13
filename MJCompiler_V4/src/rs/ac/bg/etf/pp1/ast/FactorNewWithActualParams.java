// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class FactorNewWithActualParams extends Factor {

    private Type Type;
    private ActualParameters ActualParameters;

    public FactorNewWithActualParams (Type Type, ActualParameters ActualParameters) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ActualParameters=ActualParameters;
        if(ActualParameters!=null) ActualParameters.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ActualParameters getActualParameters() {
        return ActualParameters;
    }

    public void setActualParameters(ActualParameters ActualParameters) {
        this.ActualParameters=ActualParameters;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ActualParameters!=null) ActualParameters.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ActualParameters!=null) ActualParameters.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ActualParameters!=null) ActualParameters.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNewWithActualParams(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParameters!=null)
            buffer.append(ActualParameters.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNewWithActualParams]");
        return buffer.toString();
    }
}
