package feifan.plugin.test.navigator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.UIJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import feifan.plugin.test.handlers.MethodVisitor;

public class MethodsContentProvider implements ITreeContentProvider, IResourceChangeListener, IResourceDeltaVisitor {
	private static final Logger logger = LoggerFactory.getLogger(MethodsContentProvider.class);

	private static final Object[] NO_CHILDREN = new Object[0];

	private static final Object JAVA_EXT = "java"; //$NON-NLS-1$

	@SuppressWarnings("rawtypes")
	private final Map/*<IFile, MethodsTreeData[]>*/ cachedModelMap = new HashMap();

	private StructuredViewer viewer;
	

	public MethodsContentProvider() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}


	public Object[] getChildren(Object parentElement) {  
		Object[] children = null;
		if (parentElement instanceof MethodsTreeData) { 
			children = NO_CHILDREN;
		} else if(parentElement instanceof IFile) {
			IFile modelFile = (IFile) parentElement;
			if(JAVA_EXT.equals(modelFile.getFileExtension())) {				
				children = (MethodsTreeData[]) cachedModelMap.get(modelFile);
				if(children == null && updateModel(modelFile) != null) {
					children = (MethodsTreeData[]) cachedModelMap.get(modelFile);
				}
			}
		}   
		return children != null ? children : NO_CHILDREN;
	}  


	private synchronized Properties updateModel(IFile modelFile) { 
		
		if(JAVA_EXT.equals(modelFile.getFileExtension()) ) {
			Properties model = new Properties();
			if (modelFile.exists()) {
				try {
					CompilationUnit cu = JavaParser.parse(modelFile.getRawLocation().makeAbsolute().toFile());
					List<HashMap<String, String>> methodList = new MethodVisitor().visit(cu, null);
					List methods = new ArrayList();

					for (HashMap<String, String> hashMap : methodList) {
						String methodName = hashMap.get("methodName");
						String value = hashMap.get("startLine")+":"+hashMap.get("endLine");
						methods.add(new MethodsTreeData(modelFile,methodName,value));
					}
					model.load(modelFile.getContents());
					MethodsTreeData[] MethodsTreeData = (MethodsTreeData[])
						methods.toArray(new MethodsTreeData[methods.size()]);
					
					cachedModelMap.put(modelFile, MethodsTreeData);
					return model; 
				} catch (IOException e) {
				} catch (CoreException e) {
				}
			} else {
				cachedModelMap.remove(modelFile);
			}
		}
		return null; 
	}

	public Object getParent(Object element) {
		if (element instanceof MethodsTreeData) {
			MethodsTreeData data = (MethodsTreeData) element;
			return data.getContainer();
		} 
		return null;
	}

	public boolean hasChildren(Object element) {		
		if (element instanceof MethodsTreeData) {
			return false;		
		} else if(element instanceof IFile) {
			return JAVA_EXT.equals(((IFile) element).getFileExtension());
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		cachedModelMap.clear();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this); 
	}

	public void inputChanged(Viewer aViewer, Object oldInput, Object newInput) {
		if (oldInput != null && !oldInput.equals(newInput))
			cachedModelMap.clear();
		viewer = (StructuredViewer) aViewer;
	}

	public void resourceChanged(IResourceChangeEvent event) {

		IResourceDelta delta = event.getDelta();
		try {
			delta.accept(this);
		} catch (CoreException e) { 
			e.printStackTrace();
		} 
	}

	public boolean visit(IResourceDelta delta) {

		IResource source = delta.getResource();
		switch (source.getType()) {
		case IResource.ROOT:
		case IResource.PROJECT:
		case IResource.FOLDER:
			return true;
		case IResource.FILE:
			final IFile file = (IFile) source;
			if (JAVA_EXT.equals(file.getFileExtension())) {
				updateModel(file);
				new UIJob("Update Methods Model in Methods Viewer") { 
					public IStatus runInUIThread(IProgressMonitor monitor) {
						if (viewer != null && !viewer.getControl().isDisposed())
							viewer.refresh(file);
						return Status.OK_STATUS;						
					}
				}.schedule();
			}
			return false;
		}
		return false;
	} 
}
