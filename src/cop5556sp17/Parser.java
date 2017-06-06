package cop5556sp17;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.*;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */
	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}
	
	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
//	@SuppressWarnings("serial")	
//	public static class UnimplementedFeatureException extends RuntimeException {
//		public UnimplementedFeatureException() {
//			super();
//		}
//	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
		
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	ASTNode parse() throws SyntaxException {
		ASTNode p=program();
		matchEOF();
		return p;
	}
	private static List<Kind> relOp   = new ArrayList<Kind>(Arrays.asList(LT , LE , GT , GE , EQUAL , NOTEQUAL));
	
	Expression expression() throws SyntaxException {
		//TODO
		Token firstToken=t;
		Expression e0 = null;
		Expression e1 = null;
		e0=term();
		while (relOp.contains(t.kind)) 
		{   Token op = t;
			consume();
			e1=term();
			e0 = new BinaryExpression(firstToken,e0,op,e1);
		}
		return e0;
	}
	private static List<Kind> weakOp   = new ArrayList<Kind>(Arrays.asList(PLUS, MINUS, OR));
	
	Expression term() throws SyntaxException {
		Token firstToken=t;
		Expression e0 = null;
		Expression e1 = null;
		e0=elem();
		while (weakOp.contains(t.kind))
		 {
			Token op = t;
			consume();
			e1=elem();
			e0=new BinaryExpression(firstToken,e0,op,e1);
		}
		return e0;
	}
	private static List<Kind> strongOp   = new ArrayList<Kind>(Arrays.asList(TIMES, DIV , AND , MOD));
	
	Expression elem() throws SyntaxException {
		Token firstToken=t;
		Expression e0 = null;
		Expression e1 = null;
		e0=	factor();
		while (strongOp.contains(t.kind))
		 {
			Token op = t;
			consume();
			e1=factor();
			e0=new BinaryExpression(firstToken,e0,op,e1);
			}
			return e0;
		
	}

	
	Expression factor() throws SyntaxException {
		Kind kind = t.kind;
		Expression e = null;
		
		switch (kind) {
		case IDENT: {
			e=new IdentExpression(t);
			consume();
		}
			break;
		case INT_LIT: {
			e = new IntLitExpression(t);
			consume();
		}
			break;
		case KW_TRUE:
		case KW_FALSE: {
			e= new BooleanLitExpression(t);
			consume();
		}
			break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			e= new ConstantExpression(t);
			consume();
		}
			break;
		case LPAREN: {
			consume();
			e=expression();
			match(RPAREN);
		}
			break;
		default:
			//you will want to provide a more useful error message
			throw new SyntaxException("illegal factor"+t.kind+"where we expected IDENT | INT_LIT | KW_TRUE | KW_FALSE| KW_SCREENWIDTH | KW_SCREENHEIGHT | ( expression )");
		}
		
		return e;
	}

	Block block() throws SyntaxException {
		//Statement s=null;Dec d=null;
		Block b=null;
		Token firstToken=t;
		ArrayList<Dec> dlist=new ArrayList<>();
		ArrayList<Statement> slist=new ArrayList<>();
		if(t.isKind(LBRACE))
		{
			consume();
			while (FIRSTdec.contains(t.kind) || t.isKind(KW_WHILE) || t.isKind(KW_IF) || t.isKind(IDENT)||  t.isKind(OP_SLEEP)||filterOp.contains(t.kind) || frameOp.contains(t.kind) || imageOp.contains(t.kind)) 
			{
				
				if(t.isKind(KW_WHILE) || t.isKind(KW_IF) || t.isKind(IDENT) || t.isKind(OP_SLEEP)||filterOp.contains(t.kind)|| frameOp.contains(t.kind) || imageOp.contains(t.kind))
				{
					Statement s=statement();
					slist.add(s);
				}
				else 
				{
					Dec d=dec();
					dlist.add(d);
				}
				
		    }
			match(RBRACE);
			b=new Block(firstToken,dlist,slist);
//			return b;
						
		}
		else throw new SyntaxException("saw " + t.kind + "expected LBRACE");
		
		return b;
		}
		

	Program  program() throws SyntaxException {
		Token firstToken=t;
		Block b=null;
		ArrayList<ParamDec> p=new ArrayList<ParamDec>();
		if(t.isKind(IDENT))
		{
			consume();
		if(t.isKind(LBRACE))
		{	
			b=block();
		}
		else
		{
			p.add(paramDec());
			while (t.isKind(COMMA)) 
			{
				consume();
				p.add(paramDec());
		    }
			b=block();
			}
		
		}
		else
		 throw new SyntaxException("Encountered" + t.kind + ", expected: <IDENT>");		
		return new Program(firstToken,p,b);
	}
	
	private static List<Kind> FIRSTparamDec = new ArrayList<Kind>(Arrays.asList(KW_URL,KW_INTEGER, KW_FILE, KW_BOOLEAN));

	ParamDec paramDec() throws SyntaxException {
		
		Token firstToken=t;
		Token sec=null;
		if(FIRSTparamDec.contains(t.kind))
		{   consume();
		    sec=t;
			match(IDENT);
		}
		
		else throw new SyntaxException("Encountered " + t.kind + "where we expected"+FIRSTparamDec);
		return new ParamDec(firstToken,sec);
		
	}
	
	
	private static List<Kind> FIRSTdec = new ArrayList<Kind>(Arrays.asList(KW_INTEGER,KW_BOOLEAN,KW_IMAGE, KW_FRAME));

	Dec dec() throws SyntaxException {
		Dec d=null;
		Token firstToken=t;
		if(FIRSTdec.contains(t.kind))
		{	consume();
			d=new Dec(firstToken,t);
			match(IDENT);
		}
		else 
			throw new SyntaxException("Encountered " + t.kind + "where we expected " + FIRSTdec);;
		return d;	
	}
	
	private static List<Kind> filterOp  = new ArrayList<Kind>(Arrays.asList(OP_BLUR,OP_GRAY,OP_CONVOLVE));
	private static List<Kind> frameOp  = new ArrayList<Kind>(Arrays.asList(KW_SHOW,KW_HIDE,KW_MOVE,KW_XLOC,KW_YLOC));
	private static List<Kind> imageOp   = new ArrayList<Kind>(Arrays.asList(OP_WIDTH,OP_HEIGHT,KW_SCALE));
	
	Statement statement() throws SyntaxException {
		Statement s=null;
		Block b=null;
		Token firstToken=t;
		Token peekToken = scanner.peek();
		Expression e=null;Chain c=null;
		IdentLValue i=null;
		if(t.isKind(OP_SLEEP))
		{
			consume();
			e=expression();
			s=new SleepStatement(firstToken,e);
			match(SEMI);
		}
		else if(t.isKind(KW_WHILE))
		{
			consume();
			match(LPAREN);
			e=expression();
			match(RPAREN);
			b=block();
			s=new WhileStatement(firstToken,e,b);
		}
		
		else if(t.isKind(KW_IF))
		{
			consume();
			match(LPAREN);
			e=expression();
			match(RPAREN);
			b=block();
			s=new IfStatement(firstToken,e,b);
		}
		
		else if(t.isKind(IDENT)&&(peekToken.isKind(ASSIGN)))
			{
			i=new IdentLValue(t);
			consume();
			consume();
			e=expression();
			match(SEMI);
			s=new AssignmentStatement(firstToken,i,e);
			
		}
		
		
		else if(t.isKind(IDENT) || filterOp.contains(t.kind) || frameOp.contains(t.kind) || imageOp.contains(t.kind))
		{
		s=chain();
		match(SEMI);
		}
		else throw new SyntaxException("Encountered" + t.kind + "where we expected <OP_SLEEP> or <KW_WHILE> or <KW_IF> or <IDENT> ");
		
		return s;
	}
	
	
	Chain chain() throws SyntaxException {
		
		Token firstToken=t;//
		Token arrow=null;//
		ChainElem e1=null;//
		Chain e0=null;//
		e0=chainElem();//
			if(t.isKind(ARROW)||t.isKind(BARARROW))
			{
				arrow=t;
				consume();
			}
			else
				throw new SyntaxException("Encountered" + t.kind + ", where we expected: <ARROW/BARARROW>");
			
			e1=chainElem();
			e0= new BinaryChain(firstToken,e0,arrow,e1);
			while(t.isKind(ARROW)||t.isKind(BARARROW))
			{
				arrow=t;
				consume();
				e1=chainElem();
				e0=new BinaryChain(firstToken,e0,arrow,e1);
				
			}
		
		return e0;
	}

	ChainElem chainElem() throws SyntaxException {
		ChainElem c=null;
		Tuple tup=null;
		Token firstToken=t;
		if(t.isKind(IDENT))
		{
			c=new IdentChain(t);
			consume();
		}
		
		else  
			if (filterOp.contains(t.kind)) 
			{
				consume();
				tup=arg();
				c=new FilterOpChain(firstToken,tup);
			}
			else if ( frameOp.contains(t.kind)) 
			{
				consume();
				tup=arg();
				c=new FrameOpChain(firstToken,tup);
			} 
			else if (imageOp.contains(t.kind)) 
			{
				consume();
				tup=arg();
				c=new ImageOpChain(firstToken,tup);
			}
			else throw new SyntaxException("Encountered" + t.kind + "where we expected IDENT or filterOp arg or frameOp arg or imageOp arg");
		
		
		 return c;
	}

	Tuple arg() throws SyntaxException {
		Token firstToken=t;
		Tuple tup=null;
		ArrayList<Expression> elist=new ArrayList<Expression>();
		Expression e0,e1=null;
		
		if(t.isKind(LPAREN))
		{
			consume();
			e0=expression();
			elist.add(e0);
			while(t.isKind(COMMA))
			{
				consume();
				e1=expression();
				elist.add(e1);
			}
			match(RPAREN);
			
		}
		tup=new Tuple(firstToken,elist);
		return tup;
			}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.isKind(EOF)) {
			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.isKind(kind)) {
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		
		for(Kind k:kinds)
		{if (t.isKind(k)) 
			return consume();
		
		}
		 throw new SyntaxException("Encountered " + t.kind + "expected " + kinds);
		
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}
