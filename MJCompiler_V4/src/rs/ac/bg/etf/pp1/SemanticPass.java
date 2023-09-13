package rs.ac.bg.etf.pp1;

import java.util.*;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class SemanticPass extends VisitorAdaptor {
	
	public int nVars;
	int printCallCount = 0;
	int varDeclCount = 0;
	boolean returnFound = false; // da li je detektovana return naredba
	boolean emptyReturnFound = false;
	boolean errorDetected = false;
	boolean mainMethodFound = false; // da li je detektovana main metoda
	boolean variableArrayDeclared = false; // flag da li je promenljiva deklarisana kao niz
	String relationOperatorName = null;
	int whileLoopsCount = 0; // koliko ima while petlji
	
	// foreach
	boolean foreachLoopDetected = false;
	String currentForeachDesignator = null;
	String currentForeachIterator = null;
	Map<String, List<String>> foreachLoopsIterators = new HashMap<String, List<String>>();
	
	Logger log = Logger.getLogger(getClass());
	
	Struct temporaryType = null;
	Obj currentMethod = null;
	int methodFormParsDefined = 0;
	String currentConstVariableName = null;
	Obj currentDeclaredClass = null;
	
	class FunctionNode implements Map.Entry<String, List<Struct>> {
		String functionName;
		List<Struct> formalParameters;
		
		public FunctionNode(String functionName) {
			this.functionName = functionName;
			this.formalParameters = new ArrayList<>();
		}
		
		@Override
		public String getKey() {
			return functionName;
		}

		@Override
		public List<Struct> getValue() {
			return formalParameters;
		}

		@Override
		public List<Struct> setValue(List<Struct> value) {
			this.formalParameters = value;
			return this.getValue();
		}
		
	}
	
	// lista svih definisanih/deklarisanih metoda
	List<FunctionNode> allFunctions = new ArrayList<>();
	
	// lista stvarnih parametara funkcije
	// kada naidjemo na actPars smenu, dodajemo po 1 stvarni argument u ovu listu
	List<List<Struct>> functionActualParameters = new ArrayList<>();
	
	FunctionNode findMethodByName(List<FunctionNode> allFunctions, String targetMethodName) {
		for (FunctionNode method : allFunctions) {
			if (method.getKey().equals(targetMethodName)) {
				return method;
			}
		}
		return null;
	}
	
	int findMethodIndexByName(List<FunctionNode> allFunctions, String targetMethodName) {
		for (int i = 0; i < allFunctions.size(); i++) {
			if (allFunctions.get(i).getKey().equals(targetMethodName)) {
				return i;
			}
		}
		return -1;
	}
 	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0) {
			msg.append(" na liniji ").append(line);
		}
		log.error(msg.toString());
	}
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0) {
			msg.append(" na liniji ").append(line);
		}
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	public SemanticPass() {
		// dodati tip bool u universe scope
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", ChildTab.boolType));
	}
	
	// smena Statement := print ( Expr:e ) ;
	public void visit(PrintStmt printStmt) { 
		printCallCount++;
		// Expr mora biti tipa int, char, bool
		if (printStmt.getExpr().struct != Tab.intType 
			&& printStmt.getExpr().struct != Tab.charType 
			&& printStmt.getExpr().struct != ChildTab.boolType) {
			report_error("Semanticka greska na liniji " + printStmt.getLine() 
			 + " : U smeni print, Expr(Izraz) " + getTypeByKind(printStmt.getExpr().struct.getKind()) 
			 + " nije tipa int/char ili bool!", null);
			return;
		}
	}
	
    public void visit(PrintNumberStmt printNumberStmt) { 
    	printCallCount++;
    	if (printNumberStmt.getExpr().struct != Tab.intType 
			&& printNumberStmt.getExpr().struct != Tab.charType 
			&& printNumberStmt.getExpr().struct != ChildTab.boolType) {
			report_error("Semanticka greska na liniji " + printNumberStmt.getLine() 
				+ " : U smeni print, Expr(Izraz) " + getTypeByKind(printNumberStmt.getExpr().struct.getKind()) 
				+ " nije tipa int/char ili bool!", null);
		}
    }
    
    // ProgName smena
    // obrada deklaracije programa
    public void visit(ProgName progName) {
    	if (Tab.find(progName.getProgName()) != Tab.noObj) {
    		report_error("Semanticka greska na liniji " + progName.getLine() 
    			+ " : Ime programa " + progName.getProgName() 
    			+ " vec postoji u tabeli simbola kao definisamo ime!", null);
    		progName.obj = Tab.noObj;
    	}else {
    		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
    	}
    	Tab.openScope();
    }
	
    // Program smena
    public void visit(Program program) {
    	nVars = Tab.currentScope.getnVars();
    	
    	// ulancavamo sve lokalne simbole u polje locals od Obj cvora ProgName:p
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
    	
    	// prijaviti gresku ako program nema deklarisanu main metodu koja je tipa void
    	// TODO mainMethodFound postaviti na true kada se detektuje sledeca smena:
    	// void main() {...}
    	if (mainMethodFound == false) {
    		report_error("Semanticka greska na liniji: " + program.getLine() + " : U programu " + program.getProgName().getProgName() + " nije navedena main metoda bez argumenata!", null);
    	}
    }
    
    // ConstDecl smena
    // ConstDeclaration je tipa Obj
    
    public void visit(ConstNumber constNumber) {
    	currentConstVariableName = constNumber.getConstName();
    	
    	// prijaviti gresku ako constNumber nije tipa int
    	if (temporaryType.equals(Tab.intType) == false) {
    		report_error("Semanticka greska na liniji: " + constNumber.getLine() + " : Promenljiva " + currentConstVariableName + " nije deklarisana kao tip INT!", null);
    		return;
    	}
    	
		if (Tab.noObj != Tab.find(currentConstVariableName)) {
			report_error("Semanticka greska na liniji: " + constNumber.getLine() + " : Promenljiva " + currentConstVariableName + " je prethodno vec deklarisana!", null);
    		return;
    	}
    	
    	Obj node = Tab.insert(Obj.Con, currentConstVariableName, Tab.intType);
    	// postaviti vrednost konstante
    	node.setAdr(constNumber.getN());
    	
    	// postaviti currentConstVariableName na null
    	currentConstVariableName = null;
    }
    
    public void visit(ConstChar constChar) {
		currentConstVariableName = constChar.getConstName();
    	
    	// prijaviti gresku ako constNumber nije tipa int
    	if (temporaryType.equals(Tab.charType) == false) {
    		report_error("Semanticka greska na liniji: " + constChar.getLine() + " : Konstanta " + currentConstVariableName + " nije deklarisana kao tip CHAR!", null);
    		return;
    	}
    	
		if (Tab.noObj != Tab.find(currentConstVariableName)) {
			report_error("Semanticka greska na liniji: " + constChar.getLine() + " : Konstanta " + currentConstVariableName + " je prethodno vec deklarisana!", null);
    		return;
    	}
    	
    	Obj node = Tab.insert(Obj.Con, currentConstVariableName, Tab.charType);
    	// postaviti vrednost konstante
    	node.setAdr(constChar.getCh());
    	
    	// postaviti currentConstVariableName na null
    	currentConstVariableName = null;
    }
    
    public void visit(ConstBool constBool) {
		currentConstVariableName = constBool.getConstName();
    	
    	// prijaviti gresku ako constNumber nije tipa int
    	if (temporaryType.equals(ChildTab.boolType) == false) {
    		report_error("Semanticka greska na liniji: " + constBool.getLine() + " : Konstanta " + currentConstVariableName + " nije deklarisana kao tip BOOL!", null);
    		return;
    	}
    	
		if (Tab.noObj != Tab.find(currentConstVariableName)) {
			report_error("Semanticka greska na liniji: " + constBool.getLine() + " : Konstanta " + currentConstVariableName + " je prethodno vec deklarisana!", null);
    		return;
    	}
    	
    	Obj node = Tab.insert(Obj.Con, currentConstVariableName, ChildTab.boolType);
    	// postaviti vrednost konstante
    	boolean boolValue = constBool.getB();
    	if (boolValue) {
    		node.setAdr(1);
    	}else {
    		node.setAdr(0);
    	}
    	
    	// postaviti currentConstVariableName na null
    	currentConstVariableName = null;
    }
    
    // VarDecl smena
    // obrada deklaracije globalnih promenljivih
    public void visit(VarDecl varDecl) { 
    	
    }
    
    public void visit(VarDeclArray varDeclArray) {
    	variableArrayDeclared = true;
    }
    
    public void visit(NoVarDeclArray noVarDeclArray) {
    	variableArrayDeclared = false;
    }
    
    public void visit(VarDeclaration varDeclaration) {
    	// proveriti da li je vec deklarisana promenljiva
    	boolean variableAlreadyDeclared = (Tab.currentScope.findSymbol(varDeclaration.getVarName()) != null) ? true : false;
    	if (variableAlreadyDeclared) {
    		report_error("Semanticka greska na liniji " + varDeclaration.getLine() + " : Promenljiva " + varDeclaration.getVarName() + " je vec prethodno deklarisana!", null);
    		return;
    	}
    	
    	varDeclCount++;
    	report_info("Deklarisana promenljiva: " + varDeclaration.getVarName(), varDeclaration);
    	
    	// proveriti da li je deklarisana promenljiva niz/obicna promenljiva
    	Struct varType = null;
    	if (variableArrayDeclared == false) {
    		varType = temporaryType;
    	}else {
       		varType = new Struct(Struct.Array, temporaryType); 
    		variableArrayDeclared = false;
    	}
    	if (varType != null) {
    		Tab.insert(Obj.Var, varDeclaration.getVarName(), varType);
    	}else {
    		report_error("Semanticka greska na liniji " + varDeclaration.getLine() + " : Promenljiva " + varDeclaration.getVarName() + " je neuspesno ubacena u tabelu simbola!", null);
    	}
    }
    
    // smena ClassDecl
    public void visit(BaseClassName baseClassName) {
    	// proveriti da li je klasa vec deklarisana
    	Obj classNode = Tab.find(baseClassName.getClassName());
    	if (classNode == Tab.noObj) {
    		// nova deklaracija klase
    		currentDeclaredClass = Tab.insert(Obj.Type, baseClassName.getClassName(), new Struct(Struct.Class));
    		baseClassName.obj = currentDeclaredClass;
    	}else {
    		report_error("Semanticka greska na liniji " + baseClassName.getLine() 
				+ " : Klasa " + baseClassName.getClassName() 
				+ " je vec prethodno deklarisana!", null);
    		currentDeclaredClass = Tab.noObj;
    	}
    	Tab.openScope();
    	report_info("Deklarisana klasa: " + baseClassName.getClassName(), baseClassName);
    }
    
    public void visit(ClassDeclExtendFull classDeclExtendFull) {
    	Tab.chainLocalSymbols(currentDeclaredClass.getType());
    	Tab.closeScope();
    	currentDeclaredClass = null;
    }
    
    public void visit(ClassDeclExtendNoMethods ClassDeclExtendNoMethods) {
    	Tab.chainLocalSymbols(currentDeclaredClass.getType());
    	Tab.closeScope();
    	currentDeclaredClass = null;
    }
    
    public void visit(ClassDeclNoExtendFull ClassDeclNoExtendFull) {
    	Tab.chainLocalSymbols(currentDeclaredClass.getType());
    	Tab.closeScope();
    	currentDeclaredClass = null;
    }

    public void visit(ClassDeclNoExtendNoMethods ClassDeclNoExtendNoMethods) {
    	Tab.chainLocalSymbols(currentDeclaredClass.getType());
    	Tab.closeScope();
    	currentDeclaredClass = null;
    }
  
    public void visit(Type type) {
    	// da li se radi o tipu    	
    	Obj typeNode = ChildTab.find(type.getTypeName());
    	if (typeNode == ChildTab.noObj) {
    		// nije nadjen objektni cvor
    		// javljamo semanticku gresku za prijavu gresaka
    		report_error("Semanticka greska: Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola!", null);
    		type.struct = ChildTab.noType;
    	}else {
    		System.out.println("PRONADJEN TIP: " + type.getTypeName() + " na liniji " + type.getLine());
    		if (Obj.Type == typeNode.getKind()) {
    			type.struct = typeNode.getType();
    		}else {
    			report_error("Semanticka greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = ChildTab.noType;
    		}
    	}
    	
    	temporaryType = type.struct;
    }
    
    // smena MethodDecl
    // obrada deklaracije metoda
    public void visit(MethodReturn methodReturn) {
    	// metoda sa povratnom vrednoscu
    	// proveriti da li je metoda prethodno vec deklarisana
    	Obj methodNode = Tab.currentScope().findSymbol(methodReturn.getMethodName());
    	if (methodNode == null) {
    		currentMethod = Tab.insert(Obj.Meth, methodReturn.getMethodName(), methodReturn.getType().struct);    
        	methodReturn.obj = currentMethod;
    	}else {
    		report_error("Semanticka greska na liniji " + methodReturn.getLine() + " : Ime " + methodReturn.getMethodName() + " je vec deklarisano!", null);
    		currentMethod = methodNode;
    		return;
    	}
    	Tab.openScope();
    	report_info("Obradjuje se funkcija: " + methodReturn.getMethodName(), methodReturn);
    	    	
    	allFunctions.add(new FunctionNode(methodReturn.getMethodName()));
    	
    	// koliko tekuca metoda ima formalnih parametara
    	methodFormParsDefined = 0;
    	
    	// postaviti flag mainMethodFound = true
    	int flg = methodReturn.getMethodName().compareTo("main");
    	if (flg == 0) {
    		mainMethodFound = true;
    		report_error("Semanticka greska na liniji " + methodReturn.getLine() + " : Main metoda mora biti tipa void!", null);
    	}
    	
    }
    
    public void visit(MethodVoid methodVoid) {
    	Obj methodNode = Tab.currentScope().findSymbol(methodVoid.getMethodName());
    	if (methodNode == null) {
    		currentMethod = Tab.insert(Obj.Meth, methodVoid.getMethodName(), Tab.noType);
        	methodVoid.obj = currentMethod;
    	}else {
    		report_error("Semanticka greska na liniji " + methodVoid.getLine() + " : Ime " + methodVoid.getMethodName() + " je vec deklarisano!", null);
    		currentMethod = methodNode;
    		return;
    	}
    	Tab.openScope();
    	report_info("Obradjuje se funkcija: " + methodVoid.getMethodName(), methodVoid);
    	
    	allFunctions.add(new FunctionNode(methodVoid.getMethodName()));
    	
    	// koliko tekuca metoda ima formalnih parametara
    	methodFormParsDefined = 0;
    	
    	int flg = methodVoid.getMethodName().compareTo("main");
    	if (flg == 0) {
    		mainMethodFound = true;
    	}
    }
    
    public void visit(MethodDecl methodDecl) {
    	Obj obj = Tab.find(currentMethod.getName());
    	if (!returnFound 
			&& currentMethod.getType() != Tab.noType
			&& obj != null && obj.getKind() == Obj.Meth) {
    		report_error("Semanticka greska na liniji " + methodDecl.getLine() + " : funkcija " + currentMethod.getName() + " nema return iskaz.", null);
    	}

    	// Prijaviti semanticku gresku ukoliko main metoda ima argumente
    	if (methodDecl.getMethodTypeName().obj != null && methodFormParsDefined != 0 && 
    			methodDecl.getMethodTypeName().obj.getName().equals("main")) {
    		report_error("Semanticka greska na liniji " + methodDecl.getLine() + " : Main metoda mora biti definisana bez argumenata!", null);
    	}
    	
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	emptyReturnFound = false;
    	returnFound = false;
    	currentMethod = null;
    	methodFormParsDefined = 0;
    }
    
    public void visit(FormalParameter formalParameter) {
    	// proveriti da li je dati formalni parametar vec deklarisan
    	Obj formalParameterNode = Tab.currentScope().findSymbol(formalParameter.getFormalParamName());
    	if (formalParameterNode != null) {
    		report_error("Semanticka greska na liniji " + formalParameter.getLine() + " : Formalni parametar " + formalParameter.getFormalParamName() + " je vec prethodno deklarisan/definisan!", null);
    		return;
    	}
    	methodFormParsDefined++;
    	
    	// ubaciti formalni parametar u tabelu simbola
    	Struct formalParameterType = null;
    	if (variableArrayDeclared == false) {
    		// obicna promenljiva
    		formalParameterType = temporaryType;
    	}else {
    		// niz
    		formalParameterType = new Struct(Struct.Array, temporaryType);
    	}
    	Tab.insert(Obj.Var, formalParameter.getFormalParamName(), formalParameterType);
    	report_info("Formalni parametar " + formalParameter.getFormalParamName() + " je definisan u metodi " + currentMethod.getName(), null);
    	
    	// dodati u hash mapu za sve definisane metode u programu
    	// da li smo naleteli na tekucu metodu
    	if (currentMethod != null) {
    		Struct formalParamType = null;
        	if (variableArrayDeclared == false) {
        		// obicna promenljiva
        		formalParamType = temporaryType;
        	}else {
        		// niz
        		formalParamType = new Struct(Struct.Array, temporaryType);
        		variableArrayDeclared = false;
        	}
        	
        	int functionIndex = findMethodIndexByName(allFunctions, currentMethod.getName());
        	if (functionIndex != -1) {
        		
        		allFunctions.get(functionIndex).formalParameters.add(formalParamType);
        	}
        	
    	}
    	
    }
    
    // smene Designator
    // smena Designator = ident
    public void visit(DesignatorOne designatorOne) {
    	Obj obj = Tab.find(designatorOne.getDesName());
    	if (obj == Tab.noObj) {
    		report_error("Semanticka greska na liniji: " + designatorOne.getLine() + " : ime " + designatorOne.getDesName() + " nije deklarisano.", null);
    	}
    	designatorOne.obj = obj;
    	
    	// detektovati lokalne i globalne promenljive, simbolicke konstante
    	// globalna promenljiva
    	if (designatorOne.obj.getKind() == Obj.Var && designatorOne.obj.getLevel() == 0) {
    		report_info("Detektovana GLOBALNA promenljiva " + designatorOne.getDesName() 
    			+ " na liniji " + designatorOne.getLine(), null);
    	}
    	// lokalna promenljiva
    	if (designatorOne.obj.getKind() == Obj.Var && designatorOne.obj.getLevel() == 1) {
    		report_info("Detektovana LOKALNA promenljiva " + designatorOne.getDesName() 
    			+ " na liniji " + designatorOne.getLine(), null);
    	}
    	if (designatorOne.obj.getKind() == Obj.Con) {
    		report_info("Detektovana SIMBOLICKA KONSTANTA " + designatorOne.getDesName() 
    			+ " na liniji " + designatorOne.getLine(), null);
    	}
    }
    
//	static Map<String, Integer> mapElementsCounter = new HashMap<>();
    
    // smena Designator = Designator "[" Expr "]"
    // element niza
    public void visit(DesignatorMoreExpr designatorMoreExpr) {    	
    	String desName = designatorMoreExpr.getDesignatorArrayName().getDesignator().obj.getName();
    	Obj obj = Tab.find(desName);
    	if (obj == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + designatorMoreExpr.getLine() + " : ime " + desName + " nije deklarisano.", null);
    		return;
    	}
    	
    	if (designatorMoreExpr.getDesignatorArrayName().getDesignator().obj.getType().getKind() != Struct.Array) {
    		report_error("Semanticka greska na liniji " + designatorMoreExpr.getLine() 
    			+ " : Neterminal Designator: " + desName + " nije tipa NIZ!", null);
    		designatorMoreExpr.obj = Tab.noObj;
    		return;
    	}
    	if (designatorMoreExpr.getExpr().struct != Tab.intType) {
    		report_error("Semanticka greska na liniji " + designatorMoreExpr.getLine() 
				+ " : Izraz(neterminal Expr) u okviru zagrada [] " + desName + " nije tipa INT!", null);
    		designatorMoreExpr.obj = Tab.noObj;
    		return;
    	}
    	
//    	if (designatorMoreExpr.getDolarPart() instanceof DolarNonEmpty) {
//    		designatorMoreExpr.obj = new Obj(Obj.Var, desName + "$", Tab.intType);
//    		return;
//    	}
//    	
//    	int index = Integer.MIN_VALUE;
//    	if (designatorMoreExpr.getExpr() instanceof ExpressionTerm) {
//    		ExpressionTerm et = (ExpressionTerm) designatorMoreExpr.getExpr();
//    		if (et.getTerm() instanceof TermFactor) {
//    			TermFactor tf = (TermFactor) et.getTerm();
//    			if (tf.getFactor() instanceof FactorNumber) {
//    				FactorNumber fn = (FactorNumber) tf.getFactor();
//    				System.out.println("INDEX: " + fn.getN());
//    				index = fn.getN();
//    			}
//    		}
//    	}
//    	
//    	System.out.println("MAPA BROJA PRISTUPA ELEMENTIMA PRE:");
//		System.out.println("=======================");
//		for(Map.Entry<String, Integer> entry : mapElementsCounter.entrySet()) {
//			System.out.println(entry.getKey() + " " + entry.getValue());
//		}
//		System.out.println("=======================");
//    	
//    	String designatorElementName = desName + " " + index;
//    	
//		if (mapElementsCounter.containsKey(designatorElementName)) {
//			mapElementsCounter.put(designatorElementName, mapElementsCounter.get(designatorElementName) + 1);
//        } else {
//        	mapElementsCounter.put(designatorElementName, 1);
//        }
//    	
//		System.out.println("MAPA BROJA PRISTUPA ELEMENTIMA POSLE:");
//		System.out.println("=======================");
//		for(Map.Entry<String, Integer> entry : mapElementsCounter.entrySet()) {
//			System.out.println(entry.getKey() + " " + entry.getValue());
//		}
//		System.out.println("=======================");
		
    	// element niza
    	Struct elemType = designatorMoreExpr.getDesignatorArrayName().getDesignator().obj.getType().getElemType();
    	designatorMoreExpr.obj = new Obj(Obj.Elem, desName, elemType);
    	
    	System.out.println("TIP ELEMENTA: " + getTypeByKind(designatorMoreExpr.getDesignatorArrayName().getDesignator().obj.getType().getElemType().getKind()));
    	System.out.println("TIP NIZA: " + getTypeByKind(designatorMoreExpr.getDesignatorArrayName().getDesignator().obj.getType().getKind()));
    	report_info("Pristupa se elementu niza " + desName + " na liniji " + designatorMoreExpr.getLine(), null);
    }
    
    public boolean checkNumberOfActualAndFormalFunctionParameters(String methodName, SyntaxNode node) {
    	System.out.println("USAO U PROVERU BROJA PARAMETARA!");
    	FunctionNode method = findMethodByName(allFunctions, methodName);
    	boolean flag = false;
    	if (method != null) {
    		int numOfFormalParams = method.getValue().size();
    		if (this.functionActualParameters.size() == 0) {
    			return true;
    		}
    		List<Struct> currentFunctionActualParams = this.functionActualParameters.get(this.functionActualParameters.size() - 1);
    		int numOfActualParams = currentFunctionActualParams.size();
    		System.out.println("BROJ FORMALNIH PARAMETARA: " + numOfFormalParams + " za funkciju " + methodName);
    		System.out.println("BROJ STVARNIH PARAMETARA: " + numOfActualParams + " za funkciju " + methodName);
    		
    		System.out.println("===================================");
    		System.out.println("FORMALNI PARAMETRI:");
    		for (int i = 0; i < numOfFormalParams; i++) {
    			String formalParamType = getTypeByKind(method.getValue().get(i).getKind());
    			System.out.println(i + " " + formalParamType);
			}
    		System.out.println("===================================");
    		
    		System.out.println("===================================");
    		System.out.println("STVARNI PARAMETRI:");
    		for (int i = 0; i < numOfActualParams; i++) {
    			String actualParamType = getTypeByKind(currentFunctionActualParams.get(i).getKind());
    			System.out.println(i + " " + actualParamType);
			}
    		System.out.println("===================================");
    	
    		if (numOfActualParams != numOfFormalParams) {
    			report_error("Semanticka greska na liniji " + node.getLine() + " : Broj stvarnih argumenata NIJE jednak broju formalnih argumenata za funkciju " + methodName, null);
    		}else {
    			flag = true;
    			report_info("Broj formalnih i stvarnih argumenata je jednak za funkciju " + methodName, null);
    		} 
    	}
    	System.out.println("IZASAO U PROVERU BROJA PARAMETARA!");
    	return flag;
    }
    
    public String getTypeByKind(int kind) {
    	switch(kind) {
    	case 0:
    		return "None";
    	case 1:
    		return "Int";
    	case 2:
    		return "Char";
    	case 3:
    		return "Array";
    	case 4:
    		return "Class";
    	case 5:
    		return "Bool";	
    	default:
    		return null;
    	}
    }
    
    public boolean checkTypeOfActualAndFormalFunctionParameters(String methodName, SyntaxNode node) {
    	System.out.println("USAO U PROVERU TIPOVA PARAMETARA!");
    	boolean flag = true;
    	FunctionNode method = findMethodByName(allFunctions, methodName);
    	if (method != null) {
    		int index = 0;
    		if (this.functionActualParameters.size() == 0) {
    			return true;
    		}	
    		List<Struct> currentMethodActualParameters = this.functionActualParameters.remove(this.functionActualParameters.size() - 1);
    		while (index < currentMethodActualParameters.size()) {
    			Struct formalParameter = method.getValue().get(index);
    			Struct actualParameter = currentMethodActualParameters.get(index);
    			if (getTypeByKind(formalParameter.getKind()).equals(getTypeByKind(actualParameter.getKind())) == false) {
    				flag = false;
    				report_error("Semanticka greska na liniji " + node.getLine() 
    					+ " : Na poziciji " + index + ", Tip formalnog argumenta " + getTypeByKind(formalParameter.getKind()) 
						+ " se razlikuje od tipa stvarnog argumenta " + getTypeByKind(actualParameter.getKind())
    					+ " za funkciju " + methodName, null);
    			}
    			index++;
    		}
    	}else {
    		flag = false;
    	}
    	System.out.println("IZASAO U PROVERU TIPOVA PARAMETARA!");
    	return flag;
    }
    
    public boolean isStandardMethod(String methodName) {
    	return (methodName.equals("ord") 
			|| methodName.equals("chr")
			|| methodName.equals("len"));
    }
    
    public boolean handleStandardMethods(String methodName, SyntaxNode node) {
    	boolean flag = true;
    	if (methodName.equals("ord")) {
    		// ord(char ch);
    		FunctionNode fn = new FunctionNode(methodName);
    		fn.formalParameters.add(Tab.charType);
    		allFunctions.add(fn);
    		
    		flag = checkNumberOfActualAndFormalFunctionParameters(methodName, node);
    		if (flag == false) {
    			return flag;
    		}else {
    			flag = checkTypeOfActualAndFormalFunctionParameters(methodName, node);
    			if (flag == true) {
    				report_info("STANDARDNA METODA " + methodName 
    					+ " ima sve formalne i stvarne parametre istog tipa", node);
    				return true;
    			}else {
    				return false;
    			}
    		}
    		
    	}else if (methodName.equals("chr")) {
    		// chr(int num);
    		FunctionNode fn = new FunctionNode(methodName);
    		fn.formalParameters.add(Tab.intType);
    		allFunctions.add(fn);
    		
    		flag = checkNumberOfActualAndFormalFunctionParameters(methodName, node);
    		if (flag == false) {
    			return flag;
    		}else {
    			flag = checkTypeOfActualAndFormalFunctionParameters(methodName, node);
    			if (flag == true) {
    				report_info("STANDARDNA METODA " + methodName 
    					+ " ima sve formalne i stvarne parametre istog tipa", node);
    				return true;
    			}else {
    				return false;
    			}
    		}
    	}else if (methodName.equals("len")) {    		
    		FunctionNode fn = new FunctionNode(methodName);
    		fn.formalParameters.add(new Struct(Struct.Array));
    		allFunctions.add(fn);
    		
    		flag = checkNumberOfActualAndFormalFunctionParameters(methodName, node);
    		if (flag == false) {
    			return flag;
    		}else {
    			flag = checkTypeOfActualAndFormalFunctionParameters(methodName, node);
    			if (flag == true) {
    				report_info("STANDARDNA METODA " + methodName 
    					+ " ima sve formalne i stvarne parametre istog tipa", node);
    				return true;
    			}else {
    				return false;
    			}
    		}
    	}
    	return flag;
    }
    
    // Designator kao funkcija sa stvarnim argumentima
    public void visit(ActParsDesignator actParsDesignator) {    	
    	Obj obj = Tab.find(actParsDesignator.getFactorFuncCallBeginning().getDesignator().obj.getName());
    	if (obj == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + actParsDesignator.getLine() + " : ime " 
				+ actParsDesignator.getFactorFuncCallBeginning().getDesignator().obj.getName() + " nije deklarisano!", null);
    		return;
    	}
    	Obj designatorFunctionNode = actParsDesignator.getFactorFuncCallBeginning().getDesignator().obj;
    	if (designatorFunctionNode.getKind() != Obj.Meth) {
    		report_error("Semanticka greska na liniji " + actParsDesignator.getLine() 
    			+ " : ime " + designatorFunctionNode.getName() + " nije deklarisano kao FUNKCIJA!", null);
    		return;
    	}
    	
    	//ISPRAVITI
    	
