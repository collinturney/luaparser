package com.luacpp.generator;

import java.util.HashSet;

import com.google.common.base.Strings;
import com.luacpp.parser.LuaParser;

public class CodeGenerator {
	
	private Integer indent = 0;
	
	public void printHeader() {
		System.out.print(String.join("\n",
				"#include <stdio.h>",
				"",
				"int main() {",
				""));
		
		indent();
	}
	
	public void printFooter() {
		System.out.print(String.join("\n",
				"}"));
		
		unindent();
	}
	
	public void printDeclarations(ASTNode rootNode) {
		HashSet<String> vars = new HashSet<String>();
		
		ASTUtils.getVariables(rootNode, vars);
		
		System.out.print("\n");
		
		for (String var : vars) {
			printIndentation();
			System.out.printf("int %s = 0;\n", var);
		}

		System.out.print("\n");
	}

    public void printChunk(ASTNode node) throws ASTException {
    	ASTNode block = node.getChild(0);
    	
    	if (block.getType() == LuaParser.RULE_block) {
    		printBlock(block);
    	}
    	else {
    		throw new ASTException("printChunk()", block);
    	}
    }
    
    public void printBlock(ASTNode block) throws ASTException {
    	for (ASTNode stmt : block) {
    		printIndentation();
    		
    		if (stmt.getType() == LuaParser.RULE_stat) {
    			printStmt(stmt);
    		}
    		else if (stmt.getType() == LuaParser.RULE_retstat) {
    			printReturnStmt(stmt);
    		}
        	else {
        		throw new ASTException("printBlock()", stmt);
        	}
    	}
    }
    
    public void printStmt(ASTNode stmt) throws ASTException {
    	if (stmt.getType() == LuaParser.RULE_functioncall) {
    		printFunction(stmt);
    	}
		else if (ASTUtils.isIfStmt(stmt)) {
			printIfStmt(stmt);
		}
		else if (ASTUtils.isAssignmentStmt(stmt)) {
			printAssignmentStmt(stmt);
		}
		else if (stmt.getType() == LuaParser.RULE_stat) {
			unindent(); // do nothing
		}
		else {
			throw new ASTException("printStmt()", stmt);
		}
    }
    
    public void printReturnStmt(ASTNode stmt) throws ASTException {
    	System.out.printf("return %s;\n", stmt.getChild(0).getData());
    }
    
    public void printAssignmentStmt(ASTNode node) {
		ASTNode varlist = node.getChild(0);
		ASTNode explist = node.getChild(1);
		
		System.out.printf("%s = %s;\n", varlist.getChild(0).getData(),
				                        explist.getChild(0).getData());
	}

	public void printIfStmt(ASTNode ifStmt) throws ASTException {
    	
    	boolean firstConditional = true;
    	boolean lastBlock = false;
    	
    	for (ASTNode node : ifStmt) {
    		if (node.getType() == LuaParser.RULE_exp) {
    			lastBlock = false;
    			
    			if (firstConditional) {
        			System.out.print("if (");
        			firstConditional = false;
    			}
    			else {
    				printIndentation();
    				System.out.print("else if (");
    			}
    			
    			printExpression(node);
    			System.out.print(") {\n");
    		}
    		else if (node.getType() == LuaParser.RULE_block) {
    			if (lastBlock) {
    				printIndentation();
    				System.out.print("else {\n");
    			}
    			
    			indent();
    			printBlock(node);
    			unindent();
    			
    	    	printIndentation();
    	    	System.out.print("}\n");
    			
    			lastBlock = true;
    		}
    		else {
    			throw new ASTException("printIfStmt()", node);
    		}
    	}
    }
    
    public void printFunction(ASTNode funcCall) {
    	ASTNode funcName = funcCall.getChild(0);
    	ASTNode funcParams = funcCall.getChild(1);
    	
    	System.out.printf("%s(", funcName.getData());
    	printExpression(funcParams);
    }
    
    public void printExpression(ASTNode exp) {
    	if (ASTUtils.isBinaryOp(exp)) {
    		System.out.print("(");
    		printExpression(exp.getChild(0));
    		System.out.printf(" %s ", exp.getChild(1).getData());
    		printExpression(exp.getChild(2));
    		System.out.print(")");
    	}
    	else if (ASTUtils.isNumber(exp)) {
    		System.out.print(exp.getData());
    	}
    	else if (exp.getType() == LuaParser.RULE_var) {
    		System.out.print(exp.getData());
    	}
    	else {
    		for (ASTNode child : exp) {
    			printExpression(child);
    		}
    	}
    }
    
	public void indent() {
		this.indent++;
	}
	
	public void unindent() {
		this.indent--;
	}
	
	public void printIndentation() {
		System.out.print(Strings.repeat(" ", this.indent * 2));
	}
}
