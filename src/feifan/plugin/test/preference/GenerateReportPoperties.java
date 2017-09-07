package feifan.plugin.test.preference;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feifan.plugin.test.Activator;

public class GenerateReportPoperties extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private static final Logger logger = LoggerFactory.getLogger(GenerateReportPoperties.class);

	public static final String FOLDER_PATH = "FolderPath";
	public static final String SUCCESS_MESSAGE = "SuccessMessage";
	public static final String GROUP_TIITLE = "Java Methods";

	public GenerateReportPoperties() {
		super(GRID);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(FOLDER_PATH, store.getString(FOLDER_PATH)!=null&&!"".equals(store.getString(FOLDER_PATH))?store.getString(FOLDER_PATH):store.getDefaultString(FOLDER_PATH));
		store.setValue(SUCCESS_MESSAGE, store.getString(SUCCESS_MESSAGE)!=null&&!"".equals(store.getString(SUCCESS_MESSAGE))?store.getString(SUCCESS_MESSAGE):store.getDefaultString(SUCCESS_MESSAGE));
		setPreferenceStore(store);
    }
	
	
	@Override
    public void createFieldEditors() {
        addField(new DirectoryFieldEditor(FOLDER_PATH, "&Save report in:", getFieldEditorParent()));
        addField(new StringFieldEditor(SUCCESS_MESSAGE, "&Success Message:", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
    		logger.info("init GenerateReportPoperties");
        setDescription("Demonstrate preference:");
    }
    
    @Override
    protected void performDefaults() {
    		getPreferenceStore().setValue(FOLDER_PATH, getPreferenceStore().getDefaultString(FOLDER_PATH));
    		getPreferenceStore().setValue(SUCCESS_MESSAGE, getPreferenceStore().getDefaultString(SUCCESS_MESSAGE));
    		
    		super.performDefaults();
		
    }
    
    @Override
	public boolean performOk() {
		boolean result = super.performOk();
		return result;
	}
    
    /**
     * Initializes all field editors.
     */
    protected void initialize() {
        super.initialize();
    }



}