//    	actParsDesignator.struct = designatorFunctionNode.getType();
    	
    	actParsDesignator.obj = designatorFunctionNode;
    	
    	if (isStandardMethod(designatorFunctionNode.getName()) == true) {
    		handleStandardMethods(designatorFunctionNode.getName(), actParsDesignator);
    	}else {
    		// ispitati koliko data metoda ima formalnih/stvarnih parametara, tj da li su jednaki
        	boolean flag = checkNumberOfActualAndFormalFunctionParameters(designatorFunctionNode.getName(), actParsDesignator);
        	if (flag == false) {
        		return;
        	}
        	
        	// ispitati da li se slaze tip za svaki od formalnih i stvarnih argumenata respektivno
        	flag = checkTypeOfActualAndFormalFunctionParameters(designatorFunctionNode.getName(), actParsDesignator);
        	if (flag == true) {            	
        		report_info("Svi formalni i stvarni argumenti su istog tipa za funkciju " + designatorFunctionNode.getName(), null);
        	}
    	}

    }
    
    // smena ActPars
    public void visit(ActualParam actualParam) {
    	Struct actParExpression = actualParam.getExpr().struct;
    	if (this.functionActualParameters.isEmpty()) {
    		List<Struct> newActualParameterList = new ArrayList<>();
    		newActualParameterList.add(actParExpression);
    		this.functionActualParameters.add(newActualParameterList);
    	}else {
    		this.functionActualParameters.get(this.functionActualParameters.size() - 1).add(actParExpression);
    	}
    }
    
    public void visit(ActualParams actualParams) {
    	Struct actParExpression = actualParams.getExpr().struct;
    	if (this.functionActualParameters.isEmpty()) {
    		List<Struct> newActualParameterList = new ArrayList<>();
    		newActualParameterList.add(actParExpression);
    		this.functionActualParameters.add(newActualParameterList);
    	}else {
    		this.functionActualParameters.get(this.functionActualParameters.size() - 1).add(actParExpression);
    	}
    }
    
    public void visit(AssignDesignator assignDesignator) {
    	assignDesignator.obj = Tab.noObj;
    	
    	if (assignDesignator.getDesignator().obj == null) {
    		report_error("Semanticka greska na liniji " + assignDesignator.getLine() 
    			+ " : U smeni dodele vrednosti, Designator ima vrednost NULL!", null);
    		return;
    	}

    	// designator mora oznacavati promenljivu/element niza/polje unutar klase    	
    	if ((assignDesignator.getDesignator().obj != null && assignDesignator.getDesignator().obj.getKind() != Obj.Var) &&
    			(assignDesignator.getDesignator().obj.getKind() != Obj.Elem) &&
    			(assignDesignator.getDesignator().obj.getKind() != Obj.Fld)) {
    		report_error("Semanticka greska na liniji " + assignDesignator.getLine() + " : Designator " 
    			+ assignDesignator.getDesignator().obj.getName() + " mora oznacavati promenljivu/element niza/polje unutar klase!", null);
    		return;
    	}
    	
    	// tip neterminala Expr mora biti kompatibilan pri dodeli sa tipom Designator-a
    	// smena Designator:d Assignop:a Expr:e SEMI
    	Struct designatorType = assignDesignator.getDesignator().obj.getType();
    	Struct exprType = assignDesignator.getExpr().struct;
    	if (designatorType != null && exprType != null && designatorType.getKind() != Struct.Array) {
    		System.out.println("======================");
    		System.out.println("DESIGNATOR TIP(Leva strana): " + getTypeByKind(designatorType.getKind()));
    		System.out.println("EXPR(Desna strana): " + getTypeByKind(exprType.getKind()));
    		System.out.println("======================");
    		if (!designatorType.equals(exprType)) {
    			report_error("Semanticka greska na liniji " + assignDesignator.getLine() + 
    					" : Tip neterminala expr(desna strana) nije kompatibilan pri dodeli sa tipom designatora(leva strana)!", null);
    		}

    	}else {
    		// niz 
    		// proveriti da li je desna strana izraza dodele == Struct.Array
    		if (exprType != null && exprType.getKind() != Struct.Array) {
    			report_error("Semanticka greska na liniji " + assignDesignator.getLine() + 
    					" : Tip neterminala expr(desna strana) nije kompatibilan pri dodeli sa tipom NIZA = designator(leva strana)!", null);
    		}
    		if (designatorType != null && exprType != null && designatorType.getElemType() != exprType.getElemType()) {
    			report_error("Semanticka greska na liniji " + assignDesignator.getLine() + 
    					" : Neterminal expr(desna strana) ima elemente niza razlicitog tipa u odnosu na neterminal designator(leva strana)!", null);
    		}
    	}
    	
    	// proveriti da li se iterator u foreach petlji koristi za CITANJE, ne sme za UPIS
    	if (foreachLoopDetected 
			&& this.foreachLoopsIterators.get(currentForeachDesignator).contains(assignDesignator.getDesignator().obj.getName())) {
    		report_error("Semanticka greska na liniji " + assignDesignator.getLine() 
    			+ " : Iterator " + assignDesignator.getDesignator().obj.getName() 
    			+ " NE sme da se koristi za DODELU VREDNOSTI tokom iteriranja kroz niz " + currentForeachDesignator, null);
    		return;
    	}
    }
    
    public void visit(IncrementDesignator incrementDesignator) {

    	
    	// Designator mora oznacavati promenljivu/element niza/polje objekta unutrasnje klase
    	Obj obj = Tab.find(incrementDesignator.getDesignator().obj.getName());
    	if (obj == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + incrementDesignator.getLine() 
    			+ " : ime " + incrementDesignator.getDesignator().obj.getName() + " nije prethodno deklarisano/definisano!", null);
    		return;
    	}
    	if (incrementDesignator.getDesignator().obj.getKind() != Obj.Var 
    			&& incrementDesignator.getDesignator().obj.getKind() != Obj.Elem
    			&& incrementDesignator.getDesignator().obj.getKind() != Obj.Fld) {
    		report_error("Semanticka greska na liniji " + incrementDesignator.getLine() 
				+ " : ime " + incrementDesignator.getDesignator().obj.getName() + " ne oznacava promenljivu/element niza/polje unutrasnje klase!", null);	
    		return;
    	}
    	if (incrementDesignator.getDesignator().obj.getType() != Tab.intType) {
    		report_error("Semanticka greska na liniji " + incrementDesignator.getLine() 
				+ " : U izrazu DesignatorStatement = Designator ++, designator " + incrementDesignator.getDesignator().obj.getName() + " nije tipa INT!", null);	
    		return;
    	}
    	
    	// proveriti da li se iterator u foreach petlji koristi za CITANJE, ne sme za UPIS
    	if (foreachLoopDetected 
			&& this.foreachLoopsIterators.get(currentForeachDesignator).contains(incrementDesignator.getDesignator().obj.getName())) {
    		report_error("Semanticka greska na liniji " + incrementDesignator.getLine() 
    			+ " : Iterator " + incrementDesignator.getDesignator().obj.getName() 
    			+ " NE sme da INKREMENTIRA vrednost tokom iteriranja kroz niz " + currentForeachDesignator, null);
    		return;
    	}
    	
    	// ISPRAVITI
    	
