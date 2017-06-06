package cop5556sp17;



import static cop5556sp17.Scanner.Kind.*;

import cop5556sp17.Scanner.IllegalNumberException;

import cop5556sp17.Scanner.IllegalCharException;

import cop5556sp17.Parser.SyntaxException;

import static org.junit.Assert.assertEquals;



import java.util.ArrayList;



import org.junit.Rule;

import org.junit.Test;

import org.junit.rules.ExpectedException;



import cop5556sp17.AST.*;



public class ASTTest {



static final boolean doPrint = true;

static void show(Object s){

if(doPrint){System.out.println(s);}

}




@Rule

public ExpectedException thrown = ExpectedException.none();



@Test

public void testEmptyInput() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.parse();

}


@Test

public void testIfStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "if( abc > 3) { abc -> b; }";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.statement();

assertEquals(IfStatement.class, ast.getClass());


}


@Test

public void testFactorIdent() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(IdentExpression.class, ast.getClass());

}


@Test

public void testFactorBoolTrue() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "true";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BooleanLitExpression.class, ast.getClass());

}


@Test

public void testFactorBoolFalse() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "false";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BooleanLitExpression.class, ast.getClass());

}


@Test

public void testFactorBoolFalseBrackets() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(false)";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BooleanLitExpression.class, ast.getClass());

}


@Test

public void testFactorConstantWidth() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "screenwidth";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(ConstantExpression.class, ast.getClass());

}


@Test

public void testFactorConstantHeight() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "screenheight";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(ConstantExpression.class, ast.getClass());

}



@Test

public void testFactorIntLit() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "123";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(IntLitExpression.class, ast.getClass());

}



@Test
public void testStatementError() throws IllegalCharException, IllegalNumberException, SyntaxException {

	//String input = " a <= 7";
	String input = " x -> y -> z;";

	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	ASTNode ast = parser.statement();

	assertEquals(BinaryChain.class, ast.getClass());

	BinaryChain be = (BinaryChain) ast;

	assertEquals(BinaryChain.class, be.getE0().getClass());
	assertEquals(ARROW, be.getArrow().kind);
	assertEquals(IdentChain.class, be.getE1().getClass());



	}

@Test
public void testStatementBlockChain2() throws IllegalCharException, IllegalNumberException, SyntaxException {

	
	String input = " { x -> y -> z; }";

	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	Block ast = parser.block();

	assertEquals(Block.class, ast.getClass());

	}

@Test
public void testImageOpStatement() throws IllegalCharException, IllegalNumberException, SyntaxException {

	
	String input = "tos integer x\n{image i frame f i -> scale (x) -> f;}";

	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	ASTNode ast = parser.program();

	assertEquals(Program.class, ast.getClass());

	}

@Test
public void testImageOpStatement3() throws IllegalCharException, IllegalNumberException, SyntaxException {

	
	String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height -> x; frame f i -> scale (x) -> f;}";

	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	ASTNode ast = parser.program();

	assertEquals(Program.class, ast.getClass());

	}

@Test
public void testFilterop1() throws IllegalCharException, IllegalNumberException, SyntaxException {

	
	String input = "  x -> show -> hide ;";
	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	ASTNode ast = parser.statement();

	assertEquals(BinaryChain.class, ast.getClass());

	BinaryChain be = (BinaryChain) ast;

	assertEquals(BinaryChain.class, be.getE0().getClass());
	assertEquals(ARROW, be.getArrow().kind);
	assertEquals(FrameOpChain.class, be.getE1().getClass());


	}

@Test
public void testFilterop2() throws IllegalCharException, IllegalNumberException, SyntaxException {

	
	String input = "  x -> move (3,4) -> hide ;";
	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	ASTNode ast = parser.statement();

	assertEquals(BinaryChain.class, ast.getClass());

	BinaryChain be = (BinaryChain) ast;

	assertEquals(BinaryChain.class, be.getE0().getClass());
	assertEquals(ARROW, be.getArrow().kind);
	assertEquals(FrameOpChain.class, be.getE1().getClass());


	}

