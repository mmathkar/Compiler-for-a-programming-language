package cop5556sp17;



import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import cop5556sp17.AST.Dec;


public class SymbolTable {
	
	
	//TODO  add fields
	int  current_scope, next_scope;
	Stack<Integer> scope_stack=new Stack<Integer>();
	HashMap<String,LinkedList<SymTableEntry>> map=new HashMap<>();
	public class SymTableEntry
	{
		int scope;
		Dec dec;
		public SymTableEntry(int scope,Dec dec)
		{
			this.scope=scope;
			this.dec=dec;
		}
		
	}

	/** 
	 * to be called when block entered
	 */
	public void enterScope(){
		//TODO:  IMPLEMENT THIS
		current_scope = next_scope++; 
		scope_stack.push(current_scope);
	}
		
	/**
	 * leaves scope
	 */
	public void leaveScope(){
		//TODO:  IMPLEMENT THIS
		current_scope = scope_stack.pop();
	}
	
	@Override
	public String toString() {
		//TODO:  IMPLEMENT THIS
		Set<Entry<String,LinkedList<SymTableEntry>>> symbolTable=map.entrySet();
		StringBuffer sb=new StringBuffer();
		for(Entry<String,LinkedList<SymTableEntry>> m:symbolTable)
		{
			sb.append(m.getKey()+" :");
						
			for(SymTableEntry s:m.getValue())
			{
				sb.append("Scope: "+s.scope+" Type:"+s.dec.getFirstToken().getText()+" Ident "+s.dec.getIdent().getText());
			}
				
		}
		return sb.toString();
	}

	public boolean insert(String ident, Dec dec){
		//TODO:  IMPLEMENT THIS
		if(map.containsKey(ident))
		{
			LinkedList<SymTableEntry> oldList=map.get(ident);
			for(SymTableEntry s:oldList)
				{
				if(current_scope==s.scope)
				return false;
				}
				oldList.addFirst(new SymTableEntry(current_scope,dec));
				map.put(ident, oldList);			
			
		}
		else
		{
			LinkedList<SymTableEntry> list=new LinkedList<>();
			list.addFirst(new SymTableEntry(current_scope,dec));
			map.put(ident, list);
			//System.out.println("new elem"+ident);
		}
		
		return true;
	}
	
	public Dec lookup(String ident){
		//TODO:  IMPLEMENT THIS
		LinkedList<SymTableEntry> matchList=map.get(ident);
		if(matchList!=null) 
		{Iterator<SymTableEntry> iter = matchList.iterator();
        
		while (iter.hasNext()){
			SymTableEntry sym=iter.next();
			if(scope_stack.contains(sym.scope))
				return sym.dec;
		}
		}
		return null;
	}
		
	public SymbolTable() {
		//TODO:  IMPLEMENT THIS
		next_scope=0;
		current_scope=0;
	}
	
	


}