//    	incrementDesignator.struct = obj.getType();
    	
    	incrementDesignator.obj = obj;
    }
    
    public void visit(DecrementDesignator decrementDesignator) {

    	Obj obj = Tab.find(decrementDesignator.getDesignator().obj.getName());
    	if (obj == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + decrementDesignator.getLine() 
    			+ " : ime " + decrementDesignator.getDesignator().obj.getName() + " nije prethodno deklarisano/definisano!", null);
    		return;
    	}
    	if (decrementDesignator.getDesignator().obj.getKind() != Obj.Var 
    			&& decrementDesignator.getDesignator().obj.getKind() != Obj.Elem
    			&& decrementDesignator.getDesignator().obj.getKind() != Obj.Fld) {
    		report_error("Semanticka greska na liniji " + decrementDesignator.getLine() 
				+ " : ime " + decrementDesignator.getDesignator().obj.getName() + " ne oznacava promenljivu/element niza/polje unutrasnje klase!", null);	
    		return;
    	}
    	if (decrementDesignator.getDesignator().obj.getType() != Tab.intType) {
    		report_error("Semanticka greska na liniji " + decrementDesignator.getLine() 
				+ " : U izrazu DesignatorStatement = Designator --, designator " + decrementDesignator.getDesignator().obj.getName() + " nije tipa INT!", null);	
    		return;
    	}
    	
    	
    	// proveriti da li se iterator u foreach petlji koristi za CITANJE, ne sme za UPIS
    	if (foreachLoopDetected 
			&& this.foreachLoopsIterators.get(currentForeachDesignator).contains(decrementDesignator.getDesignator().obj.getName())) {
    		report_error("Semanticka greska na liniji " + decrementDesignator.getLine() 
    			+ " : Iterator " + decrementDesignator.getDesignator().obj.getName() 
    			+ " NE sme da DEKREMENTIRA vrednost tokom iteriranja kroz niz " + currentForeachDesignator, null);
    		return;
    	}
    	
    	// ISPRAVITI
    	
    	decrementDesignator.obj = obj;
    	
