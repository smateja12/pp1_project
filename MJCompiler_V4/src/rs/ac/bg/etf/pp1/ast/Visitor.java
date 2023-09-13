// generated with ast extension for cup
// version 0.8
// 4/8/2023 10:32:47


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(ConstDeclParam ConstDeclParam);
    public void visit(VarDeclParamComma VarDeclParamComma);
    public void visit(Mulop Mulop);
    public void visit(Relop Relop);
    public void visit(Assignop Assignop);
    public void visit(ProgramDeclList ProgramDeclList);
    public void visit(VarDeclParam VarDeclParam);
    public void visit(CondTermList CondTermList);
    public void visit(FormParamElem FormParamElem);
    public void visit(StatementList StatementList);
    public void visit(VarDeclListTail VarDeclListTail);
    public void visit(Addop Addop);
    public void visit(ActualParameters ActualParameters);
    public void visit(Factor Factor);
    public void visit(CondFactList CondFactList);
    public void visit(StatementListTail StatementListTail);
    public void visit(DesignatorStmtParam DesignatorStmtParam);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(MethodFormPars MethodFormPars);
    public void visit(FormParamElemComma FormParamElemComma);
    public void visit(ConstDeclaration ConstDeclaration);
    public void visit(DesignatorListParam DesignatorListParam);
    public void visit(ExprList ExprList);
    public void visit(VarDeclList VarDeclList);
    public void visit(Expr Expr);
    public void visit(ActPars ActPars);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(ConstDeclParamComma ConstDeclParamComma);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(Statement Statement);
    public void visit(ExprParam ExprParam);
    public void visit(ClassDecl ClassDecl);
    public void visit(CondFact CondFact);
    public void visit(DesignatorElement DesignatorElement);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(DolarPart DolarPart);
    public void visit(FormPars FormPars);
    public void visit(ModOperand ModOperand);
    public void visit(DivOperand DivOperand);
    public void visit(MulOperand MulOperand);
    public void visit(MinusOperand MinusOperand);
    public void visit(PlusOperand PlusOperand);
    public void visit(LessOrEqualOperand LessOrEqualOperand);
    public void visit(LessOperand LessOperand);
    public void visit(GreaterOrEqualOperand GreaterOrEqualOperand);
    public void visit(GreaterOperand GreaterOperand);
    public void visit(NotEqualOperand NotEqualOperand);
    public void visit(EqualOperand EqualOperand);
    public void visit(AssignOperand AssignOperand);
    public void visit(Label Label);
    public void visit(DesignatorArrayName DesignatorArrayName);
    public void visit(DolarEmpty DolarEmpty);
    public void visit(DolarNonEmpty DolarNonEmpty);
    public void visit(DesignatorMoreIdentDot DesignatorMoreIdentDot);
    public void visit(DesignatorMoreExpr DesignatorMoreExpr);
    public void visit(DesignatorOne DesignatorOne);
    public void visit(FactorFuncCallBeginning FactorFuncCallBeginning);
    public void visit(FactorMaxArray FactorMaxArray);
    public void visit(FactorBinaryNumber FactorBinaryNumber);
    public void visit(FactorExpr FactorExpr);
    public void visit(FactorNewWithActualParams FactorNewWithActualParams);
    public void visit(FactorNewArrayAllocate FactorNewArrayAllocate);
    public void visit(FactorBool FactorBool);
    public void visit(FactorChar FactorChar);
    public void visit(FactorNumber FactorNumber);
    public void visit(FactorFuncCallActPars FactorFuncCallActPars);
    public void visit(FactorVar FactorVar);
    public void visit(TermFactorList TermFactorList);
    public void visit(TermFactor TermFactor);
    public void visit(ExprTermAddopList ExprTermAddopList);
    public void visit(ExpressionTerm ExpressionTerm);
    public void visit(ExpressionNegTerm ExpressionNegTerm);
    public void visit(CondFactMore CondFactMore);
    public void visit(CondFactOne CondFactOne);
    public void visit(CondFactListMore CondFactListMore);
    public void visit(CondFactListOne CondFactListOne);
    public void visit(CondTerm CondTerm);
    public void visit(BeforeLastOrElement BeforeLastOrElement);
    public void visit(CondTermListMore CondTermListMore);
    public void visit(CondTermListOne CondTermListOne);
    public void visit(Condition Condition);
    public void visit(ActualParams ActualParams);
    public void visit(ActualParam ActualParam);
    public void visit(NoActParams NoActParams);
    public void visit(ActParams ActParams);
    public void visit(ExprListMore ExprListMore);
    public void visit(ExprListOne ExprListOne);
    public void visit(DesignatorArrayFilter DesignatorArrayFilter);
    public void visit(DesignatorArrayInit DesignatorArrayInit);
    public void visit(SwapElementsArrayDesignator SwapElementsArrayDesignator);
    public void visit(DecrementDesignator DecrementDesignator);
    public void visit(IncrementDesignator IncrementDesignator);
    public void visit(ActParsDesignator ActParsDesignator);
    public void visit(DesignatorErrorSemi DesignatorErrorSemi);
    public void visit(AssignDesignator AssignDesignator);
    public void visit(NoDesignatorElem NoDesignatorElem);
    public void visit(DesignatorElem DesignatorElem);
    public void visit(DesignatorListParamMore DesignatorListParamMore);
    public void visit(DesignatorListParamOne DesignatorListParamOne);
    public void visit(DesignatorList DesignatorList);
    public void visit(DesStatementMore DesStatementMore);
    public void visit(DesStatementOne DesStatementOne);
    public void visit(NoExpression NoExpression);
    public void visit(ExpressionExist ExpressionExist);
    public void visit(IfStmtPartLast IfStmtPartLast);
    public void visit(BeforeCondition BeforeCondition);
    public void visit(WhileStmtFirstPart WhileStmtFirstPart);
    public void visit(FindAnyPartLast FindAnyPartLast);
    public void visit(FindAnyPartFirst FindAnyPartFirst);
    public void visit(ForeachPartLast ForeachPartLast);
    public void visit(ForeachPartFirst ForeachPartFirst);
    public void visit(FindAnyCheckExpression FindAnyCheckExpression);
    public void visit(FindAndReplaceEnd FindAndReplaceEnd);
    public void visit(FindAndReplaceMiddle FindAndReplaceMiddle);
    public void visit(FindAndReplaceStart FindAndReplaceStart);
    public void visit(WhileStmtLastPart WhileStmtLastPart);
    public void visit(RparentIfCondition RparentIfCondition);
    public void visit(SkipStmt SkipStmt);
    public void visit(FindAndReplaceStmt FindAndReplaceStmt);
    public void visit(FindAnyStmt FindAnyStmt);
    public void visit(ForeachStmt ForeachStmt);
    public void visit(IfStmtErrorRparen IfStmtErrorRparen);
    public void visit(IfElseStmt IfElseStmt);
    public void visit(IfStmt IfStmt);
    public void visit(Statements Statements);
    public void visit(PrintStmt PrintStmt);
    public void visit(PrintNumberStmt PrintNumberStmt);
    public void visit(ReadStmt ReadStmt);
    public void visit(ReturnStmt ReturnStmt);
    public void visit(ContinueStmt ContinueStmt);
    public void visit(BreakStmt BreakStmt);
    public void visit(WhileStmt WhileStmt);
    public void visit(DesignatorStmt DesignatorStmt);
    public void visit(FormalParameter FormalParameter);
    public void visit(FormParamErrorComma FormParamErrorComma);
    public void visit(FormParamOneComma FormParamOneComma);
    public void visit(FormParamErrorRparen FormParamErrorRparen);
    public void visit(FormParamOne FormParamOne);
    public void visit(FormParamMore FormParamMore);
    public void visit(FormalPars FormalPars);
    public void visit(NoStatementListMore NoStatementListMore);
    public void visit(StatementListMore StatementListMore);
    public void visit(NoStmts NoStmts);
    public void visit(StatementListOne StatementListOne);
    public void visit(MethodNoFormalPars MethodNoFormalPars);
    public void visit(MethodWithFormalPars MethodWithFormalPars);
    public void visit(MethodVoid MethodVoid);
    public void visit(MethodReturn MethodReturn);
    public void visit(MethodDecl MethodDecl);
    public void visit(NoVarDeclListMore NoVarDeclListMore);
    public void visit(VarDeclListMore VarDeclListMore);
    public void visit(NoVarDeclList NoVarDeclList);
    public void visit(VarDeclListOne VarDeclListOne);
    public void visit(BaseClassName BaseClassName);
    public void visit(ClassDeclNoExtendNoMethods ClassDeclNoExtendNoMethods);
    public void visit(ClassDeclNoExtendFull ClassDeclNoExtendFull);
    public void visit(ClassDeclExtendNoMethods ClassDeclExtendNoMethods);
    public void visit(ClassDeclExtendFull ClassDeclExtendFull);
    public void visit(NoVarDeclArray NoVarDeclArray);
    public void visit(VarDeclArray VarDeclArray);
    public void visit(VarDeclaration VarDeclaration);
    public void visit(VarDeclErrorComma VarDeclErrorComma);
    public void visit(VarDeclOneComma VarDeclOneComma);
    public void visit(VarDeclErrorSemi VarDeclErrorSemi);
    public void visit(VarDeclOne VarDeclOne);
    public void visit(VarDeclMore VarDeclMore);
    public void visit(VarDecl VarDecl);
    public void visit(Type Type);
    public void visit(ConstBool ConstBool);
    public void visit(ConstChar ConstChar);
    public void visit(ConstNumber ConstNumber);
    public void visit(ConstDeclErrorComma ConstDeclErrorComma);
    public void visit(ConstDeclOneComma ConstDeclOneComma);
    public void visit(ConstDeclErrorSemi ConstDeclErrorSemi);
    public void visit(ConstDeclOne ConstDeclOne);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(ConstDecl ConstDecl);
    public void visit(NoMethodDecls NoMethodDecls);
    public void visit(MethodDecls MethodDecls);
    public void visit(NoDecls NoDecls);
    public void visit(ClassDecls ClassDecls);
    public void visit(VarDecls VarDecls);
    public void visit(ConstDecls ConstDecls);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
