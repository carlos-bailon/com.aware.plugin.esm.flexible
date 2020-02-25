package com.aware.plugin.esm.flexible;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.aware.Aware;
import com.aware.ui.AppCompatPreferenceActivity;

public class Settings extends AppCompatPreferenceActivity implements OnSharedPreferenceChangeListener {

    /**
     * Activate/deactivate plugin
     */
    public static final String STATUS_PLUGIN_ESM_FLEXIBLE = "status_plugin_esm_flexible";

    /**
     * Comma separated names of questionnaires to trigger
     */
    public static final String QUESTIONNAIRE_NAMES = "questionnaire_names";

    /**
     * Name of the questionnaire to trigger manually
     */
    public static final String MANUAL_TRIGGER_QUESTIONNAIRE = "manual_trigger_questionnaire";

    private static CheckBoxPreference status;
    private static EditTextPreference questionnaireNames, manualTriggerQuestionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_esm_flexible);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_ESM_FLEXIBLE);
        if( Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_ESM_FLEXIBLE).length() == 0 ) {
            Aware.setSetting( getApplicationContext(), STATUS_PLUGIN_ESM_FLEXIBLE, true ); //by default, the setting is true on install
        }
        status.setChecked(Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_ESM_FLEXIBLE).equals("true"));

        questionnaireNames = (EditTextPreference) findPreference(QUESTIONNAIRE_NAMES);
        if (Aware.getSetting(getApplicationContext(), QUESTIONNAIRE_NAMES).length() == 0) {
            Aware.setSetting(getApplicationContext(), QUESTIONNAIRE_NAMES, "");
        }
        questionnaireNames.setSummary(Aware.getSetting(getApplicationContext(), QUESTIONNAIRE_NAMES));

        manualTriggerQuestionnaire = (EditTextPreference) findPreference(MANUAL_TRIGGER_QUESTIONNAIRE);
        if (Aware.getSetting(getApplicationContext(), MANUAL_TRIGGER_QUESTIONNAIRE).length() == 0) {
            Aware.setSetting(getApplicationContext(), MANUAL_TRIGGER_QUESTIONNAIRE, "");
        }
        manualTriggerQuestionnaire.setSummary(Aware.getSetting(getApplicationContext(), MANUAL_TRIGGER_QUESTIONNAIRE));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference setting = findPreference(key);

        if( setting.getKey().equals(STATUS_PLUGIN_ESM_FLEXIBLE) ) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getBoolean(key, false));
            status.setChecked(sharedPreferences.getBoolean(key, false));
        }
        if (setting.getKey().equals(QUESTIONNAIRE_NAMES)) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getString(key, ""));
            questionnaireNames.setSummary(Aware.getSetting(getApplicationContext(), QUESTIONNAIRE_NAMES));
        }
        if (setting.getKey().equals(MANUAL_TRIGGER_QUESTIONNAIRE)) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getString(key, ""));
            manualTriggerQuestionnaire.setSummary(Aware.getSetting(getApplicationContext(), MANUAL_TRIGGER_QUESTIONNAIRE));
        }

        if (Aware.getSetting(this, STATUS_PLUGIN_ESM_FLEXIBLE).equals("true")) {
            Aware.startPlugin(getApplicationContext(), "com.aware.plugin.esm.flexible");
        } else {
            Aware.stopPlugin(getApplicationContext(), "com.aware.plugin.esm.flexible");
        }
    }
}