//    	decrementDesignator.struct = obj.getType();
    }
    
    public void visit(FactorFuncCallBeginning factorFuncCallBeginning) {
    	this.functionActualParameters.add(new ArrayList<>());
    }
       
    public void visit(FactorFuncCallActPars factorFuncCallActPars) {    	
    	Obj func = factorFuncCallActPars.getFactorFuncCallBeginning().getDesignator().obj;
    	if (Obj.Meth != func.getKind()) {
    		report_error("Semanticka greska na liniji: " + factorFuncCallActPars.getLine() + " , ime " + func.getName() + " nije deklarisano kao FUNKCIJA!", null);
    		factorFuncCallActPars.struct = Tab.noType;
    		return;
    	}
    	
    	if (Tab.noType == func.getType()) {
    		report_error("Semanticka greska na liniji " + factorFuncCallActPars.getLine() 
    			+ " : Funkcija " + func.getName() 
    			+ " ne moze se koristiti u izrazima jer nema povratnu vrednost!", null);
    		factorFuncCallActPars.struct = Tab.noType;
    		return;
    	}
    	
    	report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + factorFuncCallActPars.getLine(), null);
		factorFuncCallActPars.struct = func.getType();
    	
		if (isStandardMethod(func.getName()) == true) {
			handleStandardMethods(func.getName(), factorFuncCallActPars);
		}else {
			// ispitati koliko data metoda ima formalnih/stvarnih parametara, tj da li su jednaki
	    	boolean flag = checkNumberOfActualAndFormalFunctionParameters(func.getName(), factorFuncCallActPars);
	    	if (flag == false) {
	    		return;
	    	}
	    	
	    	// ispitati da li se slaze tip za svaki od formalnih i stvarnih argumenata respektivno
	    	flag = checkTypeOfActualAndFormalFunctionParameters(func.getName(), factorFuncCallActPars);
	    	if (flag == true) {
	    		report_info("Detektovan POZIV GLOBALNE FUNKCIJE " + func.getName() 
					+ " sa istim brojem stvarnih i formalnih parametara", factorFuncCallActPars);
	    	}
		}
		
    	// proveriti da li se iterator u foreach petlji koristi za CITANJE, ne sme za UPIS
    	if (foreachLoopDetected && this.foreachLoopsIterators.get(currentForeachDesignator).contains(
    			currentForeachIterator)) {
    		report_error("Semanticka greska na liniji " + factorFuncCallActPars.getLine() 
    			+ " : Iterator " + currentForeachIterator 
    			+ " NE sme da se koristi za dodelu POVRATNE VREDNOSTI FUNKCIJE tokom iteriranja kroz niz: " + currentForeachDesignator, null);
    		foreachLoopDetected = false;
    		return;
    	}
    }
    
    public void visit(ExpressionTerm expressionTerm) {
    	expressionTerm.struct = expressionTerm.getTerm().struct;
    }
    
    public void visit(ExpressionNegTerm expressionNegTerm) {
    	if (expressionNegTerm.getTerm().struct != Tab.intType) {
    		report_error("Semanticka greska na liniji " + expressionNegTerm.getLine() 
    			+ " : U smeni Expr = - Term, neterminal Term mora biti tipa INT!", null);
    	}
    	expressionNegTerm.struct = expressionNegTerm.getTerm().struct;
    }
  
    public void visit(ExprTermAddopList exprTermAddopList) {
    	Struct te = exprTermAddopList.getExpr().struct;
    	Struct t = exprTermAddopList.getTerm().struct;
    	// poredimo 2 tipa
    	if (te.equals(t) && te == Tab.intType) {
    		exprTermAddopList.struct = te;
    	}else {
    		report_error("Semanticka greska na liniji " + exprTermAddopList.getLine() + " : nekompatibilni tipovi u izrazu za sabiranje.", null);
    		exprTermAddopList.struct = Tab.noType;
    	} 
    }
    
    // smene ConfFact
    // smena CondFact = Expr Relop Expr
    public void visit(CondFactMore condFactMore) {
    	// tipovi oba izraza moraju biti kompatibilni
    	Struct exprFirst = condFactMore.getExpr().struct;
    	Struct exprSecond = condFactMore.getExpr1().struct;
    	System.out.println("EXPR 1: " + getTypeByKind(exprFirst.getKind()));
    	System.out.println("EXPR 2: " + getTypeByKind(exprSecond.getKind()));
    	if (!exprFirst.compatibleWith(exprSecond)) {
    		report_error("Semanticka greska na liniji " + condFactMore.getLine() 
    			+ " : U smeni CondFact = Expr:e1 Relop Expr:e2, tipovi oba izraza nisu kompatibilni!", null);
    		relationOperatorName = null;
    	}
    	System.out.println("============================");
    	System.out.println("RELACIONI OPERATOR: " + relationOperatorName);
    	System.out.println("LEFT EXPR: " + getTypeByKind(exprFirst.getKind()));
    	System.out.println("RIGHT EXPR: " + getTypeByKind(exprSecond.getKind()));
    	System.out.println("============================");
    	
    	// uz promenljive tipa klase/niza, mogu se samo koristiti != / == od relacionih operatora
    	if ((!(relationOperatorName.equals("!=") 
    			|| relationOperatorName.equals("=="))
    		) && (getTypeByKind(exprFirst.getKind()).equals("Class")
    			|| getTypeByKind(exprFirst.getKind()).equals("Array") 
    			|| getTypeByKind(exprSecond.getKind()).equals("Class")
    			|| getTypeByKind(exprSecond.getKind()).equals("Array"))
    		) {
    			report_error("Semanticka greska na liniji " + condFactMore.getLine() 
    				+ " : Uz promenljive tipa klase/niza mogu se samo koristiti !=/== od relacionih operatora!", null);
    			relationOperatorName = null;	
    		} 
    	relationOperatorName = null;
    }
    
    // smena CondFact = Expr
    public void visit(CondFactOne condFactOne) {
    	// proveriti da li je condFactOne tipa BOOL
    	if (condFactOne.getExpr().struct != ChildTab.boolType) {
    		report_error("Semanticka greska na liniji " + condFactOne.getLine() 
    			+ " : U smeni if(CondFact = Expr)/while(CondFact = Expr), izraz u okviru if naredbe mora biti tipa BOOL!", null);
    	}
    }
    
    // smene Statement
    // smena Statement = "if" "(" Condition ")" Statement ["else" Statement].
    public void visit(IfStmt ifStmt) {
    	
    }
    
    public void visit(IfElseStmt ifElseStmt) {
    	
    }
    
    boolean whileLoopDetected = false;
    
    //smena while
    public void visit(WhileStmtFirstPart whileStmtFirstPart) {
//    	whileLoopsCount++;
    	whileLoopDetected = true;
    }
    
    public void visit(WhileStmtLastPart whileStmtLastPart) {
    	whileLoopDetected = false;
    }
    
    // dodati da se prepoznaju break i continue kao Obj/Struct cvorovi
    //smena break
    public void visit(BreakStmt breakStmt) {
    	// proveriti da li se break naredba zove samo u okviru while/foreach petlji
//    	if (whileLoopsCount != 0) {
//    		// sme da se pozove break, izlazimo iz while petlje
//    		whileLoopsCount--;
//    	}else {
//    		// NE sme da se pozove break
//    		report_error("Semanticka greska: Iskaz break se moze koristiti samo unutar petlji!", null);
//    	}
    	if (!whileLoopDetected && !foreachLoopDetected) {
    		report_error("Semanticka greska: Iskaz break se moze koristiti samo unutar petlji!", null);
    	}
    }
    
    //smena continue
    public void visit(ContinueStmt continueStmt) {
    	// ne sme da se pozove continue iskaz van while/foreach naredbi
//    	if (whileLoopsCount == 0) {
//    		// ne sme da se pozove CONTINUE
//    		report_error("Semanticka greska: Iskaz continue se moze koristiti samo unutar while ili foreach petlje!", null);
//    	}
    	if (!whileLoopDetected && !foreachLoopDetected) {
    		report_error("Semanticka greska: Iskaz continue se moze koristiti samo unutar petlji!", null);
    	}
    }
        
    public void visit(ForeachPartFirst foreachPartFirst) {
    	foreachLoopDetected = true;
    	
    	Obj obj = Tab.find(foreachPartFirst.getIterator());
    	if (obj == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + foreachPartFirst.getLine()
				+ " : U smeni foreach, iterator " + foreachPartFirst.getIterator() + " nije prethodno deklarisano/definisano!", null);
    		foreachPartFirst.obj = Tab.noObj;
    		currentForeachDesignator = null;
    		currentForeachIterator = null;
    		return;
    	}else {
    		foreachPartFirst.obj = obj;		
    		currentForeachDesignator = foreachPartFirst.getDesignator().obj.getName();
    		currentForeachIterator = foreachPartFirst.getIterator();
    		if (!this.foreachLoopsIterators.containsKey(currentForeachDesignator)) {
    			List<String> foreachIteratorList = new ArrayList<String>();
    			foreachIteratorList.add(currentForeachIterator);
    			this.foreachLoopsIterators.put(currentForeachDesignator, foreachIteratorList);
    		}else {
    			if (this.foreachLoopsIterators.get(currentForeachDesignator).size() != 0) {
    				// dodavanje na kraj
    				this.foreachLoopsIterators.get(currentForeachDesignator).add(
    						this.foreachLoopsIterators.get(currentForeachDesignator).size() - 1, currentForeachIterator);
    			}else {
    				// dodavanje na pocetak
    				this.foreachLoopsIterators.get(currentForeachDesignator).add(0, currentForeachIterator);
    			}
    		}
    	}
    }
    
    //smenta Statement := Designator . foreach ( ident => Statement ) ;
    public void visit(ForeachStmt foreachStmt) {
    	foreachLoopDetected = false;
    	
    	Obj foreachDesignatorObj = foreachStmt.getForeachPartFirst().getDesignator().obj;
    	
    	// Designator mora oznacavati niz proizvoljnog tipa
    	if (foreachDesignatorObj.getType().getKind() != Struct.Array) {
    		report_error("Semanticka greska na liniji " + foreachStmt.getLine() 
    			+ " : U smeni foreach, Designator " + foreachDesignatorObj.getName()
    			+ " mora oznacavati NIZ!", null);
    		return;
    	}
    	
    	// ident mora biti lokalna/globalna promenljiva istog tipa kao i elementi niza koji opisuje Designator
    	String iteratorName = foreachStmt.getForeachPartFirst().getIterator();
    	Obj foreachIterator = Tab.find(iteratorName);
    	if (foreachIterator == null) {
    		report_error("Semanticka greska na liniji " + foreachStmt.getLine()
    			+ " : U smeni foreach, iterator " + iteratorName + " nije prethodno deklarisano/definisano!", null);
    		return;
    	}
		
    	// iterator mora biti lokalna/globalna promenljiva
		if (Obj.Var != foreachIterator.getKind()) {
			report_error("Semanticka greska na liniji " + foreachStmt.getLine()
				+ " : U smeni foreach, iterator " + iteratorName + " nije deklarisan/definisan kao lokalna/globalna promenljiva(VAR)!", null);
			return;
		}
		
		Struct foreachDesignatorElementsType = foreachDesignatorObj.getType().getElemType();
    	if (foreachDesignatorElementsType.equals(foreachIterator.getType()) == false) {
    		report_error("Semanticka greska na liniji " + foreachStmt.getLine()
				+ " : U smeni foreach, iterator " + iteratorName + " tipa " 
				+ getTypeByKind(foreachIterator.getType().getKind()) 
				+ " nije istog tipa " 
				+ getTypeByKind(foreachDesignatorElementsType.getKind())
				+ " kao i elementi niza koji opisuje Designator " + foreachDesignatorObj.getName(), null);
    		return;
    	}
    	
    	report_info("Obradjuje se FOREACH iskaz", foreachStmt);
    }
    
    //smena Statement := Designator "=" Designator "." "findAny" "(" Expr ")" ";"
    public void visit(FindAnyPartFirst findAnyPartFirst) {
    	// Designator sa leve strane jednakosti mora oznacavati promenljivu, tipa BOOL
    	Obj leftDesignator = findAnyPartFirst.getDesignator().obj;
    	Obj rightDesignator = findAnyPartFirst.getDesignator1().obj;
    	
    	if (Tab.find(leftDesignator.getName()) == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + findAnyPartFirst.getLine() 
    			+ " : U smeni findAny, LEVI designator " + leftDesignator.getName() 
    			+ " nije deklarisan/definisan!", null);
    		findAnyPartFirst.obj = Tab.noObj;
    		return;
    	}
    	
    	if (Tab.find(rightDesignator.getName()) == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + findAnyPartFirst.getLine() 
    			+ " : U smeni findAny, DESNI designator " + rightDesignator.getName() 
    			+ " nije deklarisan/definisan!", null);
    		findAnyPartFirst.obj = Tab.noObj;
    		return;
    	}
    	
    	findAnyPartFirst.obj = findAnyPartFirst.getDesignator().obj;
    }
    
    public void visit(FindAnyStmt findAnyStmt) {
    	// Designator sa leve strane jednakosti mora oznacavati promenljivu, tipa BOOL
    	Obj leftDesignator = findAnyStmt.getFindAnyPartFirst().getDesignator().obj;
    	Obj rightDesignator = findAnyStmt.getFindAnyPartFirst().getDesignator1().obj;
    	
    	if (leftDesignator.getKind() != Obj.Var) {
    		report_error("Semanticka greska na liniji " + findAnyStmt.getLine() 
    			+ " : U smeni findAny, LEVI designator " + leftDesignator.getName() 
    			+ " ne oznacava promenljivu(Var)!", null);
    		return;
    	}
    	
    	if (leftDesignator.getType() != ChildTab.boolType) {
    		report_error("Semanticka greska na liniji " + findAnyStmt.getLine() 
    			+ " : U smeni findAny, LEVI designator " + leftDesignator.getName() 
    			+ " nije tipa BOOL!", null);
    		return;
    	}
    	
    	// Designator sa desne strane mora oznacavati jednodimenzionalni niz ugradjenog tipa
    	if (rightDesignator.getType().getKind() != Struct.Array) {
    		report_error("Semanticka greska na liniji " + findAnyStmt.getLine() 
				+ " : U smeni findAny, DESNI designator " + rightDesignator.getName() 
				+ " ne oznacava NIZ!", null);
    		return;
    	}
    	
    	if (rightDesignator.getType().getElemType() != Tab.intType 
    			&& rightDesignator.getType().getElemType() != Tab.charType 
    			&& rightDesignator.getType().getElemType() != ChildTab.boolType) {
    		report_error("Semanticka greska na liniji " + findAnyStmt.getLine() 
				+ " : U smeni findAny, DESNI designator " + rightDesignator.getName() 
				+ " ne oznacava niz ugradjenog tipa!", null);
    		return;
    	}
    	
    }
    
    // smena Relop
    public void visit(EqualOperand equalOperand) {
    	relationOperatorName = String.valueOf("==");
    }
    
    public void visit(NotEqualOperand notEqualOperand) {
    	relationOperatorName = String.valueOf("!=");
    }
    
    public void visit(GreaterOperand greaterOperand) {
    	relationOperatorName = String.valueOf(">");
    }
    
    public void visit(GreaterOrEqualOperand greaterOrEqualOperand) {
    	relationOperatorName = String.valueOf(">=");
    }
    
    public void visit(LessOperand lessOperand) {
    	relationOperatorName = String.valueOf("<");
    }
    
    public void visit(LessOrEqualOperand lessOrEqualOperand) {
    	relationOperatorName = String.valueOf("<=");
    }
    
    // smene Factor
    public void visit(FactorNumber factorNumber) {
    	factorNumber.struct = Tab.intType;
    }
    
    public void visit(FactorVar factorVar) {
    	String factorVarName = factorVar.getDesignator().obj.getName();
    	Obj obj = Tab.find(factorVarName);
    	if (obj == null) {
    		report_error("Semanticka greska na liniji " + factorVar.getLine() 
    			+ " : ime " + factorVarName + " nije deklarisano/definisano!", null);
    	}
    	factorVar.struct = factorVar.getDesignator().obj.getType();
    }
    
