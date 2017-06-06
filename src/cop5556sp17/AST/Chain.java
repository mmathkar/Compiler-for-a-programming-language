package cop5556sp17.AST;

import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.Type.TypeName;


public abstract class Chain extends Statement {
	TypeName t;
	public Chain(Token firstToken) {
		super(firstToken);
	}
	public TypeName getTypeName() {
		return t;
	}

	public void setTypeName(TypeName t) {
		this.t = t;
	}

	
}