@Test
public void testFilterop3() throws IllegalCharException, IllegalNumberException, SyntaxException {

	
	String input = "  x -> show |-> move (x,y);";
	Scanner scanner = new Scanner(input);

	scanner.scan();

	Parser parser = new Parser(scanner);

	ASTNode ast = parser.statement();

	assertEquals(BinaryChain.class, ast.getClass());

	BinaryChain be = (BinaryChain) ast;

	assertEquals(BinaryChain.class, be.getE0().getClass());
	assertEquals(BARARROW, be.getArrow().kind);
	assertEquals(FrameOpChain.class, be.getE1().getClass());


	}

@Test

public void testIfStatement1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = " if (true) \n {x -> show |-> move (x,y) ;} ";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.statement();

assertEquals(IfStatement.class, ast.getClass());
IfStatement wh = (IfStatement) ast;
assertEquals(BooleanLitExpression.class, wh.getE().getClass());
assertEquals(Block.class, wh.getB().getClass());
}







@Test
public void testWhileStatementH() throws IllegalCharException, IllegalNumberException, SyntaxException {
	String input = " while (true) \n {x -> show |-> move (x,y) ;} ";
	Scanner scanner = new Scanner(input);
	scanner.scan();
	Parser parser = new Parser(scanner);
	ASTNode ast = parser.statement();
	assertEquals(WhileStatement.class, ast.getClass());
	WhileStatement wh = (WhileStatement) ast;
	assertEquals(BooleanLitExpression.class, wh.getE().getClass());
}


@Test

public void testWeakOp() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "a - b";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression be = (BinaryExpression) ast;

assertEquals(IdentExpression.class, be.getE0().getClass());

assertEquals(IdentExpression.class, be.getE1().getClass());

assertEquals(MINUS, be.getOp().kind);

}


@Test

public void testStrongOp() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "2 * 5";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression be = (BinaryExpression) ast;

assertEquals(IntLitExpression.class, be.getE0().getClass());

assertEquals(IntLitExpression.class, be.getE1().getClass());

assertEquals(TIMES, be.getOp().kind);

}

@Test

public void testBinaryExpr01() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "1+abc";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression be = (BinaryExpression) ast;

assertEquals(IntLitExpression.class, be.getE0().getClass());

assertEquals(IdentExpression.class, be.getE1().getClass());

assertEquals(PLUS, be.getOp().kind);

}


 // Jahin

@Test

public void testProgram01() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class,ast.getClass());

Program be = (Program) ast;

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals(java.util.ArrayList.class, be.getParams().getClass());

assertEquals(Block.class, be.getB().getClass());

Block b = be.getB();

assertEquals("{",b.getFirstToken().getText());

assertEquals(new ArrayList<Statement>(),b.getStatements());


}


@Test

public void testProgram02() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "file abc";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.program();

}


@Test

public void testProgram03() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnv {integer nn}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class,ast.getClass());

Program be = (Program) ast;

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals(java.util.ArrayList.class, be.getParams().getClass());

assertEquals(Block.class, be.getB().getClass());

Block b = be.getB();

assertEquals("{",b.getFirstToken().getText());

Dec a = b.getDecs().get(0);

assertEquals(KW_INTEGER,a.getFirstToken().kind);

assertEquals("nn",a.getIdent().getText());


}


@Test

public void testProgram4() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnv {grayish <-  998|(abcgg*77%true+9<=false<98>=abc>nncd8==(a!=b));}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class,ast.getClass());

Program be = (Program) ast;

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals(java.util.ArrayList.class, be.getParams().getClass());

assertEquals(Block.class, be.getB().getClass());

Block b = be.getB();

assertEquals("{",b.getFirstToken().getText());

AssignmentStatement s = (AssignmentStatement) b.getStatements().get(0);

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals(IdentLValue.class, s.getVar().getClass());

assertEquals("grayish", s.getVar().getText());

assertEquals(BinaryExpression.class, s.getE().getClass());


}


@Test

public void testProgram5() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnddv {integer nn grayish <-  998|(abcgg*77%true+9<=false<98>=abc>nncd8==(a!=b));}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class,ast.getClass());

Program be = (Program) ast;

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals("mnddv",be.getFirstToken().getText());

assertEquals(java.util.ArrayList.class, be.getParams().getClass());

assertEquals(Block.class, be.getB().getClass());

Block b = be.getB();

assertEquals("{",b.getFirstToken().getText());

