package feifan.plugin.test.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import feifan.plugin.test.Activator;

public class TestHandler extends AbstractHandler {

	private static final Logger logger = LoggerFactory.getLogger(TestHandler.class);
	private static final String javaFileExtension = "java";
	private static final String reportName = "ProjectJavaMethodsReport.xml";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		String filePath = Activator.getDefault().getPreferenceStore().getString("FolderPath");
		String successMessage = Activator.getDefault().getPreferenceStore().getString("SuccessMessage");
		successMessage += "\nFile saved in"+filePath+File.separator+reportName;
		
		boolean error = false;

		if (activeEditor != null) {
			IEditorInput input = activeEditor.getEditorInput();

			IProject project = input.getAdapter(IProject.class);
			if (project == null) {
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null) {
					project = resource.getProject();
					logger.info("Project Name: " + project.getName());
					IResource[] members;
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					try {
						members = project.members();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						// root elements
						Document doc = docBuilder.newDocument();
						Element rootElement = doc.createElement("Project");

						// project name
						String projectName = project.getName();
						Attr projAttr = doc.createAttribute("Name");
						projAttr.setValue(projectName);
						rootElement.setAttributeNode(projAttr);
						doc.appendChild(rootElement);

						if (members != null && members.length > 0) {
							for (int i = 0; i < members.length; i++) {
								getMethodFromJavaFile(members[i], doc);
							}
						}
						
						// write the content into xml file
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);

						StreamResult result = new StreamResult(new File(filePath+File.separator+reportName));

						// Output to console for testing
						// StreamResult result = new StreamResult(System.out);

						transformer.transform(source, result);

						
					} catch (CoreException e) {
						logger.debug("CoreException: "+e.getMessage());
						error = true;
					} catch (FileNotFoundException e) {
						logger.debug("FileNotFoundException: "+e.getMessage());
						error = true;
					} catch (ParserConfigurationException e) {
						logger.debug("ParserConfigurationException: "+e.getMessage());
						error = true;
					} catch (TransformerConfigurationException e) {
						logger.debug("TransformerConfigurationException: "+e.getMessage());
						error = true;
					} catch (TransformerException e) {
						logger.debug("TransformerException: "+e.getMessage());
						error = true;
					} finally {
						if (error) {
							MessageDialog.openError(window.getShell(), "Failed to Generate Report!",
									"Failed to Generate Report!");
						} else {
							MessageDialog.openInformation(window.getShell(), "File saved!",
									successMessage);
						}
					}
				}
			}
		} else {
			MessageDialog.openInformation(window.getShell(), "No Active Project",
					"Please open a Java file to continue.");
		}

		return null;
	}

	private void getMethodFromJavaFile(IResource root, Document doc) throws FileNotFoundException {
		if (root != null) {
			if (root.getType() == IResource.FILE && javaFileExtension.equals(root.getFileExtension())) {
				logger.info("File Path: " + root.getProjectRelativePath());
				Element rootElement = doc.getDocumentElement();
				// Java file elements
				Element file = doc.createElement("File");
				rootElement.appendChild(file);

				// File name
				String fileName = root.getProjectRelativePath().toString();
				Attr fileAttr = doc.createAttribute("Name");
				fileAttr.setValue(fileName);
				file.setAttributeNode(fileAttr);

				CompilationUnit cu = JavaParser.parse(root.getRawLocation().makeAbsolute().toFile());
				List<HashMap<String, String>> methodList = new MethodVisitor().visit(cu, null);
				for (HashMap<String, String> hashMap : methodList) {
					// method name elements
					Element methodName = doc.createElement("methodName");
					methodName.appendChild(doc.createTextNode(hashMap.get("methodName")));
					file.appendChild(methodName);
					
					// Starting line elements
					Element startLine = doc.createElement("startLine");
					startLine.appendChild(doc.createTextNode(hashMap.get("startLine")));
					methodName.appendChild(startLine);

					// Ending line elements
					Element endLine = doc.createElement("endLine");
					endLine.appendChild(doc.createTextNode(hashMap.get("endLine")));
					methodName.appendChild(endLine);
				}
				

				
			} else if (root.getType() == IResource.FOLDER) {
				IFolder folder = (IFolder) root;
				try {
					IResource[] members = folder.members();
					if (members != null && members.length > 0) {
						for (int i = 0; i < members.length; i++) {
							getMethodFromJavaFile(members[i], doc);
						}
					}
				} catch (CoreException e) {
					logger.debug("error in find element in folders: " + e.getMessage());
				}
			}
		}
	}

}
