// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.prediccion.acciones.controller;

import com.prediccion.acciones.controller.CompanyController;
import com.prediccion.acciones.domain.Company;
import com.prediccion.acciones.service.CompanyPropertyService;
import com.prediccion.acciones.service.CompanyService;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect CompanyController_Roo_Controller {
    
    @Autowired
    CompanyService CompanyController.companyService;
    
    @Autowired
    CompanyPropertyService CompanyController.companyPropertyService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CompanyController.create(@Valid Company company, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, company);
            return "companys/create";
        }
        uiModel.asMap().clear();
        companyService.saveCompany(company);
        return "redirect:/companys/" + encodeUrlPathSegment(company.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CompanyController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Company());
        return "companys/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CompanyController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("company", companyService.findCompany(id));
        uiModel.addAttribute("itemId", id);
        return "companys/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CompanyController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("companys", companyService.findCompanyEntries(firstResult, sizeNo));
            float nrOfPages = (float) companyService.countAllCompanys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("companys", companyService.findAllCompanys());
        }
        return "companys/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CompanyController.update(@Valid Company company, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, company);
            return "companys/update";
        }
        uiModel.asMap().clear();
        companyService.updateCompany(company);
        return "redirect:/companys/" + encodeUrlPathSegment(company.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CompanyController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, companyService.findCompany(id));
        return "companys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CompanyController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Company company = companyService.findCompany(id);
        companyService.deleteCompany(company);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/companys";
    }
    
    void CompanyController.populateEditForm(Model uiModel, Company company) {
        uiModel.addAttribute("company", company);
        uiModel.addAttribute("companypropertys", companyPropertyService.findAllCompanyPropertys());
    }
    
    String CompanyController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