//    // MODIFIKACIJA Factor ::= TARABA Designator:d
//    public void visit(FactorBinaryNumber factorBinaryNumber) {
//    	String factorVarName = factorBinaryNumber.getDesignator().obj.getName();
//    	Obj obj = Tab.find(factorVarName);
//    	if (obj == null) {
//    		report_error("Semanticka greska na liniji " + factorBinaryNumber.getLine() 
//    			+ " : ime " + factorVarName + " nije deklarisano/definisano!", null);
//    	}
//    	factorBinaryNumber.struct = Tab.intType;
//    }
    
//    public void visit(FactorMaxArray factorMaxArray) {
//    	String factorVarName = factorMaxArray.getDesignator().obj.getName();
//    	Obj obj = Tab.find(factorVarName);
//    	if (obj == null) {
//    		report_error("Semanticka greska na liniji " + factorMaxArray.getLine() 
//    			+ " : ime " + factorVarName + " nije deklarisano/definisano!", null);
//    	}
//    	// povratna vrednost ce biti int
//    	factorMaxArray.struct = Tab.intType;
//    }
    
//    public void visit(FactorFuncCallNoActPars factorFuncCallNoActPars) {
//    	Obj func = factorFuncCallNoActPars.getDesignator().obj;
//    	if (Obj.Meth == func.getKind()) {
//    		report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + factorFuncCallNoActPars.getLine(), null);
//    		factorFuncCallNoActPars.struct = func.getType();
//    	}else {
//    		report_error("Semanticka greska na liniji: " + factorFuncCallNoActPars.getLine() + " , ime " + func.getName() + " nije deklarisano!", null);
//    		factorFuncCallNoActPars.struct = Tab.noType;
//    	}
//    }
    	
    public void visit(FactorChar factorChar) {
    	factorChar.struct = Tab.charType;
    }
    
    public void visit(FactorBool factorBool) {
    	// Prosiriti klasu Tab, sa tipom BOOL
    	factorBool.struct = ChildTab.boolType;
    }
    
    public void visit(FactorExpr factorExpr) {
    	factorExpr.struct = factorExpr.getExpr().struct;
    }
    
    public void visit(FactorNewArrayAllocate factorNewArray) {
    	if (factorNewArray.getExpr().struct != Tab.intType) {
    		report_error("Semanticka greska na liniji: " + factorNewArray.getLine() + " : U smeni Factor := NEW Type [Expr], velicina niza nije tipa INT!", null);
    		factorNewArray.struct = Tab.noType;
    		return;
    	}
    	
    	System.out.println("============================");
    	System.out.println("CURRENT TYPE: " + getTypeByKind(temporaryType.getKind()));
    	System.out.println("TIP NIZA UZ OPERATOR NEW: " + getTypeByKind(factorNewArray.getType().struct.getKind()));
    	System.out.println("============================");
    	factorNewArray.struct = new Struct(Struct.Array, factorNewArray.getType().struct);
    }
    
    // poziv konstruktora 
    // smena Factor = "new" Type "(" [ActPars] ")"
