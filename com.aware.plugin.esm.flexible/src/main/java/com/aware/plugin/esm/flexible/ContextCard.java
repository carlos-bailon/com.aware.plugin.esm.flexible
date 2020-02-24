package com.aware.plugin.esm.flexible;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aware.Aware;
import com.aware.ESM;
import com.aware.providers.Scheduler_Provider;
import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Question;
import com.aware.utils.IContextCard;
import com.aware.utils.Scheduler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    @Override
    public View getContextCard(final Context context) {
        //Load card layout
        View card = LayoutInflater.from(context).inflate(R.layout.card_esm_flexible, null);

        //Manually trigger questionnaire
        Button manualTrigger = card.findViewById(R.id.bt_trigger);
        manualTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor _schedules = context.getContentResolver().query(Scheduler_Provider.Scheduler_Data.CONTENT_URI, null, Scheduler_Provider.Scheduler_Data.SCHEDULE_ID + " NOT LIKE 'schedule_aware%' AND " + Scheduler_Provider.Scheduler_Data.PACKAGE_NAME + " LIKE '" + context.getPackageName() + "'", null, null);
                triggerQuestionnaire(context, _schedules);
            }
        });

        //Return the card to AWARE/apps
        return card;
    }

    private void triggerQuestionnaire(Context context, Cursor schedules) {
        boolean found = false;
        String questionnaire = Aware.getSetting(context, Settings.MANUAL_TRIGGER_QUESTIONNAIRE);
        if (!questionnaire.equals("") && schedules != null && schedules.moveToFirst()) {
            Log.d("Plugin: ESM Flexible", "Manually triggering "+questionnaire+"...");
            do {
                try {
                    JSONObject schedule = new JSONObject(schedules.getString(schedules.getColumnIndex(Scheduler_Provider.Scheduler_Data.SCHEDULE)));
                    if (schedule.getJSONObject("schedule").getJSONObject(Scheduler.SCHEDULE_ACTION).getString(Scheduler.ACTION_CLASS).equals(Plugin.ACTION_AWARE_ESM_FLEXIBLE)) {
                        JSONArray queue = new JSONArray(schedule.getJSONObject("schedule").getJSONObject(Scheduler.SCHEDULE_ACTION).getJSONArray(Scheduler.ACTION_EXTRAS).getJSONObject(0).getString(Scheduler.ACTION_EXTRA_VALUE));
                        String trigger = queue.getJSONObject(0).getJSONObject(ESM.EXTRA_ESM).getString(ESM_Question.esm_trigger);
                        if (trigger.equals(questionnaire)) {
                            for (int i=0; i<queue.length(); i++) {
                                queue.getJSONObject(i).getJSONObject(ESM.EXTRA_ESM).put(ESM_Question.esm_trigger, "Manual trigger - " + trigger);
                            }
                            ESM.queueESM(context, queue.toString());
                            found = true;
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (schedules.moveToNext());
            schedules.close();
            if (!found) {
                Toast toast = Toast.makeText(context, R.string.manual_trigger_toast_notfound, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Log.d("Plugin: ESM Flexible", "The questionnaire was not found");
            }
        } else {
            Toast toast = Toast.makeText(context, R.string.manual_trigger_toast_empty, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            Log.d("Plugin: ESM Flexible", "There are no questionnaires to trigger");
        }
    }
}
