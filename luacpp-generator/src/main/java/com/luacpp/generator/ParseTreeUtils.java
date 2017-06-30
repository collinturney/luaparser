package com.luacpp.generator;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.google.common.base.Strings;
import com.luacpp.parser.LuaParser;

public class ParseTreeUtils {

    public static void exploreContext(RuleContext context) {
        String ruleName = LuaParser.ruleNames[context.getRuleIndex()];
        
        System.out.print(Strings.repeat(" ", (context.depth() - 1) * 2));
        System.out.println(ruleName);
                
        for (int i=0; i < context.getChildCount(); ++i) {
            ParseTree element = context.getChild(i);
            
            if (element instanceof RuleContext) {
                exploreContext((RuleContext) element);
            }
        }
    }
}
