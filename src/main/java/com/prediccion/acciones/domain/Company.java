package com.prediccion.acciones.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Company {

    /**
     */
    @NotNull
    @Column(unique = true)
    private String title;

    /**
     */
    @NotNull
    @Column(unique = true)
    private String ticker;

    /**
     */
    @NotNull
    private String exchange;

    /**
     */
    @Column(unique = true)
    private String companyId;

    /**
     */
    private String localCurrencySymbol;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<CompanyProperty> properties = new ArrayList<CompanyProperty>();
}
