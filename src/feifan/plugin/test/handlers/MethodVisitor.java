package feifan.plugin.test.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

class MethodVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration n, Void arg) {
    	final Logger logger = LoggerFactory.getLogger(MethodVisitor.class);

        /* here you can access the attributes of the method.
         this method will be called for all methods in this 
         CompilationUnit, including inner class methods */
        logger.info("    " + n.getDeclarationAsString(false, false));
        logger.info("    starting line: " + n.getBegin().get().line + " ending line: " + n.getEnd().get().line);
        super.visit(n, arg);
    }
}