// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.prediccion.acciones.domain;

import com.prediccion.acciones.domain.Company;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Company_Roo_Json {
    
    public String Company.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String Company.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Company Company.fromJsonToCompany(String json) {
        return new JSONDeserializer<Company>()
        .use(null, Company.class).deserialize(json);
    }
    
    public static String Company.toJsonArray(Collection<Company> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String Company.toJsonArray(Collection<Company> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Company> Company.fromJsonArrayToCompanys(String json) {
        return new JSONDeserializer<List<Company>>()
        .use("values", Company.class).deserialize(json);
    }
    
}
