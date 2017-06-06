package cop5556sp17;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;


public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@Test
	public void test1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida university";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}

	@Test
	public void test4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida { university }";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida { integer 123}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida { integer abc}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}

	@Test
	public void test7() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida  integer 123 {\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test8() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida boolean abc \n  {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}

	@Test
	public void test9() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida   integer 123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida image";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida integer abc, boolean true {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test12() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida integer abc, file pqr {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}

	@Test
	public void test13() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida url gmail {\n image google\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}

	@Test
	public void test14() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida url gmail{while(alphabet)}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.parse();
	}

	@Test
	public void test15() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "florida url gmail{while(alphabet){}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.parse();
	}

	@Test
	public void test16() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test17() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{alpha}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test18() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{image abc}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test19() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{sleep true}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test20() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{\nsleep false;\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test21() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ if false\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test22() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{if()false}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test23() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{if ()}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test24() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ if (true)}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test25() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ if (false) {}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test26() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{if(false{})}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test27() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ if true {}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test28() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ if (treu) {}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test29() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ if (true) {integer abc}}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test30() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ abc\n;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test31() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{abc=123}";
		Scanner scanner = new Scanner(input);
		thrown.expect(Scanner.IllegalCharException.class);
		scanner.scan();
		/*
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
		*/
	}

	@Test
	public void test32() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{abc = 123;}";
		Scanner scanner = new Scanner(input);
		thrown.expect(Scanner.IllegalCharException.class);
		scanner.scan();
		/*
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
		*/
	}

	@Test
	public void test33() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{abc <- 123;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test34() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{abc == boolean;";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test35() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{abc == ;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test36() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ = true;}";
		Scanner scanner = new Scanner(input);
		thrown.expect(Scanner.IllegalCharException.class);
		scanner.scan();
		/*
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
		*/
	}

	@Test
	public void test37() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ == true;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test38() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{->\nabc}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test39() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{ pqr -> abc}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test40() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{pqr -> abc;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test41() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{pqr - > abc;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test42() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{pqr |-> abc;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test43() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{show -> pqr |-> hide(true)}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test44() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{show -> pqr |-> hide(true)};";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test45() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{show (true) -> hide(true);}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}
	
	@Test
	public void testCorrection() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "__ {__->_|->$0|-> show (__/_%($$TAT$T_T%$)|true*screenwidth&$|_*$!=_==_>=(z_z),_&$|_$+_0); while (__==$$!=$_/_){sleep z$_2+_3z%$;} blur -> width ($);}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//parser.block();
		parser.parse();
	}

	@Test
	public void test46() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{show true->\nabc}"; //no semicolon
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
		//parser.parse();
	}

	@Test
	public void test47() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{move(\ntrue) |-> pqr\n;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
		
	}

	@Test
	public void test48() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{xloc(x==5)->triangle;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test49() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{height (ht < 10) |-> area;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.block();
	}

	@Test
	public void test50() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "{height (ht < ) -> area;}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.block();
	}

	@Test
	public void test51() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while()";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}

	@Test
	public void test52() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(){}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}

	@Test
	public void test53() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(x==5){\nabc <- 5;\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void test54() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while(c=5){}";
		Scanner scanner = new Scanner(input);
		thrown.expect(Scanner.IllegalCharException.class);
		scanner.scan();
		/*
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
		*/
	}

	@Test
	public void test55() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "while ( x <= 5 ){";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}

	@Test
	public void test56() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "\nif\n(\nx\n>\n6\n)\n{\nsleep\ntrue\n;\n}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void test57() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  if   ( x < 2 *10) {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void test58() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "if ( x > 2 + a / 5) {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.statement();
	}

	@Test
	public void test59() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "( abc != 5)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.arg();
	}

	@Test
	public void test60() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "( abc != 6";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void test61() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "( a>=1\n, b <\n2, c\n==d)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.arg();
	}

	@Test
	public void test62() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc != 5)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		// ** Not throwing error as arg just checks if first token is LPAREN if not then do nothing
		//
		// thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void test63() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(a+b,c-d,)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void test64() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "()";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}

	@Test
	public void test65() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "( abc \n,pqr)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.arg();
	}

	@Test
	public void test66() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test67() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test68() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "==";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.expression();
	}

	@Test
	public void test69() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "/";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.expression();
	}

	@Test
	public void test70() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "+";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.expression();
	}

	@Test
	public void test71() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc 123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		//** Expression just checks the first term and returns
		//thrown.expect(Parser.SyntaxException.class);
		parser.expression();
	}

	@Test
	public void test72() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc + 123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test73() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc * 123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test74() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc != pqr";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test75() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "2 % 5 + 5 % 2";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test76() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc | true & false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test77() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a + \n5 < 12";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test78() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a > b + \n5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test79() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a - 5 /*something*/ >= b - 5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test80() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "a | (b <= 10)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.expression();
	}

	@Test
	public void test81() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.statement();
	}


	// Example Test cases by Prof

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}

	@Test
	public void testArg() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.arg();
	}

	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
	}


	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}

}
