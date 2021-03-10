package com.aware.plugin.esm.flexible;

import android.content.Context;
import android.util.Log;

import com.aware.ESM;
import com.aware.plugin.esm.flexible.definition.LikertOptions;
import com.aware.plugin.esm.flexible.definition.Question;
import com.aware.plugin.esm.flexible.definition.ScaleOptions;
import com.aware.plugin.esm.flexible.definition.Schedule;
import com.aware.plugin.esm.flexible.definition.ESMDefinition;

import com.aware.ui.esms.ESMFactory;
import com.aware.ui.esms.ESM_Checkbox;
import com.aware.ui.esms.ESM_Likert;
import com.aware.ui.esms.ESM_Question;
import com.aware.ui.esms.ESM_QuickAnswer;
import com.aware.ui.esms.ESM_Radio;
import com.aware.ui.esms.ESM_Scale;
import com.aware.ui.esms.ESM_ScaleImage;
import com.aware.utils.Http;
import com.aware.utils.Https;
import com.aware.utils.SSLManager;
import com.aware.utils.Scheduler;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ESMBuilder {

    private static String LOG_TAG = "ESMBuilder";
    private static String DEFAULT_LOCALE = "en";

    private ESMFactory factory;
    private ESMDefinition questionnaire;
    private Context context;

    private String locale;

    // 1. Test creation from XML file

    public ESMFactory createESM(Context context, ESMFactory factory, String File) {
        this.context = context;
        this.factory = factory;
        ESMDefinition questionnaire = null;
        if (File.startsWith("http://")) { // remote configuration file with HTTP
            try {
                Log.v(LOG_TAG,"Trying to connect to url: "+File);
                String serialized = new Http().dataGET(File, false);
                if (serialized != null) {
                    Log.v(LOG_TAG,"Connected to url");
                    questionnaire = ESMDeserializer.deserializeXml(serialized);
                    Log.v(LOG_TAG,"XML deserialized");
                } else {
                    Log.i(LOG_TAG,"XML is null cannot deserialize");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (File.startsWith("https://")) { // remote configuration file with HTTPS
            try {
                Log.v(LOG_TAG,"Trying to connect to url: "+File);
                SSLManager.handleUrl(context, File, true);
                String serialized = new Https(SSLManager.getHTTPS(context, File)).dataGET(File, false);
                if (serialized != null) {
                    Log.v(LOG_TAG,"Connected to url");
                    questionnaire = ESMDeserializer.deserializeXml(serialized);
                    Log.v(LOG_TAG,"XML deserialized");
                } else {
                    Log.i(LOG_TAG,"XML is null cannot deserialize");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try { // local configuration file
                Log.v(LOG_TAG,"Looking for file in local folder");
                InputStream is = context.getApplicationContext().getAssets().open(File);
                questionnaire = ESMDeserializer.deserializeXml(getStringFromInputStream(is));
                Log.v(LOG_TAG,"XML deserialized");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (questionnaire != null) {
            Log.v(LOG_TAG,"Constructing questionnaire: " + questionnaire.getName());
            this.questionnaire = questionnaire;
            setupESM();
        } else Log.i(LOG_TAG, "Questionnaire is null cannot start");
        return factory;
    }

    // 2. Test execution after creation

    public void setupESM() {
        setupLocale();
        setupQuestions();
        setupSchedules();
    }

    // 2.1 Setup locale

    private void setupLocale() {
        if (questionnaire.getQuestions() == null) { return; }

        // Get questionnaire locales
        List<String> localeList = new ArrayList<String>();
        for (Question question : questionnaire.getQuestions()) {
            if (!localeList.contains(question.getLocale())) localeList.add(question.getLocale());
        }

        // Ensure that locale list is not empty
        if (localeList.get(0) == null) {
            Log.e(LOG_TAG, "No locales found");
            return;
        }

        // Use device locale if exists in questionnaire; if not, use default or first locale available
        Log.v(LOG_TAG, localeList.size() + " locales found: " + localeList.toString());
        if (localeList.contains(Locale.getDefault().getLanguage())) {
            locale = Locale.getDefault().getLanguage();
        } else if (localeList.contains(DEFAULT_LOCALE)) {
            locale = DEFAULT_LOCALE;
        } else {
            locale = localeList.get(0);
        }
        Log.v(LOG_TAG, "Questionnaire locale set to [" + locale + "]");
    }

    // 2.2. Setup questions

    private void setupQuestions() {
        if (questionnaire.getQuestions() == null) {
            Log.e(LOG_TAG, "No questions found");
            return;
        }
        Log.v(LOG_TAG, questionnaire.getQuestions().size() + " questions found");
        for (Question question : questionnaire.getQuestions()) {
            if (question.getLocale().equals(locale)) {
                try {
                    ESM_Question esm = constructQuestion(question.getESM_Type());
                    try {
                        //Basic ESM settings
                        esm.setTitle(question.getTitle())
                                .setTrigger(questionnaire.getName())
                                .setExpirationThreshold(question.getExpirationThreshold())
                                .setNotificationTimeout(question.getNotificationTimeout())
                                .setReplaceQueue(true);

                        //Additional ESM settings
                        question.setInstructions(question.getInstructions().replace("\n", "").replace("\r", ""));
                        esm.setInstructions(question.getInstructions());
                        esm.setSubmitButton(question.getSubmitText());
                        esm.setCancelButton(question.getCancelText());

                        //Additional settings for ESM_Scale
                        if (esm instanceof ESM_Scale) {
                            ScaleOptions scaleOptions = question.getScaleOptions();

                            ((ESM_Scale) esm).setScaleStart(scaleOptions.getScaleStart());
                            ((ESM_Scale) esm).setScaleStep(scaleOptions.getScaleStep());
                            ((ESM_Scale) esm).setScaleMin(scaleOptions.getScaleMin());
                            ((ESM_Scale) esm).setScaleMinLabel(scaleOptions.getScaleMinLabel());
                            ((ESM_Scale) esm).setScaleMax(scaleOptions.getScaleMax());
                            ((ESM_Scale) esm).setScaleMaxLabel(scaleOptions.getScaleMaxLabel());
                        }

                        //Additional settings for ESM_ScaleImage
                        if (esm instanceof ESM_ScaleImage) {
                            ScaleOptions scaleOptions = question.getScaleOptions();

                            if (!scaleOptions.isScaleStartRandom())
                                ((ESM_ScaleImage) esm).setScaleStart(scaleOptions.getScaleStart());
                            ((ESM_ScaleImage) esm).setScaleStartRandomValues(scaleOptions.getScaleStartRandomValues());
                            ((ESM_ScaleImage) esm).setScaleStep(scaleOptions.getScaleStep());
                            ((ESM_ScaleImage) esm).setScaleMin(scaleOptions.getScaleMin());
                            ((ESM_ScaleImage) esm).setScaleMinLabel(scaleOptions.getScaleMinLabel());
                            ((ESM_ScaleImage) esm).setScaleMax(scaleOptions.getScaleMax());
                            ((ESM_ScaleImage) esm).setScaleMaxLabel(scaleOptions.getScaleMaxLabel());
                            ((ESM_ScaleImage) esm).setScaleStartRandom(scaleOptions.isScaleStartRandom());
                            ((ESM_ScaleImage) esm).setScaleValueVisible(scaleOptions.isScaleValueVisible());
                            if (scaleOptions.getLeftImageUrl() != null)
                                ((ESM_ScaleImage) esm).setLeftImageUrl(scaleOptions.getLeftImageUrl());
                            if (scaleOptions.getRightImageUrl() != null)
                                ((ESM_ScaleImage) esm).setRightImageUrl(scaleOptions.getRightImageUrl());
                        }

                        //Additional settings for ESM_Likert
                        if (esm instanceof ESM_Likert) {
                            LikertOptions likertOptions = question.getLikertOptions();

                            ((ESM_Likert) esm).setLikertMax(likertOptions.getLikertMax());
                            ((ESM_Likert) esm).setLikertStep(likertOptions.getLikertStep());
                            ((ESM_Likert) esm).setLikertMinLabel(likertOptions.getLikertMinLabel());
                            ((ESM_Likert) esm).setLikertMaxLabel(likertOptions.getLikertMaxLabel());
                        }

                        //Additional settings for ESM_Checkbox, ESM_Radio and ESM_QuickAnswer
                        if (esm instanceof ESM_Checkbox) {
                            ((ESM_Checkbox) esm).setCheckboxes(question.getOptionsAsJSON());
                        } else if (esm instanceof ESM_Radio) {
                            ((ESM_Radio) esm).setRadios(question.getOptionsAsJSON());
                        } else if (esm instanceof ESM_QuickAnswer) {
                            ((ESM_QuickAnswer) esm).setQuickAnswers(question.getOptionsAsJSON());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    factory.addESM(esm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 2.3. Setup schedules

    private void setupSchedules() {
        if (questionnaire.getSchedules() == null) {
            Log.v(LOG_TAG, "No Schedules found");
            return;
        }
        Log.v(LOG_TAG, questionnaire.getSchedules().size() + " schedules found");
        for (Schedule schedule : questionnaire.getSchedules()) {
            Scheduler.Schedule s = new Scheduler.Schedule(schedule.getId());
            Log.v(LOG_TAG, "Setting up schedule: "+schedule.getId());
            try {
                for (int hour : schedule.getHour())
                    s.addHour(hour);

                for (int minute : schedule.getMinute())
                    s.addMinute(minute);

                for (String month : schedule.getMonth())
                    s.addMonth(month);

                for (String weekday : schedule.getWeekday())
                    s.addWeekday(weekday);

                if(schedule.isRandom()) s.random(schedule.getAmount(), schedule.getInterval());

                s.setActionType(Scheduler.ACTION_TYPE_BROADCAST);
                s.setActionClass(Plugin.ACTION_AWARE_ESM_FLEXIBLE);
                s.setActionIntentAction(ESM.ACTION_AWARE_QUEUE_ESM);
                s.addActionExtra(ESM.EXTRA_ESM, factory.build());

                Scheduler.saveSchedule(context, s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Asset for the deserialization method

    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    // Asset for the questionnaire construction

    private ESM_Question constructQuestion(String esm_type) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        esm_type = "com.aware.ui.esms." + esm_type;
        Class<?> c = Class.forName(esm_type);
        return (ESM_Question) c.newInstance();
    }
}