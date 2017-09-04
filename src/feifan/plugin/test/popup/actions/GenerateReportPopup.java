package feifan.plugin.test.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

public class GenerateReportPopup implements IObjectActionDelegate{
	private IWorkbenchWindow window; 
	@Override
	public void run(IAction arg0) {
		MessageDialog.openInformation(window.getShell(), "Pop Up Menu",
				"run pop up menu command");
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
