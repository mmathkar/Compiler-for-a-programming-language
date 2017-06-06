package cop5556sp17;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.TraceClassVisitor;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.ASTVisitor;
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
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

import static cop5556sp17.AST.Type.TypeName.FRAME;
import static cop5556sp17.AST.Type.TypeName.IMAGE;
import static cop5556sp17.AST.Type.TypeName.URL;
import static cop5556sp17.Scanner.Kind.*;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
		this.args_index=0;
		this.slot_no=1;//check
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;
	int slot_no;
	int args_index;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.getName();
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(51, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object",
				new String[] { "java/lang/Runnable" });
		cw.visitSource(sourceFileName, null);

		// generate constructor code
		// get a MethodVisitor
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([Ljava/lang/String;)V", null,
				null);
		mv.visitCode();

		// Create label at start of code
		Label constructorStart = new Label();
		mv.visitLabel(constructorStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering <init>");
		// generate code to call superclass constructor
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		// visit parameter decs to add each as field to the class
		// pass in mv so decs can add their initialization code to the
		// constructor.
		ArrayList<ParamDec> params = program.getParams();
		for (ParamDec dec : params)
			dec.visit(this, mv);
		mv.visitInsn(RETURN);
		// create label at end of code
		Label constructorEnd = new Label();
		mv.visitLabel(constructorEnd);
		// finish up by visiting local vars of constructor
		// the fourth and fifth arguments are the region of code where the local
		// variable is defined as represented by the labels we inserted.
		mv.visitLocalVariable("this", classDesc, null, constructorStart, constructorEnd, 0);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, constructorStart, constructorEnd, 1);
		// indicates the max stack size for the method.
		// because we used the COMPUTE_FRAMES parameter in the classwriter
		// constructor, asm
		// will do this for us. The parameters to visitMaxs don't matter, but
		// the method must
		// be called.
		mv.visitMaxs(1, 1);
		// finish up code generation for this method.
		mv.visitEnd();
		// end of constructor

		// create main method which does the following
		// 1. instantiate an instance of the class being generated, passing the
		// String[] with command line arguments
		// 2. invoke the run method.

		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null,
				null);
		mv.visitCode();
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		// this is for convenience during development--you can see that the code
		// is doing something.
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering main");
		mv.visitTypeInsn(NEW, className);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "([Ljava/lang/String;)V", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "run", "()V", false);
		mv.visitInsn(RETURN);
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
		mv.visitLocalVariable("instance", classDesc, null, mainStart, mainEnd, 1);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// create run method
		mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		mv.visitCode();
		Label startRun = new Label();
		mv.visitLabel(startRun);
		CodeGenUtils.genPrint(DEVEL, mv, "\nentering run");
		program.getB().visit(this, null);
		mv.visitInsn(RETURN);
		Label endRun = new Label();
		mv.visitLabel(endRun);


		//TODO  visit the local variables
		mv.visitLocalVariable("this", classDesc, null, startRun, endRun, 0);
		//visit cmd arg and object
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, startRun, endRun, 1);
		mv.visitMaxs(1, 1);
		mv.visitEnd(); // end of run method

		cw.visitEnd();//end of class
		//generate classfile and return it
		return cw.toByteArray();
	}



	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {

		assignStatement.getE().visit(this, arg);
		CodeGenUtils.genPrint(DEVEL, mv, "\nassignment: " + assignStatement.var.getText() + "=");
		CodeGenUtils.genPrintTOS(GRADE, mv, assignStatement.getE().getType());

		assignStatement.getVar().visit(this, arg);
		return null;
	}

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		//assert false : "not yet implemented";
		binaryChain.getE0().visit(this, 0);//arg 0 for left side
		if(binaryChain.getE0().getTypeName()==TypeName.URL)
		{
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromURL",PLPRuntimeImageIO.readFromURLSig, false);//top of stack wala part
		}
		else if(binaryChain.getE0().getTypeName()==TypeName.FILE)
		{
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className,"readFromFile",PLPRuntimeImageIO.readFromFileDesc,false);

		}
		mv.visitInsn(DUP);//duplicate
		binaryChain.getE1().visit(this, 1);//arg 1 for right side
		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
      //TODO  Implement this
		binaryExpression.getE0().visit(this,null);//pushes E1,E0 on top of stack
		binaryExpression.getE1().visit(this,null);
		Label trueLabel=new Label();
		Label EndOperation=new Label();

		if(binaryExpression.getE0().getType()==TypeName.IMAGE && binaryExpression.getE1().getType()==TypeName.IMAGE)
			{
			if(binaryExpression.getOp().isKind(PLUS))
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "add",PLPRuntimeImageOps.addSig,false);

			else if(binaryExpression.getOp().isKind(MINUS))
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "sub",PLPRuntimeImageOps.subSig,false);
			}

		else if(binaryExpression.getE0().getType()==TypeName.IMAGE && binaryExpression.getE1().getType()==TypeName.INTEGER)
			{
			 if(binaryExpression.getOp().isKind(TIMES))
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mul",PLPRuntimeImageOps.mulSig,false);

			 else if(binaryExpression.getOp().isKind(MOD))
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mod",PLPRuntimeImageOps.modSig,false);

			 else if(binaryExpression.getOp().isKind(DIV))
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "div",PLPRuntimeImageOps.divSig,false);
			}

		else if(binaryExpression.getE0().getType()==TypeName.INTEGER && binaryExpression.getE1().getType()==TypeName.IMAGE)
		{
		 if(binaryExpression.getOp().isKind(TIMES))
		 {
			mv.visitInsn(SWAP);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "mul",PLPRuntimeImageOps.mulSig,false);
		 }

		}

		else if(binaryExpression.getOp().isKind(PLUS))
			mv.visitInsn(IADD);
		else if(binaryExpression.getOp().isKind(MINUS))
			mv.visitInsn(ISUB);
		else if(binaryExpression.getOp().isKind(TIMES))
			mv.visitInsn(IMUL);
		else if(binaryExpression.getOp().isKind(DIV))
			mv.visitInsn(IDIV);
		else if(binaryExpression.getOp().isKind(OR))
			mv.visitInsn(IXOR);
		else if(binaryExpression.getOp().isKind(MOD))//CHECK
			mv.visitInsn(IREM);
		else if(binaryExpression.getOp().isKind(AND))
			mv.visitInsn(IAND);
		else if(binaryExpression.getOp().isKind(EQUAL))
			{
			mv.visitJumpInsn(IF_ICMPEQ,trueLabel);
			mv.visitLdcInsn(false);
			}
		else if(binaryExpression.getOp().isKind(NOTEQUAL))
			{mv.visitJumpInsn(IF_ICMPNE,trueLabel);
		   	 mv.visitLdcInsn(false);
		   	 }
		else if(binaryExpression.getOp().isKind(LT))
			{mv.visitJumpInsn(IF_ICMPLT,trueLabel);
			 mv.visitLdcInsn(false);
			}
		else if(binaryExpression.getOp().isKind(LE))
			{mv.visitJumpInsn(IF_ICMPLE,trueLabel);
			 mv.visitLdcInsn(false);
			}
		else if(binaryExpression.getOp().isKind(GE))
			{mv.visitJumpInsn(IF_ICMPGE,trueLabel);
			 mv.visitLdcInsn(false);
			}
		else if(binaryExpression.getOp().isKind(GT))
			{mv.visitJumpInsn(IF_ICMPGT,trueLabel);
			 mv.visitLdcInsn(false);
			 }
		mv.visitJumpInsn(GOTO,EndOperation);
		mv.visitLabel(trueLabel);
		mv.visitLdcInsn(true);
		mv.visitLabel(EndOperation);

		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		//TODO  Implement this
		//Decs are local variables in current scope of run method
		//Statements are executed in run method
		// Create label at start of code
		Label startBlock = new Label();
		mv.visitLabel(startBlock);
		Label endBlock = new Label();
		// this is for convenience during development--you can see that the code is doing something.
		ArrayList<Dec> decs = block.getDecs();
		for (Dec dec : decs)
				dec.visit(this, mv);

			ArrayList<Statement> statements = block.getStatements();
		for (Statement statement : statements)
		{
			if(statement instanceof AssignmentStatement)//check this
			{
				if(((AssignmentStatement)statement).getVar().getDec() instanceof ParamDec)//if the field variable is assigned in the method
					mv.visitIntInsn(ALOAD,0);
			}

			statement.visit(this, mv);

			if(statement instanceof BinaryChain)
			{
				mv.visitInsn(POP);//pop if it's a binary chain
			}
		}
		mv.visitLabel(endBlock);

		for (Dec dec: decs)//visit local dec variables
		{
			mv.visitLocalVariable(dec.getIdent().getText(), dec.getTypeName().getJVMTypeDesc(), null, startBlock, endBlock, dec.getSlotNo());
		}

		return null;
		}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		//TODO Implement this
		mv.visitLdcInsn(booleanLitExpression.getValue());
		return null;
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		//assert false : "not yet implemented";
		if(constantExpression.getFirstToken().isKind(KW_SCREENHEIGHT))
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenHeight",PLPRuntimeFrame.getScreenHeightSig,false);
		else
			if(constantExpression.getFirstToken().isKind(KW_SCREENWIDTH))
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenWidth",PLPRuntimeFrame.getScreenWidthSig,false);
		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		//TODO Implement this
		declaration.setSlotNo(slot_no++);//increment slot no
		if( declaration.getTypeName().equals(IMAGE)||declaration.getTypeName().equals(FRAME) )
		{
            mv.visitInsn(ACONST_NULL);      //Pushes the special null object reference onto the stack.
            mv.visitVarInsn(ASTORE, declaration.getSlotNo());
        }
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		//assert false : "not yet implemented";
		mv.visitInsn(POP);
        mv.visitInsn(ACONST_NULL);

		switch(filterOpChain.getFirstToken().kind)
		{
		case OP_GRAY:
			mv.visitInsn(POP);
	        mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "grayOp",PLPRuntimeFilterOps.opSig,false);
			break;
		case OP_CONVOLVE:
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "convolveOp",PLPRuntimeFilterOps.opSig,false);
			break;
		case OP_BLUR:
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, "blurOp",PLPRuntimeFilterOps.opSig,false);
			break;
		default:
			break;

		}

		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		//assert false : "not yet implemented";
		frameOpChain.getArg().visit(this, arg);

		if(frameOpChain.getFirstToken().isKind(KW_HIDE))

			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "hideImage",PLPRuntimeFrame.hideImageDesc,false);

		else if(frameOpChain.getFirstToken().isKind(KW_MOVE))

			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "moveFrame",PLPRuntimeFrame.moveFrameDesc,false);

		else if(frameOpChain.getFirstToken().isKind(KW_SHOW))

			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "showImage",PLPRuntimeFrame.showImageDesc,false);

		else if(frameOpChain.getFirstToken().isKind(KW_XLOC))

			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "getXVal",PLPRuntimeFrame.getXValDesc,false);

		else if(frameOpChain.getFirstToken().isKind(KW_YLOC))

			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, "getYVal",PLPRuntimeFrame.getYValDesc,false);

		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		//assert false : "not yet implemented";
		// TODO: //left
		if((Integer)arg==0)//if ident chain is on left
		{
			if(identChain.getDec() instanceof ParamDec){

				mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, className, identChain.getDec().getIdent().getText(),identChain.getDec().getTypeName().getJVMTypeDesc()); //call getfield-load the value in the field into the referenced obj
			}
			else{
				if(identChain.getTypeName()==TypeName.INTEGER)
					mv.visitVarInsn(ILOAD, identChain.getDec().getSlotNo());
				else
					mv.visitVarInsn(ALOAD, identChain.getDec().getSlotNo());
			}
		}

		else{//if right
			if(identChain.getDec() instanceof ParamDec)
				{
				if(identChain.getTypeName()==TypeName.FILE)
				{
					mv.visitInsn(POP);
					mv.visitVarInsn(ALOAD,0);
					mv.visitFieldInsn(GETFIELD, className, identChain.getDec().getIdent().getText(),identChain.getDec().getTypeName().getJVMTypeDesc());
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write",PLPRuntimeImageIO.writeImageDesc,false);
				}
				else{
					mv.visitVarInsn(ALOAD, 0);//load obj reference onto the stack
					mv.visitInsn(SWAP);
					mv.visitFieldInsn(PUTFIELD, className, identChain.getDec().getIdent().getText(), identChain.getDec().getTypeName().getJVMTypeDesc());
				}
				}
			else//not a paramdec on right
			{
				String identDesc=identChain.getDec().getIdent().getText();
				String identType=identChain.getDec().getTypeName().getJVMTypeDesc();

				if(identChain.getDec().getTypeName()==TypeName.FRAME)
				{
					mv.visitInsn(POP);
					mv.visitVarInsn(ALOAD, identChain.getDec().getSlotNo());//load onto stack from slot no
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "createOrSetFrame",PLPRuntimeFrame.createOrSetFrameSig,false);
					mv.visitInsn(DUP);//duplicate result
					mv.visitVarInsn(ASTORE, identChain.getDec().getSlotNo());//store result into the slot number
				}
				else if(identChain.getDec().getTypeName()==TypeName.FILE)//for both paramdec and dec
				{
					mv.visitInsn(POP);
					mv.visitVarInsn(ALOAD,0);
					mv.visitFieldInsn(GETFIELD, className, identDesc,identType);
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write",PLPRuntimeImageIO.writeImageDesc,false);
				}
				else if(identChain.getDec().getTypeName()==TypeName.INTEGER)
					mv.visitVarInsn(ISTORE, identChain.getDec().getSlotNo());
				else  if(identChain.getDec().getTypeName()==TypeName.IMAGE)
					{
						mv.visitVarInsn(ASTORE, identChain.getDec().getSlotNo());
					}
			}
		}
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		//TODO Implement this CHECK
		String identExpName=identExpression.getDec().getIdent().getText();
		if(identExpression.getDec() instanceof ParamDec)
		{
			mv.visitVarInsn(ALOAD,0);
			mv.visitFieldInsn(GETFIELD,className,identExpName,identExpression.getDec().getTypeName().getJVMTypeDesc());
		}
		else
		{
			if(identExpression.getDec().getTypeName()==TypeName.BOOLEAN||identExpression.getDec().getTypeName()==TypeName.INTEGER)
			mv.visitVarInsn(ILOAD, identExpression.getDec().getSlotNo());
			else
			mv.visitVarInsn(ALOAD, identExpression.getDec().getSlotNo());
		}
		return null;
	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		//TODO Implement this
