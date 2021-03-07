package com.pnudev.communalpropertyregistry.dto;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryByPurposePaginationDto {

    private List<CategoryByPurpose> content;

    private Integer page;

    private Integer totalPages;

    private Integer firstVisiblePage;

    private Integer lastVisiblePage;

    private Boolean isFirstPage;

    private Boolean isLastPage;

}