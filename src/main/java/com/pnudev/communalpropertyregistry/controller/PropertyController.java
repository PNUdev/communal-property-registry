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
@RequestMapping("/property/admin")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/index")
    public String index(@RequestParam(required = false) String q,
                        @RequestParam(required = false) Long categoryByPurposeId,
                        @RequestParam(required = false) String propertyStatus,
                        @PageableDefault(sort = "name") Pageable pageable,
                        Model model) {

        Page<Property> propertyPage = propertyService.findAll(q, categoryByPurposeId, propertyStatus, pageable);

        model.addAttribute("propertyPage", propertyPage);

        return "admin/common/index";
    }

}
