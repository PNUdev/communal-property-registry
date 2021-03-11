package com.pnudev.communalpropertyregistry.dto;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryByPurposeResponseDto {

    private List<CategoryByPurpose> categoriesByPurpose;

}