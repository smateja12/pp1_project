package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormalParameter;
import rs.ac.bg.etf.pp1.ast.VarDeclaration;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count = 0;
	
	public int getCount() {
		return count;
	}
	
	// brojanje formalnih parametara
	public static class FormParamCounter extends CounterVisitor {
		
		public void visit(FormalParameter formalParameter) {
			count++;
		}
		
	}
	
	// brojanje lokalnih parametara deklarisanih u okviru metode
	public static class VarCounter extends CounterVisitor {
		
		public void visit(VarDeclaration varDeclaration) {
			count++;
		}
		
	}
	
}
