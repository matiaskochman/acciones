package com.prediccion.acciones.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class CompanyProperty {

    /**
     */
    private String displayName;

    /**
     */
    private String propertyValue;

    /**
     */
    private String field;

    /**
     */
    private String propertyOrder;
}