//    public void visit(FactorNewWithActualParams factorNewWithActualParams) {
//    	if (factorNewWithActualParams.getType().struct.getKind() != Struct.Class) {
//    		report_error("Semanticka greska na liniji " + factorNewWithActualParams.getLine() 
//    			+ " : U smeni Factor = new Type ( [ActPars] ), neterminal Type " + getTypeByKind(factorNewWithActualParams.getType().struct.getKind()) + " ne oznacava tip unutrasnje klase", null);
//    		factorNewWithActualParams.struct = Tab.noType;
//    		return;
//    	}
//    	System.out.println("============================");
//    	System.out.println("TIP KLASE: " + factorNewWithActualParams.getType().getTypeName());
//    	System.out.println("============================");
//    	factorNewWithActualParams.struct = factorNewWithActualParams.getType().struct;
//    }
    
    // smene Term
    // smena Term = Term Mulop Factor.
    
    public void visit(TermFactorList termFactorList) {
    	// Term i Factor moraju biti tipa int
    	if (termFactorList.getTerm().struct != Tab.intType 
    			|| termFactorList.getFactor().struct != Tab.intType) {
    		report_error("Semanticka greska na liniji " + termFactorList.getLine() 
    			+ " : U smeni Term = Term Mulop Factor, Term " + getTypeByKind(termFactorList.getTerm().struct.getKind()) 
    			+ " i Factor " + getTypeByKind(termFactorList.getFactor().struct.getKind()) + " nisu tipa INT!", null);
    	}
    	termFactorList.struct = Tab.intType;
    }
    
    // Term = Factor
    // kada imamo funkciju sa povratnim tipom, da li je on jednak
    // tipu izraza koji se izracunava u okviru return naredbe
    public void visit(TermFactor termFactor) {
		termFactor.struct = termFactor.getFactor().struct;	
    }
    
    // kada metoda deklarise povratnu vrednost, tada ona mora da deklarise isti tip povratne vrednosti kroz return naredbu
    public void visit(ExpressionExist expressionExist) {
    	expressionExist.struct = expressionExist.getExpr().struct;
    }
    
    public void visit(NoExpression noExpression) {
    	noExpression.struct = Tab.noType;
    }
    
    // razdvojiti na return Expr; i return;
    public void visit(ReturnStmt returnStmt) {    	
    	returnFound = true;
    	Struct currentMethodType = currentMethod.getType();
    	ExprParam exprParam = returnStmt.getExprParam();
    	if (exprParam.struct != Tab.noType) {
    		ExpressionExist expressionExist = (ExpressionExist) exprParam;
    		System.out.println("CURRENT METHOD TYPE: " + getTypeByKind(currentMethodType.getKind()));
    		System.out.println("RETURN METHOD TYPE: " + getTypeByKind(expressionExist.getExpr().struct.getKind()));
    		if (!currentMethodType.compatibleWith(expressionExist.getExpr().struct)) {
    			report_error("Semanticka greska na liniji: " + returnStmt.getLine() + " : tip izraza u return naredbi se ne poklapa sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    		}
    	}else {
    		emptyReturnFound = true;
    	}
    }
    
    // smena Statement := read ( Designator ) ;
    public void visit(ReadStmt readStmt) {
    	Obj readDesignator = readStmt.getDesignator().obj;
    	
    	if (Tab.find(readDesignator.getName()) == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + readStmt.getLine() 
    			+ " : U smeni read, Designator " + readDesignator.getName() 
    			+ " nije deklarisan/definisan!", null);
    		return;
    	}

    	// Designator mora oznacavati promenljivu, element niza ili polje unutar objekta    	
    	if (readDesignator.getKind() != Obj.Var 
    			&& readDesignator.getKind() != Obj.Elem
    			&& readDesignator.getKind() != Obj.Fld) {
    		report_error("Semanticka greska na liniji " + readStmt.getLine() 
    			+ " : U smeni read, Designator " + readDesignator.getName() 
    			+ " ne oznacava promenljivu/element niza/polje unutar objekta!", null);
    		return;
    	}
    	
    	// Designator mora biti tipa int, char ili bool
    	if (readDesignator.getType() != Tab.intType 
    			&& readDesignator.getType() != Tab.charType
    			&& readDesignator.getType() != ChildTab.boolType) {
    		report_error("Semanticka greska na liniji " + readStmt.getLine() 
			+ " : U smeni read, Designator " + readDesignator.getName() 
			+ " nije tipa INT/CHAR/BOOL!", null);
    	}
    }    
    
    public void visit(FindAndReplaceMiddle findAndReplaceMiddle) {
    	// ident mora ident mora biti lokalna ili globalna promenljiva istog tipa kao i elementi niza koji opisuje Designator
    	Obj identObj = Tab.find(findAndReplaceMiddle.getIterator());
    	
    	if (identObj == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + findAndReplaceMiddle.getLine() 
				+ " : U smeni findAndReplace, IDENT nije deklarisan/definisan!", null);
    		findAndReplaceMiddle.obj = Tab.noObj;
			return;
    	}
    	
    	if (identObj.getKind() != Obj.Var) {
    		report_error("Semanticka greska na liniji " + findAndReplaceMiddle.getLine() 
				+ " : U smeni findAndReplace, IDENT " + identObj.getName() 
				+ " ne oznacava promenljivu(Var)!", null);
    		findAndReplaceMiddle.obj = Tab.noObj;
			return;
    	}
    	
    	findAndReplaceMiddle.obj = identObj;
    }
    
    public void visit(FindAndReplaceStmt findAndReplaceStmt) {
    	// levi i desni designator moraju da oznacavaju jednodimenzionalni niz ugradjenog tipa
    	
    	Obj leftDesignator = findAndReplaceStmt.getFindAndReplaceStart().getDesignator().obj;
    	Obj rightDesignator = findAndReplaceStmt.getFindAndReplaceStart().getDesignator1().obj;
    	
    	if (Tab.find(leftDesignator.getName()) == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
    			+ " : U smeni findAndReplace, LEVI designator " + leftDesignator.getName() 
    			+ " nije deklarisan/definisan!", null);
    		findAndReplaceStmt.obj = Tab.noObj;
    		return;
    	}
    	
    	if (Tab.find(rightDesignator.getName()) == Tab.noObj) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
    			+ " : U smeni findAndReplace, DESNI designator " + rightDesignator.getName() 
    			+ " nije deklarisan/definisan!", null);
    		findAndReplaceStmt.obj = Tab.noObj;
    		return;
    	}
    	
    	if (leftDesignator.getType().getKind() != Struct.Array) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
				+ " : U smeni findAndReplace, LEVI designator " + leftDesignator.getName() 
				+ " ne oznacava NIZ!", null);
    		findAndReplaceStmt.obj = Tab.noObj;
    		return;
    	}
    	
    	if (rightDesignator.getType().getKind() != Struct.Array) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
				+ " : U smeni findAndReplace, DESNI designator " + rightDesignator.getName() 
				+ " ne oznacava NIZ!", null);
    		findAndReplaceStmt.obj = Tab.noObj;
    		return;
    	}
    	
    	if (leftDesignator.getType().getElemType() != Tab.intType 
    			&& leftDesignator.getType().getElemType() != Tab.charType 
    			&& leftDesignator.getType().getElemType() != ChildTab.boolType) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
				+ " : U smeni findAndReplace, LEVI designator " + leftDesignator.getName() 
				+ " ne oznacava niz ugradjenog tipa!", null);
    		findAndReplaceStmt.obj = Tab.noObj;
    		return;
    	}
    	
    	if (rightDesignator.getType().getElemType() != Tab.intType 
    			&& rightDesignator.getType().getElemType() != Tab.charType 
    			&& rightDesignator.getType().getElemType() != ChildTab.boolType) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
				+ " : U smeni findAndReplace, DESNI designator " + rightDesignator.getName() 
				+ " ne oznacava niz ugradjenog tipa!", null);
    		findAndReplaceStmt.obj = Tab.noObj;
    		return;
    	}
    	
    	if (leftDesignator.getType().getElemType() != rightDesignator.getType().getElemType()) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
				+ " : U smeni findAndReplace, LEVI designator " + leftDesignator.getName() 
				+ " i DESNI designator " + rightDesignator.getName() +
				" nisu istog tipa!", null);
			findAndReplaceStmt.obj = Tab.noObj;
			return;
    	}
    	
