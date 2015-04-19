// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.prediccion.acciones.domain;

import com.prediccion.acciones.domain.CompanyProperty;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect CompanyProperty_Roo_Json {
    
    public String CompanyProperty.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String CompanyProperty.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static CompanyProperty CompanyProperty.fromJsonToCompanyProperty(String json) {
        return new JSONDeserializer<CompanyProperty>()
        .use(null, CompanyProperty.class).deserialize(json);
    }
    
    public static String CompanyProperty.toJsonArray(Collection<CompanyProperty> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String CompanyProperty.toJsonArray(Collection<CompanyProperty> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CompanyProperty> CompanyProperty.fromJsonArrayToCompanyPropertys(String json) {
        return new JSONDeserializer<List<CompanyProperty>>()
        .use("values", CompanyProperty.class).deserialize(json);
    }
    
}