Dec a = b.getDecs().get(0);

assertEquals(KW_INTEGER,a.getFirstToken().kind);

assertEquals("nn",a.getIdent().getText());

AssignmentStatement s = (AssignmentStatement) b.getStatements().get(0);

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals(IdentLValue.class, s.getVar().getClass());

assertEquals("grayish", s.getVar().getText());

assertEquals(BinaryExpression.class, s.getE().getClass());

}


@Test

public void testProgram6() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnv url fcv {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class,ast.getClass());

Program be = (Program) ast;

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals("mnv",be.getFirstToken().getText());

assertEquals(java.util.ArrayList.class, be.getParams().getClass());

assertEquals(Block.class, be.getB().getClass());

Block b = be.getB();

ParamDec p = be.getParams().get(0);

assertEquals(KW_URL,p.getFirstToken().kind);

assertEquals("fcv",p.getIdent().getText());

assertEquals("{",b.getFirstToken().getText());

}




@Test

public void testProgram7() throws IllegalCharException, IllegalNumberException, SyntaxException {

//String input = "tos url u,\n integer x\n{integer y image i u -> i; i -> height -> x; frame f i -> scale (x) -> f;}";
String input = "mnddv integer kkk,file vfrh {image hello}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class,ast.getClass());

Program be = (Program) ast;

assertEquals(IDENT,be.getFirstToken().kind);

assertEquals("mnddv",be.getFirstToken().getText());

assertEquals(java.util.ArrayList.class, be.getParams().getClass());

assertEquals(Block.class, be.getB().getClass());

Block b = be.getB();

ParamDec p = be.getParams().get(0);

assertEquals(KW_INTEGER,p.getFirstToken().kind);

assertEquals("kkk",p.getIdent().getText());

p = be.getParams().get(1);

assertEquals(KW_FILE,p.getFirstToken().kind);

assertEquals("vfrh",p.getIdent().getText());


assertEquals("{",b.getFirstToken().getText());

Dec a = b.getDecs().get(0);

assertEquals(KW_IMAGE,a.getFirstToken().kind);

assertEquals("hello",a.getIdent().getText());

}


@Test

public void testProgram8() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "url fcv {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.program();

}



@Test

public void testProgram9() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnddv ";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.program();

}


@Test

public void testProgram10() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnv url fcv ";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.program();

}




@Test

public void testProgram11() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnddv {hello}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.program();

}


@Test

public void testProgram12() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "mnv url fcv, {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

parser.program();

}


// Arunima


@Test

public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(IdentExpression.class, ast.getClass());

}



@Test

public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "123";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(IntLitExpression.class, ast.getClass());

}







@Test

public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "1+abc";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression be = (BinaryExpression) ast;

assertEquals(IntLitExpression.class, be.getE0().getClass());

assertEquals(IdentExpression.class, be.getE1().getClass());

assertEquals(PLUS, be.getOp().kind);

}


@Test

public void testArg0() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "  (3,5) ";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.arg();

assertEquals(Tuple.class, ast.getClass());

Tuple tu = (Tuple) ast;

assertEquals(IntLitExpression.class, tu.getExprList().get(0).getClass());

assertEquals(IntLitExpression.class, tu.getExprList().get(1).getClass());

}


@Test

public void testArg1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "  (3,) ";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.arg();

}



@Test

public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "prog0 {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class, ast.getClass());

Program pr = (Program) ast;

assertEquals(IDENT, pr.getFirstToken().kind);

assertEquals(Block.class, pr.getB().getClass());

}


@Test

public void testFactor() throws IllegalCharException, IllegalNumberException, SyntaxException{

String input = "()";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.factor();

}


@Test

public void testFactor2() throws IllegalCharException, IllegalNumberException, SyntaxException{

String input = "screenheight";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.factor();

assertEquals(ConstantExpression.class, ast.getClass());

}


@Test

public void testFactor3() throws IllegalCharException, IllegalNumberException, SyntaxException{

String input = "screenheight1234";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.factor();

assertEquals(IdentExpression.class, ast.getClass());

}


@Test

public void testFactor4() throws IllegalCharException, IllegalNumberException, SyntaxException{

String input = "screenheight 1234"; //Note that 1234 will not be parsed with this method call

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.factor();

assertEquals(ConstantExpression.class, ast.getClass());

}


