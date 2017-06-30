package com.luacpp.generator;

public class ASTException extends Exception {
	public ASTException(String message) {
		super(message);
	}
	
	public ASTException(String function, ASTNode node) {
		super(String.format("Invalid node type found in %s : %s (%s)",
				function, node.getTypeStr(), node.getData()));
	}
}
