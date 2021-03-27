package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import com.pnudev.communalpropertyregistry.service.PropertyAdminService;
import com.pnudev.communalpropertyregistry.util.AddressResolverClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.pnudev.communalpropertyregistry.util.FlashMessageConstants.SUCCESS_FLASH_MESSAGE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/admin/properties")
public class PropertyAdminController {

    private final AddressResolverClient addressResolverClient;

    private final PropertyAdminService propertyAdminService;

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public PropertyAdminController(AddressResolverClient addressResolverClient,
                                   PropertyAdminService propertyAdminService,
                                   CategoryByPurposeService categoryByPurposeService) {

        this.addressResolverClient = addressResolverClient;
        this.propertyAdminService = propertyAdminService;
        this.categoryByPurposeService = categoryByPurposeService;
    }

    @GetMapping
    public String findAll(@Nullable @RequestParam(name = "q") String searchQuery,
                          @Nullable @RequestParam(name = "category") Long categoryByPurposeId,
                          @Nullable @RequestParam(name = "status") String propertyStatus,
                          @PageableDefault Pageable pageable,
                          Model model) {

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.findAll();

        Page<PropertyAdminDto> propertiesAdminPage = propertyAdminService.findAll(
                searchQuery, categoryByPurposeId, propertyStatus, pageable);

        model.addAttribute("categoriesByPurpose", categoriesByPurpose);
        model.addAttribute("propertiesPage", propertiesAdminPage);

        if (nonNull(searchQuery)) {
            model.addAttribute("searchQuery", searchQuery);
        }

        if (nonNull(categoryByPurposeId)) {
            model.addAttribute("searchCategoryByPurposeId", categoryByPurposeId);
        }

        if (nonNull(propertyStatus)) {
            model.addAttribute("searchPropertyStatus", propertyStatus);
        }

        return "admin/property/index";
    }

    @GetMapping("/new")
    public String create(Model model) {
        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.findAll();

        model.addAttribute("categoriesByPurpose", categoriesByPurpose);
        return "admin/property/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable(name = "id") Long id, Model model) {

        PropertyAdminDto propertyAdminDto = propertyAdminService.findById(id);
        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.findAll();

        model.addAttribute("propertyAdminDto", propertyAdminDto);
        model.addAttribute("categoriesByPurpose", categoriesByPurpose);

        return "admin/property/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirmation(@PathVariable(name = "id") Long id, Model model) {

        model.addAttribute("message", "Ви впевнені, що хочете видалити майно?");
        model.addAttribute("returnBackUrl", "/admin/properties/update/" + id);

        return "admin/common/deleteConfirmation";
    }

    @GetMapping("/addresses")
    public String choiceAddress(Model model) {

        if (!model.containsAttribute("propertyAdminFormDto")) {
            return "redirect:/admin/properties";
        }

        return "/admin/property/address";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id,
                         PropertyAdminFormDto propertyAdminFormDto,
                         RedirectAttributes redirectAttributes,
                         AddressDto addressDto) {

        if (isNull(addressDto.getLat()) || isNull(addressDto.getLon())) {
            List<AddressDto> addresses = addressResolverClient.getAddressesDto(propertyAdminFormDto.getAddress());
            if (addresses.size() != 1) {
                redirectAttributes.addFlashAttribute("addressesDto", addresses);
                redirectAttributes.addFlashAttribute("propertyAdminFormDto", propertyAdminFormDto);
                redirectAttributes.addFlashAttribute("propertyId", id);
                return "redirect:/admin/properties/addresses";
            }
            addressDto = addresses.get(0);
        }

        propertyAdminService.updateById(id, propertyAdminFormDto, addressDto);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Майно усішно оновлено!");

        return "redirect:/admin/properties";
    }

    @PostMapping("/new")
    public String create(PropertyAdminFormDto propertyAdminFormDto,
                         AddressDto addressDto,
                         RedirectAttributes redirectAttributes) {

        if (isNull(addressDto.getLat()) || isNull(addressDto.getLon())) {
            List<AddressDto> addresses = addressResolverClient.getAddressesDto(propertyAdminFormDto.getAddress());
            if (addresses.size() != 1) {
                redirectAttributes.addFlashAttribute("addressesDto", addresses);
                redirectAttributes.addFlashAttribute("propertyAdminFormDto", propertyAdminFormDto);
                return "redirect:/admin/properties/addresses";
            }
            addressDto = addresses.get(0);
        }

        propertyAdminService.create(propertyAdminFormDto, addressDto);
        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Майно успішно створено!");

        return "redirect:/admin/properties";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {

        propertyAdminService.deleteById(id);

        redirectAttributes.addFlashAttribute(SUCCESS_FLASH_MESSAGE.name(), "Майно успішно видалено!");

        return "redirect:/admin/properties";
    }

}
