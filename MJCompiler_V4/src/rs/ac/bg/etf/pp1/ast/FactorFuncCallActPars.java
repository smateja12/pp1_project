// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public class FactorFuncCallActPars extends Factor {

    private FactorFuncCallBeginning FactorFuncCallBeginning;
    private ActualParameters ActualParameters;

    public FactorFuncCallActPars (FactorFuncCallBeginning FactorFuncCallBeginning, ActualParameters ActualParameters) {
        this.FactorFuncCallBeginning=FactorFuncCallBeginning;
        if(FactorFuncCallBeginning!=null) FactorFuncCallBeginning.setParent(this);
        this.ActualParameters=ActualParameters;
        if(ActualParameters!=null) ActualParameters.setParent(this);
    }

    public FactorFuncCallBeginning getFactorFuncCallBeginning() {
        return FactorFuncCallBeginning;
    }

    public void setFactorFuncCallBeginning(FactorFuncCallBeginning FactorFuncCallBeginning) {
        this.FactorFuncCallBeginning=FactorFuncCallBeginning;
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
        if(FactorFuncCallBeginning!=null) FactorFuncCallBeginning.accept(visitor);
        if(ActualParameters!=null) ActualParameters.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorFuncCallBeginning!=null) FactorFuncCallBeginning.traverseTopDown(visitor);
        if(ActualParameters!=null) ActualParameters.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorFuncCallBeginning!=null) FactorFuncCallBeginning.traverseBottomUp(visitor);
        if(ActualParameters!=null) ActualParameters.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorFuncCallActPars(\n");

        if(FactorFuncCallBeginning!=null)
            buffer.append(FactorFuncCallBeginning.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualParameters!=null)
            buffer.append(ActualParameters.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorFuncCallActPars]");
        return buffer.toString();
    }
}
