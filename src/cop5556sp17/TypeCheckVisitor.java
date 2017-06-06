package cop5556sp17;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.AST.WhileStatement;

import java.util.ArrayList;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.LinePos;
import cop5556sp17.Scanner.Token;
import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.Scanner.Kind.ARROW;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_XLOC;
import static cop5556sp17.Scanner.Kind.KW_YLOC;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;
import static cop5556sp17.Scanner.Kind.*;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String message) {
			super(message);
		}
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e0=binaryExpression.getE0();
		Token op=binaryExpression.getOp();
		Expression e1=binaryExpression.getE1();
		visitExpression(e0,arg);
		visitExpression(e1,arg);

		switch(e0.getType())
		{
		case BOOLEAN:
			if((op.isKind(LT)||op.isKind(LE)||op.isKind(GT)||op.isKind(GE)) && e1.getType()==BOOLEAN)
				binaryExpression.setTypeName(BOOLEAN);
			else if((op.isKind(EQUAL)||op.isKind(NOTEQUAL)) && e1.getType()==BOOLEAN)
				binaryExpression.setTypeName(BOOLEAN);
			else if((op.isKind(AND)||op.isKind(OR)) && e1.getType()==BOOLEAN)
				binaryExpression.setTypeName(BOOLEAN);
			else throw new TypeCheckException("Expected binary boolean expression");
			break;

		case IMAGE:
			if((op.isKind(PLUS)||op.isKind(MINUS)) && e1.getType()==IMAGE)
				binaryExpression.setTypeName(IMAGE);
			else if(op.isKind(TIMES) && e1.getType()==INTEGER)
				binaryExpression.setTypeName(IMAGE);
			else if((op.isKind(EQUAL)||op.isKind(NOTEQUAL)) && e1.getType()==IMAGE)
				binaryExpression.setTypeName(BOOLEAN);
			else if(op.isKind(DIV) && e1.getType()==INTEGER)//ADDED
				binaryExpression.setTypeName(IMAGE);
			else if(op.isKind(MOD) && e1.getType()==INTEGER)//ADDED
				binaryExpression.setTypeName(IMAGE);
			else throw new TypeCheckException("Expected binary IMAGE expression");

			break;
		case INTEGER:
			if((op.isKind(PLUS)||op.isKind(MINUS)) && e1.getType()==INTEGER)
				binaryExpression.setTypeName(INTEGER);
			else if((op.isKind(TIMES)||op.isKind(DIV)) && e1.getType()==INTEGER)
				binaryExpression.setTypeName(INTEGER);
			else if((op.isKind(MOD)) && e1.getType()==INTEGER)//ADDED
				binaryExpression.setTypeName(INTEGER);
			else if((op.isKind(TIMES)) && e1.getType()==IMAGE)
				binaryExpression.setTypeName(IMAGE);
			else if((op.isKind(LT)||op.isKind(LE)||op.isKind(GT)||op.isKind(GE)) && e1.getType()==INTEGER)
				binaryExpression.setTypeName(BOOLEAN);
			else if((op.isKind(EQUAL)||op.isKind(NOTEQUAL)) && e1.getType()==INTEGER)
				binaryExpression.setTypeName(BOOLEAN);
			else throw new TypeCheckException("Expected binary integer expression");
			break;

		default:

			if((op.isKind(EQUAL)||op.isKind(NOTEQUAL)) && e1.getType()==e0.getType())
				binaryExpression.setTypeName(BOOLEAN);

			else {
			throw new TypeCheckException("Expected binary expression");
			}
			break;

		}

		return null;
	}



	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		ChainElem e1=binaryChain.getE1();
		Token tok=binaryChain.getArrow();
		Chain e0=binaryChain.getE0();
		//CHAIN
		visitChain(e0,arg);
		visitChainElem(e1,arg);

		switch(e0.getTypeName())
		{
		case FILE:
			if(tok.isKind(ARROW) && e1.getTypeName()==IMAGE)
				binaryChain.setTypeName(IMAGE);
			else throw new TypeCheckException("Expected Chainelem FILE but got  "+e1.getTypeName().toString());
			break;

		case FRAME:
			if(tok.isKind(ARROW) && (e1.getFirstToken().isKind(KW_XLOC)||e1.getFirstToken().isKind(KW_YLOC)))
				binaryChain.setTypeName(INTEGER);
			else if(tok.isKind(ARROW) && (e1.getFirstToken().isKind(KW_SHOW)||e1.getFirstToken().isKind(KW_MOVE)||e1.getFirstToken().isKind(KW_HIDE)))
				binaryChain.setTypeName(FRAME);
			else throw new TypeCheckException("Expected Chainelem FRAME but got "+e1.getTypeName().toString());
			break;

		case IMAGE:
			if(e1 instanceof ImageOpChain && tok.isKind(ARROW) && (e1.getFirstToken().isKind(OP_WIDTH)||e1.getFirstToken().isKind(OP_HEIGHT)))
				binaryChain.setTypeName(INTEGER);
			else if(e1.getTypeName()==FRAME && tok.isKind(ARROW))
				binaryChain.setTypeName(FRAME);
			else if(e1.getTypeName()==FILE && tok.isKind(ARROW))
				binaryChain.setTypeName(NONE);
			else if(e1 instanceof FilterOpChain && (tok.isKind(ARROW)||tok.isKind(BARARROW))&&(e1.getFirstToken().isKind(OP_GRAY)||e1.getFirstToken().isKind(OP_BLUR)||e1.getFirstToken().isKind(OP_CONVOLVE)))
				binaryChain.setTypeName(IMAGE);
			else if(e1 instanceof ImageOpChain && tok.isKind(ARROW) && e1.getFirstToken().isKind(KW_SCALE))
			binaryChain.setTypeName(IMAGE);

			else if(e1 instanceof IdentChain && e1.getTypeName()==INTEGER )//added
				binaryChain.setTypeName(IMAGE);

			else if(e1 instanceof IdentChain && tok.isKind(ARROW) && e1.getTypeName()==IMAGE) //ADDED
				binaryChain.setTypeName(IMAGE);
			else throw new TypeCheckException("Expected Chainelem IMAGE but got  "+e1.getTypeName().toString());
			break;

		case URL:
			if(tok.isKind(ARROW) && e1.getTypeName()==IMAGE)
			binaryChain.setTypeName(IMAGE);
			else throw new TypeCheckException("Expected Chainelem URL but got  "+e1.getTypeName().toString());
		break;

		case INTEGER:
			if(e1 instanceof IdentChain && e1.getTypeName()==INTEGER)//ADDED
			binaryChain.setTypeName(INTEGER);
			else throw new TypeCheckException("Expected Chainelem Integer but got  "+e1.getTypeName().toString());
		break;

		default:
			throw new TypeCheckException("Expected Chain typename but got  "+e0.getTypeName().toString());

		}
		return null;
	}

	SymbolTable symtab = new SymbolTable();

	private void visitChain(Chain e0, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if(e0 instanceof IdentChain)//chainelem
			visitIdentChain((IdentChain)e0 ,arg);
		else if(e0 instanceof FilterOpChain)//chainelem
			visitFilterOpChain((FilterOpChain)e0 ,arg);
		else if(e0 instanceof FrameOpChain )//chainelem
			visitFrameOpChain ((FrameOpChain )e0 ,arg);

		else if(e0 instanceof ImageOpChain)//chainelem
			visitImageOpChain((ImageOpChain)e0 ,arg);
		else if(e0 instanceof BinaryChain)//chain
			visitBinaryChain((BinaryChain)e0 ,arg);
		else throw new TypeCheckException("Expected Chain but got  "+e0.getTypeName());

	}

	private void visitChainElem(ChainElem e0, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if(e0 instanceof IdentChain)//chainelem
			visitIdentChain((IdentChain)e0 ,arg);
		else if(e0 instanceof FilterOpChain)//chainelem
			visitFilterOpChain((FilterOpChain)e0 ,arg);
		else if(e0 instanceof FrameOpChain )//chainelem
			visitFrameOpChain ((FrameOpChain )e0 ,arg);

		else if(e0 instanceof ImageOpChain)//chainelem
			visitImageOpChain((ImageOpChain)e0 ,arg);
		else throw new TypeCheckException("Expected Chainelem but got  "+e0.getTypeName());
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		for(Dec d:block.getDecs())
		{
			visitDec(d,arg);
		}
		for(Statement s:block.getStatements())
		{
		if(s instanceof SleepStatement )
			visitSleepStatement((SleepStatement)s ,arg);

		else if(s instanceof WhileStatement )
			visitWhileStatement((WhileStatement)s ,arg);

		else if(s instanceof IfStatement  )
			visitIfStatement((IfStatement)s ,arg);
		else if(s instanceof AssignmentStatement  )
			visitAssignmentStatement((AssignmentStatement)s ,arg);
		else if(s instanceof Chain)
			visitChain((Chain)s,arg);
//		else if(s instanceof IdentChain)//chainelem
//			visitIdentChain((IdentChain)s ,arg);
//		else if(s instanceof FilterOpChain)//chainelem
//			visitFilterOpChain((FilterOpChain)s ,arg);
//		else if(s instanceof FrameOpChain )//chainelem
//			visitFrameOpChain ((FrameOpChain )s ,arg);
//
//		else if(s instanceof ImageOpChain)//chainelem
//			visitImageOpChain((ImageOpChain)s ,arg);
//
//		else if(s instanceof BinaryChain)//chainelem
//			visitBinaryChain((BinaryChain)s ,arg);
		else throw new TypeCheckException("Expected Block statement");

		}
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		booleanLitExpression.setTypeName(BOOLEAN);
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Tuple tup=filterOpChain.getArg();
		visitTuple(tup,arg);

		if(tup.getExprList().size()!=0)
			throw new TypeCheckException("Expected filteropchain ");
		filterOpChain.setTypeName(IMAGE);
		return null;

	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Tuple tup=frameOpChain.getArg();
		visitTuple(tup,arg);

		if (frameOpChain.firstToken.isKind(KW_SHOW)||frameOpChain.firstToken.isKind(KW_HIDE)) {
			if(tup.getExprList().size()!=0)
				throw new TypeCheckException("Expected FrameOpchain ");
				frameOpChain.setTypeName(NONE);
		}

		else if (frameOpChain.firstToken.isKind(KW_XLOC)||frameOpChain.firstToken.isKind(KW_YLOC)) {
			if(tup.getExprList().size()!=0)
				throw new TypeCheckException("Expected filteropchain ");
				frameOpChain.setTypeName(INTEGER);
		}
		else if (frameOpChain.firstToken.isKind(KW_MOVE)) {
			if(tup.getExprList().size()!=2)
				throw new TypeCheckException("Expected filteropchain ");
				frameOpChain.setTypeName(NONE);
		}
		else throw new TypeCheckException("Expected FrameOpchain ");
		return null;

	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec d=symtab.lookup(identChain.getFirstToken().getText());
		if(d==null)
		throw new TypeCheckException("identchain not in current scope ");
		identChain.setTypeName(d.getTypeName());
		identChain.setDec(d);
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec d=symtab.lookup(identExpression.getFirstToken().getText());
		if(d==null)
		throw new TypeCheckException("identExpression not in current scope ");
		identExpression.setTypeName(d.getTypeName());
		identExpression.setDec(d);
		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e=ifStatement.getE();
		Block b=ifStatement.getB();
		visitExpression(e,arg);
		visitBlock(b ,arg);

		if(e.getType()==BOOLEAN)
			return null;
		else throw new TypeCheckException("Expected BOOLEAN but got"+ e.getType());
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		intLitExpression.setTypeName(INTEGER);
		return null;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e=sleepStatement.getE();
		visitExpression(e,arg);

			if(e.getType()==INTEGER)
				return null;
			else throw new TypeCheckException("Expected Integer but got"+ e.getType().toString());


	}

	private void visitExpression(Expression e,Object arg) throws Exception
	{
		if(e instanceof IdentExpression )
			visitIdentExpression((IdentExpression)e ,arg);
		else if(e instanceof IntLitExpression  )
			visitIntLitExpression ((IntLitExpression)e ,arg);
		else if(e instanceof BooleanLitExpression )
			visitBooleanLitExpression((BooleanLitExpression)e ,arg);
		else if(e instanceof ConstantExpression )
			visitConstantExpression ((ConstantExpression)e ,arg);
		else if (e instanceof BinaryExpression)
			visitBinaryExpression((BinaryExpression)e ,arg);
		else throw new TypeCheckException("Expected Expression");

	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e=whileStatement.getE();
		Block b=whileStatement.getB();
		visitExpression(e,arg);
		visitBlock(b ,arg);

		if(e.getType()==BOOLEAN)
			return null;
		else throw new TypeCheckException("Expected BOOLEAN but got"+ e.getType());
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		// TODO Auto-generated method stub
		declaration.setTypeName(Type.getTypeName(declaration.getFirstToken()));
		Boolean ins= symtab.insert(declaration.getIdent().getText(), declaration);

		if(!ins)
			throw new TypeCheckException("Found a duplicate entry");
		return null;
	}


	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		for(ParamDec p:program.getParams())
		{
			visitParamDec(p,arg);
		}
		visitBlock(program.getB(),arg);
		//System.out.println(symtab.toString());
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		IdentLValue i=assignStatement.getVar();
		Expression e=assignStatement.getE();
		visitIdentLValue(i,arg);
		visitExpression(e,arg);
		if(i.getDec().getTypeName()==e.getType())
			return null;
		else throw new TypeCheckException("Expected "+ e.getType().toString()+"and"+i.getDec().getTypeName().toString());
		}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Dec d=symtab.lookup(identX.getText());
		if(d==null)
		throw new TypeCheckException("IdentLValue not in current scope ");
		identX.setDec(d);
		return null;
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		paramDec.setTypeName(Type.getTypeName(paramDec.getFirstToken()));
		Boolean ins= symtab.insert(paramDec.getIdent().getText(), paramDec);
		if(!ins)
			throw new TypeCheckException("Found a duplicate entry in paramdec");
		return null;

	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		constantExpression.setTypeName(INTEGER);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		visitTuple(imageOpChain.getArg(),arg);
		Token tok=imageOpChain.getFirstToken();
		Tuple tup=imageOpChain.getArg();
        if (tok.isKind(OP_WIDTH)||tok.isKind(OP_HEIGHT)){
        	//condition:  Tuple.length == 0
    		 if(tup.getExprList().size()!=0)
 				throw new TypeCheckException("Expected ImageOpChain Tuple size to be 0 ");
    		 imageOpChain.setTypeName(INTEGER);
        }
        else if (tok.isKind(KW_SCALE)){
        	  //condition: Tuple.length==1
    		  if(tup.getExprList().size()!=1)
   				throw new TypeCheckException("Expected ImageOpChain Tuple size to be 1 ");
    		  imageOpChain.setTypeName(IMAGE);//ImageOpChain.type <- IMAGE
       }

        else throw new TypeCheckException("Expected ImageOpChain");


		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		// TODO Auto-generated method stub
		for(Expression e:tuple.getExprList())
		{
			visitExpression(e,arg);
			if(e.getType()!=INTEGER)
   				throw new TypeCheckException("Expected Tuple type to be INTEGER but was "+e.getType());
		}
		return null;
	}


}
