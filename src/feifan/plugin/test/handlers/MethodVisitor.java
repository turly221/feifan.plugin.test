package feifan.plugin.test.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;

public class MethodVisitor extends GenericListVisitorAdapter<HashMap<String, String>, Void> {
	public List<HashMap<String, String>> visit(MethodDeclaration n, Void arg) {

		List<HashMap<String, String>> result = new ArrayList<>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("methodName", n.getDeclarationAsString(false, false));
		map.put("startLine", String.valueOf(n.getBegin().get().line));
		map.put("endLine", String.valueOf(n.getEnd().get().line));
		super.visit(n, arg);
		result.add(map);
		return result;
	}
}