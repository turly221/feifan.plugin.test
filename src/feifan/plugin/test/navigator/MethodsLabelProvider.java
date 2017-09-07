package feifan.plugin.test.navigator;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.IDescriptionProvider;

public class MethodsLabelProvider extends LabelProvider implements ILabelProvider, IDescriptionProvider {


	@Override
	public Image getImage(Object element) {
		if (element instanceof MethodsTreeData)
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		return null;
	}

	public String getText(Object element) {
		if (element instanceof MethodsTreeData) {
			MethodsTreeData data = (MethodsTreeData) element;
			return data.getMethodName();
		}  
		return null;
	}

	public String getDescription(Object anElement) {
		if (anElement instanceof MethodsTreeData) {
			MethodsTreeData data = (MethodsTreeData) anElement;
			return data.getMethodName();
		}
		return null;
	}

}
