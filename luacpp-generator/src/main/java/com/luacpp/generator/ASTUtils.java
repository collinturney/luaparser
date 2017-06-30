package com.luacpp.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.luacpp.parser.LuaParser;

public class ASTUtils {
	
    public static void printAST(ASTNode node, Integer depth) {
    	System.out.print(node.toString());
    	
		for (ASTNode child : node) {
    		printAST(child, depth + 1);
    	}
    }
    
    public static void buildAST(RuleContext context, ASTNode parent) {
    	for (int i=0; i < context.getChildCount(); ++i) {
    		ParseTree element = context.getChild(i);
    		
    		if (element instanceof RuleContext) {
    			RuleContext rule = (RuleContext) element;
    			ASTNode child = new ASTNode(rule.getRuleIndex(), rule.getText(), rule.depth());
    			parent.addChild(child);
    			
    			buildAST(rule, child);
    		}
    	}
    }
    
    public static void getVariables(ASTNode node, HashSet<String> vars) {
    	for (ASTNode child : node) {
    		if (child.getType() == LuaParser.RULE_var) {
    			vars.add(child.getData());
    		}
    		
    		getVariables(child, vars);
    	}
    }
	
    public static boolean isNumber(ASTNode node) {
    	if (node.getType() == LuaParser.RULE_number) {
    		return true;
    	}
    	else if (node.getType() == LuaParser.RULE_exp) {
    		if (node.numChildren() == 1 && node.getChild(0).getType() == LuaParser.RULE_number) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    public static boolean isBinaryOp(ASTNode node) {
    	if (node.numChildren() == 3) {
    		switch (node.getChild(1).getType()) {
    			case LuaParser.RULE_operatorAddSub:
    			case LuaParser.RULE_operatorAnd:
    			case LuaParser.RULE_operatorBitwise:
    			case LuaParser.RULE_operatorComparison:
    			case LuaParser.RULE_operatorMulDivMod:
    			case LuaParser.RULE_operatorOr:
    			case LuaParser.RULE_operatorPower:
    				return true;
    			default:
    				return false;
    		}
    	}

    	return false;
    }
    
    public static boolean isIfStmt(ASTNode node) {
    	if (node.getChild(0).getType() == LuaParser.RULE_exp &&
    		node.getChild(1).getType() == LuaParser.RULE_block &&
    		node.getData().startsWith("if")) {
    		
    			return true;
    	}
    	
    	return false;
    }
    
    public static boolean isAssignmentStmt(ASTNode node) {
    	if (node.numChildren() == 2 &&
    		node.getChild(0).getType() == LuaParser.RULE_varlist &&
    		node.getChild(1).getType() == LuaParser.RULE_explist) {
    		
    		return true;
    	}
    	
    	return false;
    }
}
