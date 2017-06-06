package cop5556sp17.AST;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.Type.TypeName;

public abstract class Expression extends ASTNode {
	TypeName t;
	protected Expression(Token firstToken) {
		super(firstToken);
				
	}
	public TypeName getType() {
		return t;
	}

	public void setTypeName(TypeName t) {
		this.t = t;
	}

	@Override
	abstract public Object visit(ASTVisitor v, Object arg) throws Exception;

}
