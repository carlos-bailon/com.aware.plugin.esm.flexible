package com.aware.plugin.esm.flexible.definition;

import org.json.JSONArray;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Question", strict = false)
public class Question {

    @Element(name = "ESM_Type")
    private String ESM_Type;

    @Element(name = "Title")
    private String Title;

    @Element(name = "Instructions", required = false)
    private String Instructions;

    @Element(name = "SubmitText", required = false)
    private String SubmitText;

    @Element(name = "CancelText", required = false)
    private String CancelText;

    @Element(name = "ExpirationThreshold", required = false)
    private int ExpirationThreshold;

    @Element(name = "NotificationTimeout", required = false)
    private int NotificationTimeout;

    @Element(name = "ScaleOptions", required = false)
    private ScaleOptions ScOptions;

    @Element(name = "LikertOptions", required = false)
    private LikertOptions LikOptions;

    @ElementList(name= "Options", required = false)
    private List<String> options;

    public String getESM_Type() {
        return ESM_Type;
    }

    public void setESM_Type(String ESM_Type) {
        this.ESM_Type = ESM_Type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

    public String getSubmitText() {
        return SubmitText;
    }

    public void setSubmitText(String submitText) {
        SubmitText = submitText;
    }

    public String getCancelText() {
        return CancelText;
    }

    public void setCancelText(String cancelText) {
        CancelText = cancelText;
    }

    public int getExpirationThreshold() {
        return ExpirationThreshold;
    }

    public void setExpirationThreshold(int expirationThreshold) {
        ExpirationThreshold = expirationThreshold;
    }

    public int getNotificationTimeout() {
        return NotificationTimeout;
    }

    public void setNotificationTimeout(int notificationTimeout) {
        NotificationTimeout = notificationTimeout;
    }

    public ScaleOptions getScaleOptions() { return ScOptions; }

    public void setScaleOptions(ScaleOptions scOptions) { ScOptions = scOptions; }

    public LikertOptions getLikertOptions() { return LikOptions; }

    public void setScaleOptions(LikertOptions likOptions) { LikOptions = likOptions; }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public JSONArray getOptionsAsJSON() {
        JSONArray json = new JSONArray();
        for (String op : options)
            json.put(op);
        return json;
    }

}