//check
		TypeName tName = identX.getDec().getTypeName();
		if(identX.getDec() instanceof ParamDec)
		{
			mv.visitFieldInsn(PUTFIELD, className, identX.getFirstToken().getText(), identX.getDec().getTypeName().getJVMTypeDesc());
		}

		else
			{
			if(identX.getDec().getTypeName()==TypeName.IMAGE) //added
				{
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "copyImage",PLPRuntimeImageOps.copyImageSig,false);
				mv.visitVarInsn(ASTORE, identX.getDec().getSlotNo());//added-check
				}
			else if(identX.getDec().getTypeName()==TypeName.INTEGER||identX.getDec().getTypeName()==TypeName.BOOLEAN)

				mv.visitVarInsn(ISTORE, identX.getDec().getSlotNo());

			else mv.visitVarInsn(ASTORE, identX.getDec().getSlotNo());
			}

		return null;
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		//TODO Implement this
		Label AFTER = new Label();
		ifStatement.getE().visit(this,mv);
		mv.visitJumpInsn(IFEQ, AFTER);
		ifStatement.getB().visit(this, mv);
		mv.visitLabel(AFTER);
		return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		//assert false : "not yet implemented";
		imageOpChain.getArg().visit(this, arg);//visit tuples
		if(imageOpChain.getFirstToken().isKind(KW_SCALE))
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "scale",PLPRuntimeImageOps.scaleSig, false);
		else if(imageOpChain.getFirstToken().isKind(OP_WIDTH))
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeImageIO.BufferedImageClassName, "getWidth",PLPRuntimeImageOps.getWidthSig,false);
		else if(imageOpChain.getFirstToken().isKind(OP_HEIGHT))
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeImageIO.BufferedImageClassName, "getHeight",PLPRuntimeImageOps.getHeightSig, false);
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		//TODO Implement this
		mv.visitLdcInsn(intLitExpression.value);
		return null;
	}


	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
