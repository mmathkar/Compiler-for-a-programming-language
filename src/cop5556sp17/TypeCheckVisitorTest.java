/**  Important to test the error cases in case the
 * AST is not being completely traversed.
 * 
 * Only need to test syntactically correct programs, or
 * program fragments.
 */



/** Important to test the error cases in case the
 * AST is not being completely traversed.
 * 
 * Only need to test syntactically correct programs, or
 * program fragments.
 */

package cop5556sp17;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.Statement;
import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.TypeCheckVisitor.TypeCheckException;

public class TypeCheckVisitorTest {
 

 @Rule
 public ExpectedException thrown = ExpectedException.none();

 @Test
 public void testAssignmentBoolLit0() throws Exception{
  String input = "p {\nboolean y \ny <- false;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
  ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  program.visit(v, null);
 }

 @Test
 public void testAssignmentBoolLitError0() throws Exception {
  String input = "p {\nboolean y \ny <- 3;}";
  Scanner scanner = new Scanner(input);
  scanner.scan();
  Parser parser = new Parser(scanner);
  ASTNode program = parser.parse();
  TypeCheckVisitor v = new TypeCheckVisitor();
  thrown.expect(TypeCheckVisitor.TypeCheckException.class);
  program.visit(v, null); 
 } 
 
 @Test
	public void testRedeclaredVariable() throws Exception{
		String input = "p{integer x if(false){boolean x \n x <- 4;} }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}
 
 @Test
	public void testBinaryExp() throws Exception{
		String input = "p {\nimage x integer y boolean z z <- x == y \n;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}
	
	
	
	@Test
	public void testBinaryExpError() throws Exception{
		String input = "p {\nimage x integer y boolean z z <- y != x \n;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}
	
	@Test
	public void testIfStatementError() throws Exception{
		String input = "p {\nboolean y if(7){\ny <- 2;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}
	@Test
	public void testAssignmentError() throws Exception{
		String input = "p {\nimage z \nz <- 8;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);
	}
	
	@Test
	public void testWhileStatementError() throws Exception{
		String input = "p {\nimage y while(y){\ny <- 0;}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}
	
	@Test
	public void testBinaryCh() throws Exception{
		String input = "p {\nimage ident_img frame ident_frame ident_img -> ident_frame ->xloc \n;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);	
		
	}
	
	@Test
	public void testBinaryExpressionTest() throws Exception{
		String input = "p {\nboolean y \ny <- screenwidth < screenheight;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);		
	}
	
	
	
	@Test
	public void testBinary2() throws Exception{
		String input = "p {\nimage img frame iframe img -> iframe ->yloc \n;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);	
	}
	
	
	
	@Test
	public void testRedec() throws Exception{
		String input = "p{integer x if(true){boolean x \n x <- 3;} }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		thrown.expect(TypeCheckVisitor.TypeCheckException.class);
		program.visit(v, null);		
	}
}

	
