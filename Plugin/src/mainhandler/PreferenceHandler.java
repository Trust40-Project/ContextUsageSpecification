package mainhandler;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class PreferenceHandler extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private static final IPreferenceStore PREF_STORE = new ScopedPreferenceStore(InstanceScope.INSTANCE, "");

    private static String nameDataprocessing = "1";
    private static String nameUsageModel = "2";
    private static String nameAssembly = "3";
    private static String nameRepositoryModel = "4";

    private static final int WIDTH = 100;

    public PreferenceHandler() {

    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();

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
    }

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(PREF_STORE);
        setDescription("Preference page for the plugin " + "");
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
}
