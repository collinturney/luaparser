package com.luacpp.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Strings;
import com.luacpp.parser.LuaParser;

public class ASTNode implements Iterable<ASTNode> {
	
	private Integer type = null;
	private String data = null;
	private String typeStr = null;
	private Integer depth = null;
	private List<ASTNode> children = new ArrayList<ASTNode>();
	
	public ASTNode(Integer type, String data) {
		this.type = type;
		this.typeStr = LuaParser.ruleNames[type];
		this.data = data;
		this.depth = 1;
	}
	
	public ASTNode(Integer type, String data, Integer depth) {
		this.type = type;
		this.typeStr = LuaParser.ruleNames[type];
		this.data = data;
		this.depth = depth;
	}
	
	@Override
	public Iterator<ASTNode> iterator() {
		return this.children.iterator();
	}
	
	@Override
	public String toString() {
    	String indent = Strings.repeat(" ", (this.depth - 1) * 2);
    	
    	return String.format("%s%s [%s]\n",
    			indent,
    			LuaParser.ruleNames[this.type],
    			this.data);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	
	public List<ASTNode> getChildren() {
		return children;
	}
	
	public ASTNode getChild(Integer index) {
		return children.get(index);
	}
	
	public void setChild(Integer index, ASTNode node) {
		children.set(index, node);
	}
	
	public void addChild(ASTNode node) {
		children.add(node);
	}
	
	public Integer numChildren() {
		return children.size();
	}
}
