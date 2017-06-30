package com.luacpp.generator;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;

import com.luacpp.parser.LuaLexer;
import com.luacpp.parser.LuaParser;
import com.luacpp.parser.LuaParser.ChunkContext;

public class GeneratorTest {

	@Test
	public void testParser() throws ASTException, IOException {
        CharStream charStream = CharStreams.fromFileName("src/main/resources/ifelse.lua");
        
        LuaLexer lexer = new LuaLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        LuaParser parser = new LuaParser(tokens);
        
        ChunkContext rootContext = (ChunkContext) parser.chunk();
        ASTNode rootNode = new ASTNode(rootContext.getRuleIndex(), rootContext.getText());

        ASTUtils.buildAST(rootContext, rootNode);
        
        ASTUtils.printAST(rootNode, 1);
        
        CodeGenerator codeGen = new CodeGenerator();
        
        codeGen.printHeader();
        codeGen.printDeclarations(rootNode);
        codeGen.printChunk(rootNode);
        codeGen.printFooter();
	}
}