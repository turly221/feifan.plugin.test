package feifan.plugin.test.preference;

import java.io.File;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feifan.plugin.test.Activator;

public class GenerateReportPopertiesInit extends AbstractPreferenceInitializer {
	private static final Logger logger = LoggerFactory.getLogger(GenerateReportPopertiesInit.class);
	
	public static final String FOLDER_PATH = "FolderPath";
	public static final String SUCCESS_MESSAGE = "SuccessMessage";

	public static final String DEFAULT_FOLDER_PATH = System.getProperty("user.home")+File.separator+"Desktop";
	public static final String DEFAULT_SUCCESS_MESSAGE = "File saved successfully!";

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStore=Activator.getDefault().getPreferenceStore();
		
		preferenceStore.setDefault(FOLDER_PATH, DEFAULT_FOLDER_PATH);
		preferenceStore.setDefault(SUCCESS_MESSAGE, DEFAULT_SUCCESS_MESSAGE);

		logger.info("in initializeDefaultPreferences: "+FOLDER_PATH+": "+preferenceStore.getDefaultString(FOLDER_PATH));
		logger.info("in initializeDefaultPreferences: "+SUCCESS_MESSAGE+": "+preferenceStore.getDefaultString(SUCCESS_MESSAGE));
	}

}
