package com.aware.plugin.esm.flexible;

import android.util.Log;

import com.aware.plugin.esm.flexible.definition.ESMDefinition;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class ESMDeserializer {

    public static ESMDefinition deserializeXml(String Xml) throws Exception {
        Log.v("ESMDeserializer", Xml);
        Serializer serializer = new Persister();
        ESMDefinition esmDefinition = serializer.read(ESMDefinition.class, Xml);
        return esmDefinition;
    }
}