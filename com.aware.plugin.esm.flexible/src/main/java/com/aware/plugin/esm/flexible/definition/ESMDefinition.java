package com.aware.plugin.esm.flexible.definition;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class ESMDefinition {

    @Attribute(name = "noNamespaceSchemaLocation")
    private String schema;

    @Element(name = "name")
    private String name;

    @Element(name = "short_name")
    private String short_name;

    @Element(name = "description", required = false)
    private String description;

    @ElementList(name = "Question", inline = true)
    private List<Question> questions;

    @ElementList(name = "Schedule", inline = true)
    private List<Schedule> schedules;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

}