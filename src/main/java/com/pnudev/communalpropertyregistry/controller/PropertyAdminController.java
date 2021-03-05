package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/properties")
public class PropertyAdminController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyAdminController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public String findAll(@Nullable @RequestParam(name = "q") String q,
                          @Nullable @RequestParam(name = "category") Long categoryByPurposeId,
                          @Nullable @RequestParam(name = "status") String propertyStatus,
                          @PageableDefault(sort = "name") Pageable pageable,
                          Model model) {

        Page<Property> propertiesPage = propertyService.findAll(q, categoryByPurposeId, propertyStatus, pageable);

        model.addAttribute("propertiesPage", propertiesPage);

        return "admin/common/index";
    }

}
