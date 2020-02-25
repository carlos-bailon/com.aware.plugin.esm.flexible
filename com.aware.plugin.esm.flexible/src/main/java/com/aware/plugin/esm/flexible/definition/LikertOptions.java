package com.aware.plugin.esm.flexible.definition;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "LikertOptions", strict = false)
public class LikertOptions {

    @Element(name = "LikertMax", required = false)
    private int likertMax;

    @Element(name = "LikertStep", required = false)
    private int likertStep;

    @Element(name = "LikertMinLabel", required = false)
    private String likertMinLabel;

    @Element(name = "LikertMaxLabel", required = false)
    private String likertMaxLabel;

    public int getLikertMax() {
        return likertMax;
    }

    public void setLikertMax(int likertMax) {
        this.likertMax = likertMax;
    }

    public int getLikertStep() {
        return likertStep;
    }

    public void setLikertStep(int likertStep) {
        this.likertStep = likertStep;
    }

    public String getLikertMinLabel() {
        return likertMinLabel;
    }

    public void setLikertMinLabel(String likertMinLabel) {
        this.likertMinLabel = likertMinLabel;
    }

    public String getLikertMaxLabel() {
        return likertMaxLabel;
    }

    public void setLikertMaxLabel(String likertMaxLabel) {
        this.likertMaxLabel = likertMaxLabel;
    }

}
