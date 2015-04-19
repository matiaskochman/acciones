package com.prediccion.acciones.controller;
import com.prediccion.acciones.domain.CompanyProperty;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/companypropertys")
@Controller
@RooWebScaffold(path = "companypropertys", formBackingObject = CompanyProperty.class)
public class CompanyPropertyController {
}
