package feifan.plugin.test.navigator;

import org.eclipse.core.resources.IFile;

public class MethodsTreeData {
	private IFile container; 

	private String methodName;
	private String value;
	
	public MethodsTreeData() {
	}

	public MethodsTreeData(IFile container, String method, String value) {
		this.container = container;
		this.methodName = method;
		this.value = value;
	}


	public IFile getContainer() {
		return container;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getValue() {
		return value;
	}

	public int hashCode() {
		return methodName.hashCode();
	}

	public boolean equals(Object obj) {
		return obj instanceof MethodsTreeData && ((MethodsTreeData) obj).getContainer().equals(container);
	}

	public String toString() {
		StringBuffer toString = new StringBuffer(getMethodName());
		return toString.toString();
	}
}