//		TODO Implement this
//		For assignment 5, only needs to handle integers and booleans
		FieldVisitor fv = cw.visitField(ACC_PUBLIC,paramDec.getIdent().getText(), paramDec.getTypeName().getJVMTypeDesc(), null, null);
        fv.visitEnd();
        mv.visitIntInsn(ALOAD,0);//load object ref

        if(paramDec.getTypeName()==TypeName.BOOLEAN)
		{
			mv.visitIntInsn(ALOAD,1);	 //string array args
			mv.visitLdcInsn(args_index++);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
            mv.visitFieldInsn(PUTFIELD,className,paramDec.getIdent().getText(),"Z");
		}
        else if(paramDec.getTypeName()==TypeName.INTEGER)
		{
			mv.visitIntInsn(ALOAD,1);	 //string array args
			mv.visitLdcInsn(args_index++);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I",false);
			mv.visitFieldInsn(PUTFIELD,className,paramDec.getIdent().getText(),"I");

		}
		else if(paramDec.getTypeName()==TypeName.URL)
		{
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn(args_index++);
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "getURL", PLPRuntimeImageIO.getURLSig, false);
			mv.visitFieldInsn(PUTFIELD,className,paramDec.getIdent().getText(),paramDec.getTypeName().getJVMTypeDesc());
		}
		else if(paramDec.getTypeName().equals(TypeName.FILE))
		{
			mv.visitTypeInsn(NEW, "java/io/File");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 1);	mv.visitLdcInsn(args_index++);
			mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitFieldInsn(PUTFIELD,className,paramDec.getIdent().getText(),paramDec.getTypeName().getJVMTypeDesc());
		}
		return null;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		sleepStatement.getE().visit(this, arg);//needed??check*****
		mv.visitInsn(I2L);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V",false);
		//assert false : "not yet implemented";
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		//assert false : "not yet implemented";
		List<Expression> exp=tuple.getExprList();
		for (Expression tup: exp)
		{
			tup.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		//TODO Implement this
		Label GUARD = new Label();
		Label BODY = new Label();
		mv.visitJumpInsn(GOTO, GUARD);
		mv.visitLabel(BODY);
		whileStatement.getB().visit(this, mv);
		mv.visitLabel(GUARD);
		whileStatement.getE().visit(this,mv);
		mv.visitJumpInsn(IFNE, BODY);

		return null;
	}

}
