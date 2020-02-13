package com.aware.plugin.esm.flexible;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.aware.Aware;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //Plugin settings in XML @xml/preferences
    public static final String STATUS_PLUGIN_ESM_FLEXIBLE = "status_plugin_esm_flexible";
    public static final String QUESTIONNAIRE_NAMES = "questionnaire_names";
    public static final String MANUAL_TRIGGER_QUESTIONNAIRE = "manual_trigger_questionnaire";

    //Plugin settings UI elements
    private static CheckBoxPreference status;
    private static EditTextPreference questionnaireNames;
    private static EditTextPreference manualTriggerQuestionnaire;

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
        questionnaireNames = (EditTextPreference) findPreference(QUESTIONNAIRE_NAMES);
        manualTriggerQuestionnaire = (EditTextPreference) findPreference(MANUAL_TRIGGER_QUESTIONNAIRE);

        if( Aware.getSetting(this, STATUS_PLUGIN_ESM_FLEXIBLE).length() == 0 ) {
            Aware.setSetting( this, STATUS_PLUGIN_ESM_FLEXIBLE, true ); //by default, the setting is true on install
        }
        status.setChecked(Aware.getSetting(getApplicationContext(), STATUS_PLUGIN_ESM_FLEXIBLE).equals("true"));
        questionnaireNames.setText(Aware.getSetting(getApplicationContext(), QUESTIONNAIRE_NAMES));
        manualTriggerQuestionnaire.setText(Aware.getSetting(getApplicationContext(), MANUAL_TRIGGER_QUESTIONNAIRE));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference setting = findPreference(key);

        if( setting.getKey().equals(STATUS_PLUGIN_ESM_FLEXIBLE) ) {
            Aware.setSetting(this, key, sharedPreferences.getBoolean(key, false));
            status.setChecked(sharedPreferences.getBoolean(key, false));
        }

        if (setting.getKey().equals(QUESTIONNAIRE_NAMES)) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getString(key, null));
            questionnaireNames.setText(Aware.getSetting(getApplicationContext(), QUESTIONNAIRE_NAMES));
        }

        if (setting.getKey().equals(MANUAL_TRIGGER_QUESTIONNAIRE)) {
            Aware.setSetting(getApplicationContext(), key, sharedPreferences.getString(key, null));
            manualTriggerQuestionnaire.setText(Aware.getSetting(getApplicationContext(), MANUAL_TRIGGER_QUESTIONNAIRE));
        }

        if (Aware.getSetting(this, STATUS_PLUGIN_ESM_FLEXIBLE).equals("true")) {
            Aware.startPlugin(getApplicationContext(), "com.aware.plugin.esm.flexible");
        } else {
            Aware.stopPlugin(getApplicationContext(), "com.aware.plugin.esm.flexible");
        }
    }
}