@Test

public void testElem() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(abc*1234%5/def&true)";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.elem();

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression binExp = (BinaryExpression) ast; //abc, *, 1234%5/def&true

assertEquals(AND, binExp.getOp().kind); // This is an expression hence right associative

assertEquals(BinaryExpression.class, binExp.getE0().getClass());

assertEquals(BooleanLitExpression.class, binExp.getE1().getClass());

BinaryExpression binExp1 = (BinaryExpression) binExp.getE0(); //1234, %, 5/def&true

assertEquals(IdentExpression.class, binExp1.getE1().getClass());

assertEquals(DIV, binExp1.getOp().kind);

BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0(); //1234, %, 5/def&true

assertEquals(IntLitExpression.class, binExp2.getE1().getClass());

assertEquals(MOD, binExp2.getOp().kind);

BinaryExpression binExp3 = (BinaryExpression) binExp2.getE0(); //1234, %, 5/def&true

assertEquals(IdentExpression.class, binExp3.getE0().getClass());

assertEquals(IntLitExpression.class, binExp3.getE1().getClass());

assertEquals(TIMES, binExp3.getOp().kind);


}


@Test

public void testElem1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(abc*1234%5/def&&true)"; // strongOp not followed by factor

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.elem();

}


@Test

public void testTerm() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(abc*1234%5/def&true)";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.term();

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression binExp = (BinaryExpression) ast;

assertEquals(AND, binExp.getOp().kind);

assertEquals(BooleanLitExpression.class, binExp.getE1().getClass());

BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();

assertEquals(IdentExpression.class, binExp1.getE1().getClass());

assertEquals(DIV, binExp1.getOp().kind);

BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();

assertEquals(IntLitExpression.class, binExp2.getE1().getClass());

assertEquals(MOD, binExp2.getOp().kind);

BinaryExpression binExp3 = (BinaryExpression) binExp2.getE0();

assertEquals(IntLitExpression.class, binExp3.getE1().getClass());

assertEquals(IdentExpression.class, binExp3.getE0().getClass());

assertEquals(TIMES, binExp3.getOp().kind);


}


@Test

public void testTerm1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc*1234%5/def&true|false&21+121-something";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.term(); //(abc*1234%5/def&true, |, false&21)+121-something

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression binExp = (BinaryExpression) ast;

assertEquals(MINUS, binExp.getOp().kind);

assertEquals(BinaryExpression.class, binExp.getE0().getClass());

BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();

assertEquals(BinaryExpression.class, binExp1.getE0().getClass());

assertEquals(IntLitExpression.class, binExp1.getE1().getClass());

assertEquals(PLUS, binExp1.getOp().kind);

BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();

assertEquals(BinaryExpression.class, binExp2.getE0().getClass());

assertEquals(OR, binExp2.getOp().kind);

}


@Test

public void testTerm2() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc*1234%5/def&true|false&21+121-something/";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.term();

}


@Test

public void testTerm3() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc*1234%5/def&true|false&21+121-something and someMorething";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.term(); //abc*1234%5/def&true, |, false&21+121-something  // will stop at abc*1234%5/def&true|false&21+121-something 

assertEquals(BinaryExpression.class, ast.getClass());

BinaryExpression binExp = (BinaryExpression) ast;

assertEquals(MINUS, binExp.getOp().kind);

assertEquals(BinaryExpression.class, binExp.getE0().getClass());

assertEquals(IdentExpression.class, binExp.getE1().getClass());

BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();

assertEquals(BinaryExpression.class, binExp1.getE0().getClass());

assertEquals(IntLitExpression.class, binExp1.getE1().getClass());

assertEquals(PLUS, binExp1.getOp().kind);

BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();

assertEquals(BinaryExpression.class, binExp2.getE0().getClass());

assertEquals(OR, binExp2.getOp().kind);

}


@Test

public void testExpression() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(something)<=somethingMore>somethingLess!=nothing==void";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.expression();

BinaryExpression binExp = (BinaryExpression) ast;

assertEquals(EQUAL, binExp.getOp().kind);

assertEquals(BinaryExpression.class, binExp.getE0().getClass());

