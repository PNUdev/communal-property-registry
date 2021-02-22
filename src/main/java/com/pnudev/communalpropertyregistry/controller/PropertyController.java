package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.dto.PropertyDto;
import com.pnudev.communalpropertyregistry.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/property")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/partial")
    public String getPartialProperties(@PageableDefault(size = 5, sort = "name") Pageable pageable,
//                                       @RequestParam(name = "p", defaultValue = "0") Integer page,
                                       @RequestParam(name = "q") String searchQuery,
                                       @RequestParam(name = "status") String propertyStatus,
                                       @RequestParam(name = "category") String categoryName,
                                       Model model) {

        Page<PropertyDto> partialPropertyDtos = propertyService.findPropertiesBySearchQuery(
                searchQuery, propertyStatus,
                categoryName, pageable);

        model.addAttribute("propertyDtos", partialPropertyDtos);

        return "property/multiPropertyPartial";
    }

    @GetMapping("/{id}/partial")
    public String getPartialById(@PathVariable Long id, Model model) {

        model.addAttribute("property", propertyService.findById(id));
        return "property/singlePropertyPartial";
    }
}
