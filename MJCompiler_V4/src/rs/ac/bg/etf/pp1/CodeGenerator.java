package rs.ac.bg.etf.pp1;

import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	// DEBUG BUILD
	// <arg value="-debug"/>
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public String getObjTypeByKind(int kind) {
//		public static final int Con = 0, Var = 1, Type = 2, Meth = 3, Fld = 4, Elem=5, Prog = 6;
		String[] objTypes = {"Con", "Var", "Type", "Meth", "Fld", "Elem", "Prog"};
	    
	    if (kind >= 0 && kind < objTypes.length) {
	        return objTypes[kind];
	    } else {
	        return null;
	    }
	}
	
	public String getStructTypeByKind(int kind) {
//		public static final int None = 0;
//		public static final int Int = 1;
//		public static final int Char = 2;
//		public static final int Array = 3;
//		public static final int Class = 4;
//		public static final int Bool = 5;
//		public static final int Enum = 6;
//		public static final int Interface = 7;
		
		String[] objTypes = {"None", "Int", "Char", "Array", "Class", "Bool", "Enum", "Interface"};
		if (kind >= 0 && kind < objTypes.length) {
	        return objTypes[kind];
	    } else {
	        return null;
	    }
	}
	
	int statementCount = 0;
	
	// obrada print naredbe
	public void visit(PrintStmt printStmt) {
		// na steku se ocekuje vrednost, u delu gramatike za obradu Expr se izraz stavlja na stek
		// dodajemo SAMO sirinu ispisa
		
		if (printStmt.getExpr().struct != Tab.charType) {
			// print NON CHAR value
			Code.loadConst(5);
			Code.put(Code.print);
		}else {
			// print CHAR value
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	
	}
	
	public void visit(PrintNumberStmt printNumberStmt) {
		if (printNumberStmt.getExpr().struct != Tab.charType) {
			// print NON CHAR value
			Code.loadConst(printNumberStmt.getN1());
			Code.put(Code.print);
		}else {
			// print CHAR value
			Code.loadConst(printNumberStmt.getN1());
			Code.put(Code.bprint);
		}
	}
	
	// smena Statement := "read" "(" Designator ")" ";". 
	public void visit(ReadStmt readStmt) {
		// razlikujemo slucajeve ako je u pitanju char i non-char promenljiva(int/bool)
		int readStmtCode = Integer.MIN_VALUE;
		if (readStmt.getDesignator().obj.getType() == Tab.charType) {
			// char designator 
			readStmtCode = Code.bread;
		}else {
			// non-char designator
			readStmtCode = Code.read;
		}
		Code.put(readStmtCode);
		Code.store(readStmt.getDesignator().obj);
	}

	// smene Factor
	// smena Factor ::= NUMBER;
	public void visit(FactorNumber factorNumber) {
		Obj conObj = Tab.insert(Obj.Con, "$", factorNumber.struct);
		conObj.setLevel(0); // globalni doseg
		conObj.setAdr(factorNumber.getN()); // stavljamo vrednost u tabelu simbola u polje ADR kod CONST vrednosti
	
		Code.load(conObj);
	}
	
	// smena Factor ::= FactorChar;
	public void visit(FactorChar factorChar) {
		Obj conObj = Tab.insert(Obj.Con, "$", Tab.charType);
		conObj.setLevel(0);
		conObj.setAdr(factorChar.getCh());
		
		Code.load(conObj);
	}
	
	public void visit(FactorBool factorBool) {
		Obj conObj = Tab.insert(Obj.Con, "$", ChildTab.boolType);
		conObj.setLevel(0);
		if (factorBool.getB() == true) {
			conObj.setAdr(1);
		}else {
			conObj.setAdr(0);
		}
		
		Code.load(conObj);
	}
	
	// smena Factor ::= Designator ;
	public void visit(FactorVar factorVar) {
		Code.load(factorVar.getDesignator().obj);
	}
	
	public void visit(FactorNewArrayAllocate factorNewArray) {
		// 1. staviti kod za instrukciju newarray
		// 2. na expr stacku postoje 2 opcije za newarray instrukciju:
		// newarray 1 - ako je u pitanju non-char array
		// newarray 0 - ako je u pitanju char array
		
//		Code.loadConst(0); // arraylength, 0
//		Code.put(Code.dup2); // arraylength, 0, arraylength, 0
//		
//		Code.putFalseJump(Code.gt, 0);
//		int factorNewAdr = Code.pc - 2;
//		
//		// THEN
//		Code.put(Code.pop);
//		Code.put(Code.newarray);
//		int arrayType = Integer.MIN_VALUE;
//		if (factorNewArray.struct.getElemType() == Tab.charType) {
//			// CHAR array
//			arrayType = 0;
//		}else {
//			// NON-CHAR array 
//			arrayType = 1;
//		}
//		Code.put(arrayType);
//		
//		Code.loadConst(1);
//		Code.put(Code.dup);
//		Code.putFalseJump(Code.ne, 0);
//		int endAdr = Code.pc - 2;
//		
//		// ELSE
//		
//		Code.fixup(factorNewAdr);
//		Code.put(Code.trap);
//		Code.put(1);
//
//		Code.fixup(endAdr);		
		
		Code.put(Code.newarray);
		int arrayType = Integer.MIN_VALUE;
		if (factorNewArray.struct.getElemType() == Tab.charType) {
			// CHAR array
			arrayType = 0;
		}else {
			// NON-CHAR array 
			arrayType = 1;
		}
		Code.put(arrayType);
	}
	
	public void visit(DesignatorArrayName designatorArrayName) {
		// kada naidjemo na adresu niza, stavljamo je na expr stack
		Code.load(designatorArrayName.getDesignator().obj);
	}
	
	// POZIV funkcije SA ARGUMENTIMA
	public void visit(FactorFuncCallActPars factorFuncCallActPars) {
		// generisemo call instrukciju za poziv metoda koje ucestvuju u izrazima
		Obj functionObj = factorFuncCallActPars.getFactorFuncCallBeginning().getDesignator().obj;
		
		String functionObjName = functionObj.getName();
		switch(functionObjName) {
			case "ord":
				return;
			case "chr":
				return;
			case "len":
				Code.put(Code.arraylength);
				return;
		}
		
		int functionOffset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(functionOffset); // 2B adresa pocetka metode koju pozivamo sa CALL
	}
	
	// ProcCall
	public void visit(ActParsDesignator actParsDesignator) {
		// generisemo call instrukciju za poziv metoda koje ucestvuju u izrazima
		Obj functionObj = actParsDesignator.getFactorFuncCallBeginning().getDesignator().obj;
		
		// generisanje koda za predefinisane metode: ord/chr/len
		
		String functionObjName = functionObj.getName();
		switch(functionObjName) {
			case "ord":
				return;
			case "chr":
				return;
			case "len":
				Code.put(Code.arraylength);
				return;
		}
		
		int functionOffset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(functionOffset); // 2B adresa pocetka metode koju pozivamo sa CALL
		
		if (actParsDesignator.getFactorFuncCallBeginning().getDesignator().obj.getType() != Tab.noType) {
			// NON VOID metoda
			Code.put(Code.pop); // skidamo povratnu vrednost funkcije sa steka
		}
	}
		
	// smena Statement ::= RETURN [Expr] ;
	public void visit(ReturnStmt returnStmt) {
//		Code.put(Code.exit);
//		Code.put(Code.return_);
	}
	
	// smena Factor ::= Designator;
	// Designator je promenljiva koja ucestvuje u izrazima u nasem kodu
//	public void visit(DesignatorOne designatorOne) {
//		// proveriti da li je Designator promenljiva koja ucestvuje u izrazima
//		
//		SyntaxNode parent = designatorOne.getParent();
//		
////		if (AssignDesignator.class != parent.getClass() 
////				&& FactorFuncCallActPars.class != parent.getClass()) {
////			System.out.println("USAO 1");
////			Code.load(designatorOne.obj); // promenljiva - globalna(getstatic)/lokalna(load)
////		}
//		
//		// CHECK
//		if (parent.getClass() == FactorVar.class) {
//			Code.load(designatorOne.obj); // promenljiva - globalna(getstatic)/lokalna(load)
//		}
//		
//	}
	
	// obrada deklaracije metode
	public void visit(MethodReturn methodReturn) {		
		methodReturn.obj.setAdr(Code.pc);
		
		// dohvatanje argumenata i lokalnih varijabli
		SyntaxNode methodNode = methodReturn.getParent();
		
		// dohvatanje argumenata
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		// dohvatanje lokalnih promenljivi
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// generisanje ENTER instrukcije
		Code.put(Code.enter);
		Code.put(fpCnt.getCount()); // broj formalnih parametara 1. argument ENTER instr.
		Code.put(fpCnt.getCount() + varCnt.getCount()); // broj formalnih parametara + broj lokalnih prom. 2. argument ENTER instr.
		
		// vrsi se generisanje TELA funkcije, kada se bude obradjivala StatementList smena
	}
	
	public void visit(MethodVoid methodVoid) {
		// ukoliko je MAIN metoda, treba da stavimo mainPc u CodeGenerator
		if ("main".equalsIgnoreCase(methodVoid.getMethodName())) {
			mainPc = Code.pc;
		}
		
		methodVoid.obj.setAdr(Code.pc);
		
		// dohvatanje argumenata i lokalnih varijabli
		SyntaxNode methodNode = methodVoid.getParent();
		
		// dohvatanje argumenata
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		// dohvatanje lokalnih promenljivi
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// generisanje ENTER instrukcije
		Code.put(Code.enter);
		Code.put(fpCnt.getCount()); // broj formalnih parametara 1. argument ENTER instr.
		Code.put(fpCnt.getCount() + varCnt.getCount()); // broj formalnih parametara + broj lokalnih prom. 2. argument ENTER instr.
		
		// vrsi se generisanje TELA funkcije, kada se bude obradjivala StatementList smena
	}
	
	// zavrsetak deklaracije metode
	public void visit(MethodDecl methodDecl) {
		// generisemo pocetnu instrukciju ENTER - pravi aktivacioni zapis za svaku metodu na ExprStack-u
		
		// 1. parametar enter instr. - broj stvarnih argumenata metode
		// 2. parametar enter instr. - broj stvarnih argumenata metode + broj deklaracija lokalnih promenljivih u metodi
		
		// zavrsne instrukcije unutar metode
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	// smene DesignatorStatement
	
	// smena = DesignatorStatement ::= Designator:d Assignop:a Expr:e SEMI
	// generisanje koda za izraz DODELE VREDNOSTI
	// smatramo da se na ExprStacku nalazi vec vrednost Expr, pri obradi metode visit za generisanje koda obrade racunanja izraza

	public void visit(AssignDesignator assignDesignator) {
		Code.store(assignDesignator.getDesignator().obj); // cuvamo vrednost na ExprStack = getstatic/load instrukcija
	}
	
	public void visit(IncrementDesignator incrementDesignator) {
		// razlikujemo slucajeve ako je u pitanju promenljiva i element niza
		// ocekujemo da se na stacku u ovom trenutnu nalazi promenljiva/element niza
		Obj incrementObj = incrementDesignator.getDesignator().obj;
		String incrementObjType = getObjTypeByKind(incrementObj.getKind());
		switch (incrementObjType) {
	        case "Var":
				// promenljiva
				// prom = prom + 1;
	            Code.load(incrementDesignator.getDesignator().obj);
	            break;
	        case "Elem":
	        	// element niza
				// niz[0] = niz[0] + 1;
	            Code.put(Code.dup2); // kopiramo adresu niza, index elementa niza x2 puta, zbog kasnijeg poziva Code.store
	            Code.load(incrementDesignator.getDesignator().obj);
	            break;
	        default:
	            break;
		}
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(incrementDesignator.getDesignator().obj);
	}
	
	public void visit(DecrementDesignator decrementDesignator) {
		// razlikujemo slucajeve ako je u pitanju promenljiva i element niza
		// ocekujemo da se na stacku u ovom trenutnu nalazi promenljiva/element niza
		Obj decrementObj = decrementDesignator.getDesignator().obj;
		String decrementObjType = getObjTypeByKind(decrementObj.getKind());
		switch (decrementObjType) {
	        case "Var":
				// promenljiva
				// prom = prom + 1;
	            Code.load(decrementDesignator.getDesignator().obj);
	            break;
	        case "Elem":
	        	// element niza
				// niz[0] = niz[0] + 1;
	            Code.put(Code.dup2); // kopiramo adresu niza, index elementa niza x2 puta, zbog kasnijeg poziva Code.store
	            Code.load(decrementDesignator.getDesignator().obj);
	            break;
	        default:
	            break;
		}
        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(decrementDesignator.getDesignator().obj);
	}
	
	// smene Expr
	// smena Expr ::= Expr Addop Term
	public void visit(ExprTermAddopList exprTermAddopList) {
		// SMATRATI: elementi koji ucestvuju u izrazu su vec NA steku
		
		if (exprTermAddopList.getAddop() instanceof MinusOperand) {
			Code.put(Code.sub); // kod za instrukciju SUB
		} else if (exprTermAddopList.getAddop() instanceof PlusOperand) {
			Code.put(Code.add); // kod za instrukciju ADD
		}
		
	}
	
	public void visit(ExpressionNegTerm expressionNegTerm) {
		// smatramo da se vrednost koja se negira vec nalazi na expression steku
		Code.put(Code.neg); // kod za instrukciju NEG, stavlja suprotnu vrednost operanda na stek
	}
	
	// smene Term
	// smena Term ::= Factor;
	// smena Term ::= Term:term Mulop:mulop Factor:fact;
	public void visit(TermFactorList termFactorList) {
		Mulop mulop = termFactorList.getMulop();
		if (mulop instanceof MulOperand) {
			Code.put(Code.mul); // kod za instrukciju MUL
		}else if (mulop instanceof DivOperand) {
			Code.put(Code.div); // kod za instrukciju DIV
		}else if (mulop instanceof ModOperand) {
			Code.put(Code.rem); // kod za instrukciju REM
		} 
	}
	
	// smena foreach
	int foreachLoopStartAddress = Integer.MIN_VALUE;
	
	List<Integer> listForeachTopAddresses = new ArrayList<Integer>();
	boolean foreachLoopDetected = false;
	
	public void visit(ForeachPartFirst foreachPartFirst) {
		foreachLoopDetected = true;
		
		listBreakAddresses.add(new ArrayList<>());
		
		// foreach loop start
		Code.load(foreachPartFirst.getDesignator().obj); // niz
		Code.put(Code.arraylength); // duzina_niza
		Code.loadConst(0); // duzina_niza, cnt
		
		// foreach loop body
		foreachLoopStartAddress = Code.pc;
		listForeachTopAddresses.add(foreachLoopStartAddress);
		
		Code.load(foreachPartFirst.getDesignator().obj); // duzina_niza, cnt, niz
		Code.put(Code.dup2); // duzina_niza, cnt, niz, cnt, niz
		Code.put(Code.pop); // duzina_niza, cnt, niz, cnt
		Code.put(Code.aload); // duzina_niza, cnt, niz[cnt]
		Code.store(foreachPartFirst.obj); // tekuci_element = niz[cnt]; // duzina_niza, cnt, 
	}
	
	public void visit(ForeachPartLast foreachPartLast) {
		// foreach loop end
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup2);
		Code.putFalseJump(Code.le, foreachLoopStartAddress);
		Code.put(Code.pop); // skinuti cnt
		Code.put(Code.pop); // skinuti duzina_niza
		
		fixAddresses(listBreakAddresses);
	}
	
	public void visit(ForeachStmt foreachStmt) {
		foreachLoopDetected = false;
		foreachLoopStartAddress = Integer.MIN_VALUE;
		listBreakAddresses.remove(listBreakAddresses.size() - 1);
		listForeachTopAddresses.remove(listForeachTopAddresses.size() - 1);
	}
	
	// findAny metoda
	int findanyTop = Integer.MIN_VALUE;
	int findanyEnd = Integer.MIN_VALUE;
	int findanyElseAdr = Integer.MIN_VALUE;
	
	Obj findanyResult = null;
	Obj findanyArray = null;
	
	public void visit(FindAnyPartFirst findAnyPartFirst) {
		findanyResult = findAnyPartFirst.getDesignator().obj; // povratne vrednost funkcije findAny
		findanyArray = findAnyPartFirst.getDesignator1().obj; // adresa niza
		
		Code.loadConst(0); // boolean flag = false;
		Code.load(findAnyPartFirst.getDesignator1().obj); // 0, niz
		Code.put(Code.arraylength); // 0, duzina_niza
		Code.loadConst(0); // 0, duzina_niza, cnt
		
		// pocetak petlje
		findanyTop = Code.pc;
		
		Code.put(Code.dup2); // 0, duzina_niza, cnt, duzina_niza, cnt
		
		// poredimo duzina_niza, cnt, ako je duzina_niza <= cnt, skacemo na kraj programa
		// kraj programa nema poznatu adresu
		
		Code.putFalseJump(Code.gt, 0); // 0, duzina_niza, cnt // ovo radi
		findanyEnd = Code.pc - 2;
		
		// ucitati element niza na stek
		Code.load(findanyArray); // 0, duzina_niza, cnt, niz
		Code.put(Code.dup2);
		Code.put(Code.pop);
		
		if (findanyArray.getType().getElemType() == Tab.charType) {
			Code.put(Code.baload);
		}else {
			Code.put(Code.aload); // 0, duzina_niz, niz[cnt]
		}
	
	}
	
	public void visit(FindAnyCheckExpression findAnyCheckExpression) {
		// expression se nalazi na steku
		
		// poredimo element niza i expr
		// skacemo na else granu ako su razliciti 
		
		Code.putFalseJump(Code.eq, 0);
		findanyElseAdr = Code.pc - 2;
		
		// THEN grana
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.loadConst(1);
		Code.put(Code.dup);
		Code.put(Code.dup2);
		
		Code.putFalseJump(Code.ne, 0);
		findanyEnd = Code.pc - 2;
	}
	
	public void visit(FindAnyPartLast findAnyPartLast) {
		// ELSE grana
		Code.fixup(findanyElseAdr);
		
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup2);
		Code.putFalseJump(Code.eq, findanyTop);
	}

	public void visit(FindAnyStmt findAnyStmt) {
		Code.fixup(findanyEnd);
	
		// kraj programa
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.store(findanyResult);
		
		findanyTop = Integer.MIN_VALUE;
		findanyEnd = Integer.MIN_VALUE;
		findanyElseAdr = Integer.MIN_VALUE;
		
		findanyResult = null;
		findanyArray = null;
	}

	// findAndReplace metoda
	
	/*
	 * int[] findAndReplace(int[] oldArray, int exprLeft) {
	 * 		
	 * 	int[] replacedArray = new int[oldArray.length];
	 * 	
	 * 	int cnt = 0;
	 * 	while (replacedArray.length != cnt) {	// skacemo na kraj petlje	
	 * 		int curr = oldArray[cnt];			
	 * 
	 * 		if (curr == exprLeft) {
	 * 			curr = exprRight; // provucemo curr kroz exprRight	
	 * 			replacedArray[cnt] = curr;
	 * 			cnt = cnt + 1;
	 * 			// skacemo na pocetak petlje
	 * 		}else {
	 * 			replacedArray[cnt] = oldArray[cnt];
	 * 			cnt = cnt + 1;
	 * 			// skacemo na pocetak petlje
	 * 		}
	 * 	}
	 * 	
	 * 	return newArray;
	 * }
	 * 
	 * */
	
	int findAndReplaceTop = Integer.MIN_VALUE;
	int findAndReplaceEnd = Integer.MIN_VALUE;
	int findAndReplaceElse = Integer.MIN_VALUE;
	
	Obj replacedArrayObj = null;
	Obj oldArrayObj = null;
	Obj oldArrayElementObj = null;
	
	public void visit(FindAndReplaceStart findAndReplaceStart) {
		// alocirati prostor za novi niz jednake duzine kao pocetni niz
		Code.load(findAndReplaceStart.getDesignator1().obj); // nizStari
		Code.put(Code.arraylength); // duzina_stari_niz
		
		Code.put(Code.newarray);
		if (findAndReplaceStart.getDesignator1().obj.getType().getElemType() == Tab.charType) {
			// CHAR array
			Code.put(0);
		}else {
			// NON-CHAR array
			Code.put(1);
		}
		
		Code.store(findAndReplaceStart.getDesignator().obj); // ucitati adresu u novi niz		
		oldArrayObj = findAndReplaceStart.getDesignator1().obj;
		replacedArrayObj = findAndReplaceStart.getDesignator().obj;
		
		// ucitati duzinu novog niza na expr stack
		Code.load(findAndReplaceStart.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.loadConst(0); // duzina_replaced_niz, 0
		
		// while (duzina_replaced_niz != cnt)
		
		// pocetak while petlje
		findAndReplaceTop = Code.pc;
		
		Code.put(Code.dup2); // duzina_replaced_niz, cnt, duzina_replaced_niz, cnt
		Code.putFalseJump(Code.ne, 0); // jeq KRAJ_PROGRAMA
		findAndReplaceEnd = Code.pc - 2;
	}
	
	public void visit(FindAndReplaceMiddle findAndReplaceMiddle) {
//		System.out.println("USAO 2");
		
		// imamo na steku ExprLeft = 1. argument funkcije findAndReplace
		// stek trenutni izgled: duzina_niza, cnt, leftExpr, 
		
		// smestamo curr = stari_niz[cnt];
		
		if (oldArrayObj != null) {
			Code.put(Code.dup2); // duzina_niza, cnt, leftExpr, cnt, 
			Code.put(Code.pop); // duzina_niza, cnt, leftExpr, cnt, 
			Code.load(oldArrayObj); // duzina_niza, cnt, leftExpr, cnt, stari_niz
			Code.put(Code.dup_x1); // duzina_niza, cnt, leftExpr, stari_niz, cnt, stari_niz
			Code.put(Code.pop); // duzina_niza, cnt, leftExpr, stari_niz, cnt
			Code.put(Code.aload); // duzina_niza, cnt, leftExpr, stari_niz[cnt]
			Code.store(findAndReplaceMiddle.obj); // ovde cuvamo curr = stari_niz[cnt] = putstatic 2
			oldArrayElementObj = findAndReplaceMiddle.obj; // current_old_array_element
		}
		
	}

	public void visit(FindAndReplaceEnd findAndReplaceEnd) {		
		// moramo da ExprRight smestimo u iterator
		// na steku imamo duzina_niza, cnt, leftExpr, vrednost posle sto je curr provucen kroz rightExpr
		// // sada se u curr nalazi 
		
		Code.store(oldArrayElementObj); // curr = curr -> rightExpr
		
		// ucitati stari_niz[cnt]
		// stek: duzina_niza, cnt, leftExpr, cnt, stari_niz
		// ocekivano, stek: duzina_niza, cnt, leftExpr, stari_niz[cnt]

//		Code.load(oldArrayElementObj); // duzina_niza, cnt, leftExpr, curr(oldArray[cnt])
		
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.load(oldArrayObj); 
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
	
		Code.putFalseJump(Code.eq, 0); // jne SKACEMO na ELSE granu
		findAndReplaceElse = Code.pc - 2;
		
		// THEN grana
		// stek: duzina_niza, cnt, 
		// replacedArray[cnt] = curr_updated_old_array_element;
		Code.load(replacedArrayObj);
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.load(oldArrayElementObj); // curr_updated_old_array_element
		Code.put(Code.astore);
				
		// cnt = cnt + 1;
		Code.loadConst(1);
		Code.put(Code.add); // duzina_niza, cnt++
		Code.putJump(findAndReplaceTop);
	}

	public void visit(FindAndReplaceStmt findReplaceStmt) {		
		// ELSE grana
		// stek; duzina_niza, cnt
		
		// replacedArray[cnt] = oldArray[cnt];
		// duzina_niza, cnt, novi_niz, cnt, curr  
		Code.fixup(findAndReplaceElse);
		
		Code.load(replacedArrayObj);  
		Code.put(Code.dup2);
		Code.put(Code.pop); // duzina_niza, cnt, novi_niz, cnt, 
		Code.put(Code.dup); // duzina_niza, cnt, novi_niz, cnt, cnt
		Code.load(oldArrayObj); // duzina_niza, cnt, novi_niz, cnt, cnt, stari_niz
		Code.put(Code.dup_x1); // duzina_niza, cnt, novi_niz, cnt, stari_niz, cnt
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.astore); // da li treba astore/bastore
		
		// cnt = cnt + 1;
		Code.loadConst(1);
		Code.put(Code.add); // duzina_niza, cnt++
		Code.putJump(findAndReplaceTop);		
		
		Code.fixup(findAndReplaceEnd);
		
		Code.put(Code.pop);
		Code.put(Code.pop);
		
		findAndReplaceTop = Integer.MIN_VALUE;
		findAndReplaceEnd = Integer.MIN_VALUE;
		findAndReplaceElse = Integer.MIN_VALUE;
		replacedArrayObj = null;
		oldArrayObj = null;
		oldArrayElementObj = null;
	}
	
	// while statement
	// pamtimo adrese pocetka while petlji
	List<Integer> listWhileTopAddresses = new ArrayList<>();
	
	// utitlity funkcija za popravke adrese skoka
	private void fixAddresses(List<List<Integer>> addresses) {
	    if (!addresses.isEmpty()) {
	        List<Integer> lastAddressList = addresses.get(addresses.size() - 1);
	        for (int i = 0; i < lastAddressList.size(); i++) {
	            Code.fixup(lastAddressList.get(i));
	        }
	        lastAddressList.clear();
	    }
	}

	// posto while/if petlje mogu biti ugnjezdavane, moramo pamtiti adrese skokova po nivoima ugnjezdavanja - lista listi adresa
	List<List<Integer>> listBreakAddresses = new ArrayList<>();
	List<List<Integer>> listBitwiseAndBlockAddresses = new ArrayList<>();
	List<List<Integer>> listBitwiseOrBlockAddresses = new ArrayList<>();
	
	boolean whileLoopDetected = false;
	
	public void visit(WhileStmt whileStmt) {
		whileLoopDetected = false;
		
	    // kraj while petlje
	    listBitwiseAndBlockAddresses.remove(listBitwiseAndBlockAddresses.size() - 1);
	    listBitwiseOrBlockAddresses.remove(listBitwiseOrBlockAddresses.size() - 1);
	    listBreakAddresses.remove(listBreakAddresses.size() - 1);
	    listWhileTopAddresses.remove(listWhileTopAddresses.size() - 1);
	}

	public void visit(WhileStmtFirstPart whileStmtFirstPart) {
		whileLoopDetected = true;
		
	    // pocetak while pelje
	    listBitwiseAndBlockAddresses.add(new ArrayList<>());
	    listBitwiseOrBlockAddresses.add(new ArrayList<>());
	    listBreakAddresses.add(new ArrayList<>());
	    
	    // zapamtiti adresu pocetka while petlje
	    listWhileTopAddresses.add(Code.pc);
	}
	
	// obrada uslova u okviru while petlje/if naredbe
	public void visit(CondFactOne condFactOne) {
	    Code.loadConst(0); // false vrednost
	    Code.putFalseJump(Code.ne, 0); // jeq skacemo izvan while/if 
	    int lastIndex = listBitwiseAndBlockAddresses.size() - 1;
	    listBitwiseAndBlockAddresses.get(lastIndex).add(Code.pc - 2);
	}

	public void visit(CondFactMore condFactMore) {
	    Map<Class<? extends Relop>, Integer> relopMap = new HashMap<>();
	    relopMap.put(EqualOperand.class, Code.eq);
	    relopMap.put(NotEqualOperand.class, Code.ne);
	    relopMap.put(GreaterOperand.class, Code.gt);
	    relopMap.put(GreaterOrEqualOperand.class, Code.ge);
	    relopMap.put(LessOperand.class, Code.lt);
	    relopMap.put(LessOrEqualOperand.class, Code.le);

	    Class<? extends Relop> relopClass = condFactMore.getRelop().getClass();
	    int relopCode = relopMap.get(relopClass);
	    Code.putFalseJump(relopCode, 0);
	    
	    // prosli smo uslov
	    int lastIndex = listBitwiseAndBlockAddresses.size() - 1;
	    listBitwiseAndBlockAddresses.get(lastIndex).add(Code.pc - 2);
	}
	
	// detektovan or blok, ovde fixamo AND adrese
	public void visit(BeforeLastOrElement beforeLastOrElement) {
		// dosli smo do ove smene ako su sve prethodne smene == 1, skacemo u if granu
		Code.putJump(0);
	    int lastIndex = listBitwiseOrBlockAddresses.size() - 1;
	    listBitwiseOrBlockAddresses.get(lastIndex).add(Code.pc - 2);
	    	    
	    fixAddresses(listBitwiseAndBlockAddresses);	
	}
	
	public void visit(WhileStmtLastPart whileStmtLastPart) {
	    // skacemo na pocetak while petlje
		Code.putJump(listWhileTopAddresses.get(listWhileTopAddresses.size() - 1));
		
	    // prepraviti adrese skokova
		fixAddresses(listBitwiseAndBlockAddresses);
		fixAddresses(listBitwiseOrBlockAddresses);
	    fixAddresses(listBreakAddresses);
	}

	public void visit(ContinueStmt continueStmt) {
	    // skacemo na pocetak while petlje
		if (whileLoopDetected && !foreachLoopDetected) {
			Code.putJump(listWhileTopAddresses.get(listWhileTopAddresses.size() - 1));
		} else if (!whileLoopDetected && foreachLoopDetected) {
			Code.putJump(listForeachTopAddresses.get(listForeachTopAddresses.size() - 1));
		}
	}

	public void visit(BreakStmt breakStmt) {
	    // iskacemo izvan while petlje
	    Code.putJump(0);
	    int lastIndex = listBreakAddresses.size() - 1;
	    listBreakAddresses.get(lastIndex).add(Code.pc - 2);
	}
	
	// if statement
	List<List<Integer>> listJumpFromIfAddresses = new ArrayList<>();
	
	// kraj if smene
	public void visit(IfStmt ifStmt) {
	    listBitwiseAndBlockAddresses.remove(listBitwiseAndBlockAddresses.size() - 1);
	    listBitwiseOrBlockAddresses.remove(listBitwiseOrBlockAddresses.size() - 1);
	    listJumpFromIfAddresses.remove(listJumpFromIfAddresses.size() - 1);
	}
	
	// kraj if else smene
	public void visit(IfElseStmt ifElseStmt) {
	    fixAddresses(listJumpFromIfAddresses);
	    listBitwiseOrBlockAddresses.remove(listBitwiseOrBlockAddresses.size() - 1);
	    listBitwiseAndBlockAddresses.remove(listBitwiseAndBlockAddresses.size() - 1);
	    listJumpFromIfAddresses.remove(listJumpFromIfAddresses.size() - 1);
	}
	
	// pocetak if smene
	public void visit(BeforeCondition beforeCondition) {
	    listBitwiseAndBlockAddresses.add(new ArrayList<>());
	    listBitwiseOrBlockAddresses.add(new ArrayList<>());
	    listJumpFromIfAddresses.add(new ArrayList<>());
	}
	
	// zavrsetak uslova u if naredbi
	public void visit(RparentIfCondition rparentIfCondition) {
		// prepravljamo or blok adrese
		fixAddresses(listBitwiseOrBlockAddresses);
	}
	
	// kraj if bloka/pocetak else smene
	public void visit(IfStmtPartLast lastPart) {
		// ako smo detektovali if-else smenu, kada semo zavrsili sa obradom if grane, skacemo izvan nje
		boolean partElseDetected = (!(lastPart.getParent() instanceof IfStmt)); 
	    if (partElseDetected) {
	        Code.putJump(0);
	        listJumpFromIfAddresses.get(listJumpFromIfAddresses.size() - 1).add(Code.pc - 2);
	    }	
	    fixAddresses(listBitwiseAndBlockAddresses);
	}
	
	int arrLen = 0;
	
	public void visit(ExprListOne exprListOne) {
		arrLen = 1;
	}
	
	public void visit(ExprListMore exprListMore) {
		arrLen++;
	}
	
	public void visit(DesignatorArrayInit designatorArrayInit) {
		System.out.println("DUZINA NIZA: " + arrLen);
		
		int lastIndex = arrLen - 1;
		Stack<Integer> stackEnd = new Stack<>();
		
		// smestamo brojeve u obrnutom poretku
		// alociramo niz duzine arrLen
		Code.loadConst(arrLen);
		Code.put(Code.newarray);
		if (designatorArrayInit.getDesignator().obj.getType().getElemType() == Tab.charType) {
			Code.put(0);
		}else {
			Code.put(1);
		}
		Code.store(designatorArrayInit.getDesignator().obj);
		
		while (lastIndex >= 0) {
			Code.put(Code.dup);
			Code.load(designatorArrayInit.getDesignator().obj); // 1 2 3 3 niz  
			
			// ... niz last_index value
			Code.put(Code.dup_x1); // 1 2 3 niz 3 niz
			Code.put(Code.pop); // 1 2 3 niz 3 
			Code.loadConst(lastIndex); // 1 2 3 niz 3 last_index
			Code.put(Code.dup_x1); 
			Code.put(Code.pop); // 1 2 3 niz last_index 3 // 1 2 3 2 2 3
			
			if (designatorArrayInit.getDesignator().obj.getType().getElemType() == Tab.charType) {
				Code.put(Code.bastore);
			}else {
				Code.put(Code.astore);
			}
			
			Code.put(Code.pop);
			lastIndex--;
		}
		
		arrLen = 0;
	}
	
	public void visit(DesignatorArrayFilter filter) {
		// treba ispisati sve elemente koji po vrednosti ne != Expr:e
		
		Code.store(filter.obj); // sacuvan izraz
		
		Code.load(filter.getDesignator().obj); // niz
		Code.put(Code.arraylength); // duzina_niz
		Code.loadConst(0); // duzina_niz, 0
		
		int pocetakAdr = Code.pc;
		
		Code.put(Code.dup2);
		Code.putFalseJump(Code.ne, 0);
		int krajAdr = Code.pc - 2;
		
		// duzina_niz, index
		Code.put(Code.dup); // duzina_niz, index, index
		Code.load(filter.getDesignator().obj); // duzina_niz, index, index, niz
		Code.put(Code.dup_x1);
		Code.put(Code.pop); // duzina_niz, index, niz, index
		Code.put(Code.aload); // duzina_niz, index, niz[index]
		Code.load(filter.obj); // duzina_niz, index, niz[index], izraz
		
		Code.put(Code.dup2);
		Code.putFalseJump(Code.ne, 0); // jeq *** povecaj_index
		int povecaj_index = Code.pc - 2;
		
		// printuj element
		// duzina_niz, index, niz[index], izraz
		Code.put(Code.pop); // duzina_niz, index, niz[index]
		
		if (filter.getExpr().struct != Tab.charType) {
			Code.loadConst(5);
			Code.put(Code.print);
		}else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
		
		// duzina_niz, index
		// povecaj_index
		Code.loadConst(1);
		Code.put(Code.add);
		Code.putJump(pocetakAdr);
		
		// povecaj_index 
		Code.fixup(povecaj_index);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.putJump(pocetakAdr);
		
		// KRAJ
		Code.fixup(krajAdr);
		Code.put(Code.pop);
		Code.put(Code.pop);
		
	}
	
}