assertEquals(IdentExpression.class, binExp.getE1().getClass());

BinaryExpression binExp1 = (BinaryExpression) binExp.getE0();

assertEquals(BinaryExpression.class, binExp1.getE0().getClass());

assertEquals(IdentExpression.class, binExp1.getE1().getClass());

assertEquals(NOTEQUAL, binExp1.getOp().kind);

BinaryExpression binExp2 = (BinaryExpression) binExp1.getE0();

assertEquals(BinaryExpression.class, binExp2.getE0().getClass());

assertEquals(GT, binExp2.getOp().kind);

BinaryExpression binExp3 = (BinaryExpression) binExp2.getE0();

assertEquals(IdentExpression.class, binExp3.getE1().getClass());

assertEquals(LE, binExp3.getOp().kind);

assertEquals(IdentExpression.class, binExp3.getE0().getClass());

}


@Test

public void testArg2() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.arg();

assertEquals(Tuple.class, ast.getClass());

}


@Test

public void testArg3() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(something)<=somethingMore>somethingLess!=nothing==void, (), 1234, a*b, ,";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.arg(); //(something) and stops here

assertEquals(Tuple.class, ast.getClass());

Tuple tuple = (Tuple) ast;

assertEquals(IdentExpression.class, tuple.getExprList().get(0).getClass());

}


@Test

public void testArg4() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "(something)<=somethingMore>somethingLess!=nothing==void";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.arg(); //(something) and stops here

assertEquals(Tuple.class, ast.getClass());

Tuple tuple = (Tuple) ast;

assertEquals(IdentExpression.class, tuple.getExprList().get(0).getClass());

}


@Test

public void testChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "abc blur";//blur will be ignored

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.chainElem(); //stops as abc

assertEquals(IdentChain.class, ast.getClass());

}


@Test

public void testChainElem1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "blur";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.chainElem();

assertEquals(FilterOpChain.class, ast.getClass());

}


@Test

public void testChainElem2() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "blur123";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.chainElem();

assertEquals(IdentChain.class, ast.getClass());

}


@Test

public void testIf1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "if";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.statement();

}


@Test

public void testIf2() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "if(){";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.statement();

}


@Test

public void testIf3() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "if(){}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.statement();

}


@Test

public void testIf5() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "if(abc>=123){integer def while(1){def -> blur;}}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.statement();

assertEquals(IfStatement.class, ast.getClass());

IfStatement ifStatement = (IfStatement) ast;

assertEquals(BinaryExpression.class, ifStatement.getE().getClass());

BinaryExpression binExp = (BinaryExpression) ifStatement.getE();

assertEquals(GE, binExp.getOp().kind);

assertEquals(Block.class, ifStatement.getB().getClass());

}




@Test

public void testIf6() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "if(abc>=123){integer def while(1){def -> blur somethingToCauseError}}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.statement();

}


@Test

public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "value file value1 integer value2 {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

thrown.expect(Parser.SyntaxException.class);

ASTNode ast = parser.program();

}


@Test

public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "value file value1, integer value2 {}";

Scanner scanner = new Scanner(input);

scanner.scan();

Parser parser = new Parser(scanner);

ASTNode ast = parser.program();

assertEquals(Program.class, ast.getClass());

}


@Test

public void testProgram3() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input2 = "value file value1, image value2 {}";

Parser parser2 = new Parser(new Scanner(input2).scan());

thrown.expect(Parser.SyntaxException.class);

parser2.parse();

}





@Test

public void testNested1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "value file value1, integer value2 {if((something)>=(SomethingElse)){sleep sleepExp; \r\n gray(somethings, again, and, again1)->screenwidth345;}}}";

Parser parser = new Parser(new Scanner(input).scan());

thrown.expect(Parser.SyntaxException.class);

parser.parse();

}


@Test

public void testParse1() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "prog2 {integer a };";

Parser parser = new Parser(new Scanner(input).scan());

thrown.expect(Parser.SyntaxException.class);

parser.parse();

}


@Test

public void testParse2() throws IllegalCharException, IllegalNumberException, SyntaxException {

String input = "123(";

Parser parser = new Parser(new Scanner(input).scan());

thrown.expect(Parser.SyntaxException.class);

parser.chainElem();

}



}