package com.aware.plugin.esm.flexible.definition;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ScaleOptions", strict = false)
public class ScaleOptions {

    @Element(name = "ScaleStartRandom", required = false)
    private boolean scaleStartRandom;

    @Element(name = "ScaleStartRandomValues", required = false)
    private int scaleStartRandomValues;

    @Element(name = "ScaleStart", required = false)
    private int scaleStart;

    @Element(name = "ScaleStep", required = false)
    private int scaleStep;

    @Element(name = "ScaleMin", required = false)
    private int scaleMin;

    @Element(name = "ScaleMinLabel", required = false)
    private String scaleMinLabel;

    @Element(name = "ScaleMax", required = false)
    private int scaleMax;

    @Element(name = "ScaleMaxLabel", required = false)
    private String scaleMaxLabel;

    @Element(name = "ScaleValueVisibility", required = false)
    private boolean scaleValueVisibile;

    @Element(name = "LeftImageUrl", required = false)
    private String leftImageUrl;

    @Element(name = "RightImageUrl", required = false)
    private String rightImageUrl;

    public boolean isScaleStartRandom() {
        return scaleStartRandom;
    }

    public void setScaleStartRandom(boolean scaleStartRandom) {
        this.scaleStartRandom = scaleStartRandom;
    }

    public int getScaleStart() {
        return scaleStart;
    }

    public void setScaleStartRandomValues(int scaleStartRandomValues) {
        this.scaleStartRandomValues = scaleStartRandomValues;
    }

    public int getScaleStartRandomValues() {
        return scaleStartRandomValues;
    }

    public void setScaleStart(int scaleStart) {
        this.scaleStart = scaleStart;
    }

    public int getScaleStep() {
        return scaleStep;
    }

    public void setScaleStep(int scaleStep) {
        this.scaleStep = scaleStep;
    }

    public int getScaleMin() {
        return scaleMin;
    }

    public void setScaleMin(int scaleMin) {
        this.scaleMin = scaleMin;
    }

    public String getScaleMinLabel() {
        return scaleMinLabel;
    }

    public void setScaleMinLabel(String scaleMinLabel) {
        this.scaleMinLabel = scaleMinLabel;
    }

    public int getScaleMax() {
        return scaleMax;
    }

    public void setScaleMax(int scaleMax) {
        this.scaleMax = scaleMax;
    }

    public String getScaleMaxLabel() {
        return scaleMaxLabel;
    }

    public void setScaleMaxLabel(String scaleMaxLabel) {
        this.scaleMaxLabel = scaleMaxLabel;
    }

    public boolean isScaleValueVisibile() {
        return scaleValueVisibile;
    }

    public void setScaleValueVisibile(boolean scaleValueVisibile) {
        this.scaleValueVisibile = scaleValueVisibile;
    }

    public String getLeftImageUrl() {
        return leftImageUrl;
    }

    public void setLeftImageUrl(String leftImageUrl) {
        this.leftImageUrl = leftImageUrl;
    }

    public String getRightImageUrl() {
        return rightImageUrl;
    }

    public void setRightImageUrl(String rightImageUrl) {
        this.rightImageUrl = rightImageUrl;
    }

}