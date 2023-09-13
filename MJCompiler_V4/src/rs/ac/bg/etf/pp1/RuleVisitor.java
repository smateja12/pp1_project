package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor {
	
	int printCallCount = 0;
	int varDeclCount = 0;
	
//	Logger log = Logger.getLogger(getClass());
//	
//	public void visit(PrintStmt PrintStmt) { 
//		printCallCount++;
//	}
//	
//	public void visit(VarDecl VarDecl) { 
//		varDeclCount++;
//	}
	
	public void visit(PrintStmt PrintStmt) { 
		printCallCount++;
	}
	
    public void visit(PrintNumberStmt PrintNumberStmt) { 
    	printCallCount++;
    }
	
}
