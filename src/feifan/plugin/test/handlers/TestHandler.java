package feifan.plugin.test.handlers;

import java.io.FileNotFoundException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TestHandler extends AbstractHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);
	private static final String javaFileExtension = "java";

	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);		
		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();

		if (activeEditor != null) {
		   IEditorInput input = activeEditor.getEditorInput();

		   IProject project = input.getAdapter(IProject.class);
		   if (project == null) {
		      IResource resource = input.getAdapter(IResource.class);
		      if (resource != null) {
		         project = resource.getProject();
		         logger.info("Project Name: " + project.getName());
		         IResource[] members;
				try {
					members = project.members();
					if (members != null && members.length > 0) {
						for (int i = 0; i < members.length; i++) {
							getMethodFromJavaFile(members[i]);
						}
					}
				} catch (CoreException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		         
		      }
		   }
		} else {
			MessageDialog.openInformation(
					window.getShell(),
					"No Active Project",
					"Please open a Java file to continue.");
		}
		
		return null;
	}
	
	private void getMethodFromJavaFile(IResource root) throws FileNotFoundException {
		if (root != null) {
			if (root.getType() == IResource.FILE && javaFileExtension.equals(root.getFileExtension())) {
				logger.info("File Path: " + root.getProjectRelativePath());
				CompilationUnit cu = JavaParser.parse(root.getRawLocation().makeAbsolute().toFile());
				cu.removeComment();
				new MethodVisitor().visit(cu, null);
		        
			} else if (root.getType() == IResource.FOLDER) {
				IFolder folder = (IFolder) root;
				try {
					IResource[] members = folder.members();
					if (members != null && members.length > 0) {
						for (int i = 0; i < members.length; i++) {
							getMethodFromJavaFile(members[i]);
							logger.info("");
						}
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
