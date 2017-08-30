package feifan.plugin.test.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;

class MethodVisitor extends GenericListVisitorAdapter<HashMap<String, String>, Void> {
	public List<HashMap<String, String>> visit(MethodDeclaration n, Void arg) {
		final Logger logger = LoggerFactory.getLogger(MethodVisitor.class);

		/*
		 * here you can access the attributes of the method. this method will be called
		 * for all methods in this CompilationUnit, including inner class methods
		 */
		List<HashMap<String, String>> result = new ArrayList<>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("methodName", n.getDeclarationAsString(false, false));
		map.put("startLine", String.valueOf(n.getBegin().get().line));
		map.put("endLine", String.valueOf(n.getEnd().get().line));
		logger.info(map.get("methodName"));
		super.visit(n, arg);
		result.add(map);
		return result;
	}
}