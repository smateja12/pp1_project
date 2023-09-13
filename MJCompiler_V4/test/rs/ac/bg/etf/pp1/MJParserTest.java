package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.mj.runtime.Code;

public class MJParserTest {
	
	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(MJParserTest.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program_mod_test.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
				
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse(); // pocetak parsiranja
			 
			Program prog = (Program)(s.value);
			
			// inicijalizacija tabele simbola
			Tab.init();

			// ispis sintaksnog stabla
			log.info("===========================================");
			log.info(prog.toString(""));
			log.info("===========================================");
			
			// ispis prepoznatih programskih konstrukcija
//			RuleVisitor v = new RuleVisitor();
//			prog.traverseBottomUp(v);
			
			SemanticPass v = new SemanticPass();
			prog.traverseBottomUp(v);
			
			log.info("Print count calls = " + v.printCallCount);
			
			log.info("Deklarisanih promenljivih ima = " + v.varDeclCount);
			
			// ispis tabele simbola
			tsdump();
			
			if (!p.errorDetected && v.passed()) {
				log.info("Semanticka analiza USPESNO zavrsena!");
				
				File objFile = new File("test/program.obj");
				if (objFile.exists()) {
					objFile.delete();
				}
				
				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
			
				Code.write(new FileOutputStream(objFile));
			
				log.info("Parsiranje je USPESNO zavrseno!");
			}else {
				log.info("Semanticka analiza NEUSPESNO zavrsena!");
			}
			
		} finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}
	}
	
	public static void tsdump() {
		Logger log = Logger.getLogger(MJParserTest.class);
		log.info("===========================================");
		ChildTab.dump();
		log.info("===========================================");
	}
	
}