//    	// ident mora ident mora biti lokalna ili globalna promenljiva istog tipa kao i elementi niza koji opisuje Designator
//    	Obj identObj = Tab.find(findAndReplaceStmt.getFindAndReplaceMiddle().getIterator());
//    	
//    	if (identObj == Tab.noObj) {
//    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
//				+ " : U smeni findAndReplace, IDENT nije deklarisan/definisan!", null);
//			findAndReplaceStmt.obj = Tab.noObj;
//			return;
//    	}
//    	
//    	if (identObj.getKind() != Obj.Var) {
//    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
//				+ " : U smeni findAndReplace, IDENT " + identObj.getName() 
//				+ " ne oznacava promenljivu(Var)!", null);
//			findAndReplaceStmt.obj = Tab.noObj;
//			return;
//    	}
//    	
//    	int leftDesignatorElemType = leftDesignator.getType().getElemType().getKind();
//    	
//    	if (identObj.getType().getKind() != leftDesignatorElemType) {
//    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
//				+ " : U smeni findAndReplace, IDENT " + identObj.getName() 
//				+ " nije istog tipa kao elementi niza " + leftDesignator.getName(), null);
//			findAndReplaceStmt.obj = Tab.noObj;
//			return;
//    	}
    	
    	Obj identObj = Tab.find(findAndReplaceStmt.getFindAndReplaceMiddle().getIterator());
    	int leftDesignatorElemType = leftDesignator.getType().getElemType().getKind();
    	
    	if (identObj.getType().getKind() != leftDesignatorElemType) {
    		report_error("Semanticka greska na liniji " + findAndReplaceStmt.getLine() 
				+ " : U smeni findAndReplace, IDENT " + identObj.getName() 
				+ " nije istog tipa kao elementi niza " + leftDesignator.getName(), null);
			findAndReplaceStmt.obj = Tab.noObj;
			return;
    	}
        	
    	findAndReplaceStmt.obj = findAndReplaceStmt.getFindAndReplaceStart().getDesignator().obj; // povratna vrednost
    }
    
//    public void visit(SwapElementsArrayDesignator swapElementsArrayDesignator) {
//    	swapElementsArrayDesignator.obj = Tab.noObj;
//    }
    
    public void visit(DesignatorArrayFilter filter) {
    	filter.obj = new Obj(Obj.Var, "filter", Tab.intType);
    }
    
}
