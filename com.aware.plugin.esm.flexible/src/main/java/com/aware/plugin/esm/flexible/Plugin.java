package com.aware.plugin.esm.flexible;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.plugin.esm.flexible.R;
import com.aware.ui.esms.ESMFactory;
import com.aware.utils.Aware_Plugin;
import com.aware.utils.Scheduler;

import java.security.Provider;

public class Plugin extends Aware_Plugin {

    public static String ACTION_AWARE_ESM_FLEXIBLE = "action_aware_esm_flexble";

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "AWARE::" + getResources().getString(R.string.plugin_esm_flexible);

        //Add permissions you need (Android M+).
        REQUIRED_PERMISSIONS.add(Manifest.permission.VIBRATE);
        REQUIRED_PERMISSIONS.add(Manifest.permission.INTERNET);
        REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_SYNC_SETTINGS);
    }

    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (PERMISSIONS_OK) {

            //Check if the user has toggled the debug messages
            DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

            //Initialize our plugin's settings
            if (Aware.getSetting(getApplicationContext(), Settings.STATUS_PLUGIN_ESM_FLEXIBLE).length() == 0) {
                Aware.setSetting(getApplicationContext(), Settings.STATUS_PLUGIN_ESM_FLEXIBLE, true);
            } else {
                if (Aware.getSetting(getApplicationContext(), Settings.STATUS_PLUGIN_ESM_FLEXIBLE).equalsIgnoreCase("false")) {
                    Aware.stopPlugin(getApplicationContext(), getPackageName());
                    return START_STICKY;
                }
            }

            //Start the ESM service
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, true, "com.aware.phone");

            Log.d(TAG, "Plugin started");

            //Setup questionnaires
            String[] questionnaires = Aware.getSetting(getApplicationContext(), Settings.QUESTIONNAIRE_NAMES).split(",");
            if(!questionnaires[0].equals("")) { // Do not make any questionnaire if the setting is empty
                for (String questionnaire : questionnaires) {
                    if (DEBUG) Log.v(TAG, "Creating questionnaire: " + questionnaire);
                    ExecuterParams params = new ExecuterParams(this, questionnaire);
                    ESMBuilderTask task = new ESMBuilderTask();
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
                }
            } else if (DEBUG) {
                Log.i(TAG, "No questionnaires found");
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Aware.setSetting(this, Settings.STATUS_PLUGIN_ESM_FLEXIBLE, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, false, "com.aware.phone");

        //Scheduler.clearSchedules(this);

        Log.d(TAG, "Plugin stopped");
    }

    private static class ExecuterParams {
        Context context;
        String questionnaire;

        ExecuterParams(Context context, String questionnaire) {
            this.context = context;
            this.questionnaire = questionnaire;
        }
    }

    private static class ESMBuilderTask extends AsyncTask<ExecuterParams, Void, Void> {
        @Override
        protected Void doInBackground(ExecuterParams... params) {
            Context context = params[0].context;
            ESMFactory esmFactory = new ESMFactory();
            String questionnaire = params[0].questionnaire;
            new ESMBuilder().createESM(context, esmFactory, questionnaire);
            return null;
        }
    }
}