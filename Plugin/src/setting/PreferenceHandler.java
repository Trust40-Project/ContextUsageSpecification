package setting;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import util.Util;

public class PreferenceHandler extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    // The plug-in ID
    public static final String PLUGIN_ID = "ContextUsageSpecification";
    private static final IPreferenceStore PREF_STORE = new ScopedPreferenceStore(InstanceScope.INSTANCE, PLUGIN_ID);

    public static String namePath = "0";
    public static String nameDataprocessing = "1";
    public static String nameUsageModel = "2";
    public static String nameAssembly = "3";
    public static String nameRepositoryModel = "4";
    private static String nameContextMaster = "5";
    private static String nameCreateContextCharacteristic = "6";
    private static String nameApplyStereotype = "7";
    private static String nameSaveData = "8";

    private static final int WIDTH = 75;

    public PreferenceHandler() {
        super(GRID);
        setPreferenceStore(PREF_STORE);

        setDefault();
    }

    public static void setDefault() {
        // Default values
        PREF_STORE.setDefault(namePath, Util.getCurrentDir());
        PREF_STORE.setDefault(nameRepositoryModel, "newRepository.repository");
        PREF_STORE.setDefault(nameAssembly, "newAssembly.system");
        PREF_STORE.setDefault(nameUsageModel, "newUsageModel.usagemodel");
        PREF_STORE.setDefault(nameDataprocessing, "My.dataprocessing");
    }

    @Override
    public void init(IWorkbench arg0) {
        setDescription("Preference page for the plugin " + "");
    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();

        // Paths
        StringFieldEditor path = new StringFieldEditor(namePath, "Project Path:", WIDTH, parent);
        addField(path);
        StringFieldEditor dataprocessing = new StringFieldEditor(nameDataprocessing, "DataProcessingFile:", WIDTH,
                parent);
        addField(dataprocessing);
        StringFieldEditor useagemodel = new StringFieldEditor(nameUsageModel, "UsageModelFile:", WIDTH, parent);
        addField(useagemodel);
        StringFieldEditor assembly = new StringFieldEditor(nameAssembly, "AssemblyFile:", WIDTH, parent);
        addField(assembly);
        StringFieldEditor repository = new StringFieldEditor(nameRepositoryModel, "RepositoryModelFile:", WIDTH,
                parent);
        addField(repository);

        // Settings
        String[][] s1 = { { "Characterizable", "Characterizable" }, { "DataProcessing", "DataProcessing" },
                { "Combined", "Combined" } };
        RadioGroupFieldEditor contextMaster = new RadioGroupFieldEditor(nameContextMaster, "ContextMaster:", 3, s1,
                parent);
        addField(contextMaster);
        String[][] s2 = { { "true", "true" }, { "false", "false" } };
        RadioGroupFieldEditor createContextCharacteristic = new RadioGroupFieldEditor(nameCreateContextCharacteristic,
                "Create Context Characteristic:", 2, s2, parent);
        addField(createContextCharacteristic);
        RadioGroupFieldEditor applyStereotype = new RadioGroupFieldEditor(nameApplyStereotype, "Apply Stereotype:", 2,
                s2, parent);
        addField(applyStereotype);
        RadioGroupFieldEditor saveChanges = new RadioGroupFieldEditor(nameSaveData, "SaveChanges:", 2, s2, parent);
        addField(saveChanges);
    }

    public static String getProjectPath() {
        return PREF_STORE.getString(namePath);
    }

    public static String getPathrepositorymodel() {
        return PREF_STORE.getString(nameRepositoryModel);
    }

    public static String getPathassembly() {
        return PREF_STORE.getString(nameAssembly);
    }

    public static String getPathusagemodel() {
        return PREF_STORE.getString(nameUsageModel);
    }

    public static String getPathdataprocessing() {
        return PREF_STORE.getString(nameDataprocessing);
    }

    public static Settings getSettingsFromPreferences() {
        ContextMaster master = ContextMaster.Combined;

        Boolean createContextCharacteristic;
        if (PREF_STORE.getString(nameCreateContextCharacteristic).equalsIgnoreCase("true")) {
            createContextCharacteristic = true;
        } else {
            createContextCharacteristic = false;
        }
        Boolean applyStereotype;
        if (PREF_STORE.getString(nameApplyStereotype).equalsIgnoreCase("true")) {
            applyStereotype = true;
        } else {
            applyStereotype = false;
        }

        Boolean saveData;
        if (PREF_STORE.getString(nameSaveData).equalsIgnoreCase("true")) {
            saveData = true;
        } else {
            saveData = false;
        }

        return new Settings(createContextCharacteristic, master, applyStereotype, saveData);
    }
}
