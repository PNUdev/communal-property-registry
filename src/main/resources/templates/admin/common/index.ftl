<#include "../include/header.ftl">

<#assign searchUrl = searchQuery???then("&q=${searchQuery}","")
+ searchCategoryByPurposeId???then("&category=${searchCategoryByPurposeId}", "")
+ searchPropertyStatus???then("&status=${searchPropertyStatus}", "")/>

<div class="container">

    <#if propertiesPage.number lt 0 || (propertiesPage.totalPages !=0 && propertiesPage.number gt propertiesPage.totalPages - 1)>
        <h1 class="text-center">Неіснуючий номер сторінки</h1>
        <a href="/admin/properties">
            <h2 class="text-center">Список майна</h2>
        </a>
    <#else>
        <div class="col-md-8 mx-auto mt-5 py-3 px-5 rounded bg-light border">
            <form id="search_form">

                <div class="row g-0">
                    <div class="col-md-7">
                        <label for="search" class="visually-hidden">Пошук</label>
                        <input name="q" type="search" class="form-control" id="search" placeholder="Пошук">
                    </div>

                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">&#9906;</button>
                    </div>

                    &#x00A0;&#x00A0;
                    <div class="col-auto">
                        <a href="/api/property/admin/report<#if searchUrl?length gt 1>?${searchUrl?substring(1)}</#if>"
                           class="btn-outline-primary mb-3" download>Завантажити звіт</a>
                    </div>
                </div>

                <div class="row g-2 mt-1">
                    <div class="col-auto">
                        <select name="status" id="status" class="form-select">
                            <option value="all" selected>Будь-який статус</option>
                            <option value="RENT">Орендовані</option>
                            <option value="NON_RENT">Не орендовані</option>
                            <option value="FIRST_OR_SECOND_TYPE_LIST">I-II типу</option>
                            <option value="PRIVATIZED">Приватизовані</option>
                            <option value="USED_BY_CITY_COUNCIL">Вик. міськвладою</option>
                        </select>
                    </div>

                    <div class="col-auto">
                        <select name="category" id="category" class="form-select">
                            <option value="-1" selected>Будь-яка категорія</option>
                            <#list categoriesByPurpose as categoryByPurpose>
                                <option value="${categoryByPurpose.id}">${categoryByPurpose.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>

            </form>
        </div>

        <div class="col-md-8 mx-auto py-3 d-flex justify-content-between">
            <a href="/admin/properties/new">
                <div class="btn btn-primary btn-block">Додати нове майно</div>
            </a>
            <span class="text-success">*-дані які показуються публічно</span>
            <span class="text-danger">*-дані які приховані</span>
        </div>

        <div class="py-1">
            <#list propertiesPage.content as property>
                <div class="row my-3 bg-light rounded p-3">

                    <div class="col-md-4">
                        <img src="${property.imageUrl}" alt="Ілюстрація майна" class="mw-100 mh-100">
                    </div>

                    <div class="col-md-5 d-flex flex-column col-md-5 justify-content-between">
                        <div class="h3">
                            Назва: ${property.name}
                        </div>

                        <div>
                            Адреса: ${property.address}
                        </div>

                        <div>
                            Площа: ${property.area}
                        </div>

                        <div class="<#if property.areaTransferredPubliclyViewable>text-success<#else>text-danger</#if>">
                            Передана площа: ${property.areaTransferred}
                        </div>

                        <div class="<#if property.balanceHolderPubliclyViewable>text-success<#else>text-danger</#if>">
                            Балансо утримувач: ${property.balanceHolder}
                        </div>

                        <div class="<#if property.ownerPubliclyViewable>text-success<#else>text-danger</#if>">
                            Власник: ${property.owner}
                        </div>

                        <div class="<#if property.leaseAgreementEndDatePubliclyViewable>text-success<#else>text-danger</#if>">
                            Оренда дійсна до: ${property.leaseAgreementEndDate}
                        </div>

                        <div class="<#if property.amountOfRentPubliclyViewable>text-success<#else>text-danger</#if>">
                            Сума за оренду: #{property.amountOfRent; m2}
                        </div>
                    </div>

                    <div class="col-md-3 d-flex flex-column col-md-3 justify-content-between">
                        <div>
                            Статус:
                            <#switch property.propertyStatus>
                                <#case "RENT">
                                    Орендовано
                                    <#break>
                                <#case "NON_RENT">
                                    Неорендовано
                                    <#break>
                                <#case "PRIVATIZED">
                                    Приватизовано
                                    <#break>
                                <#case "FIRST_OR_SECOND_TYPE_LIST">
                                    I-II типу
                                    <#break>
                                <#case "USED_BY_CITY_COUNCIL">
                                    Вик. міськвладою
                                    <#break>
                            </#switch>
                        </div>

                        <div>
                            Категорія за призначенням: ${property.categoryByPurposeName}
                        </div>

                        <a href="/admin/attachments/property/${property.id}" class="pt-3">
                            Показати прикріплення
                        </a>

                        <a href="/admin/properties/update/${property.id}" class="pt-3">
                            <div class="btn btn-primary">Редагувати</div>
                        </a>
                    </div>
                </div>
            </#list>
        </div>

        <#if propertiesPage.number==0 && !propertiesPage.content?has_content>
            <h1 class="text-center mt-5">
                <#if searchUrl?length == 0>
                    Список майна пустий
                <#else>
                    Результатів не знайдено
                </#if>
            </h1>
        <#else>
            <nav>
                <ul class="pagination justify-content-center mt-3">

                    <li class="page-item <#if propertiesPage.number == 0 >disabled</#if>">
                        <a class="page-link" href="?page=${propertiesPage.number}${searchUrl}">
                            Попередня сторінка
                        </a>
                    </li>

                    <#list 1..propertiesPage.totalPages as pageNumber>
                        <li class="page-item <#if propertiesPage.number == pageNumber - 1>active</#if>">
                            <a class="page-link" href="?page=${pageNumber}${searchUrl}">
                                ${pageNumber}
                            </a>
                        </li>
                    </#list>

                    <li class="page-item <#if propertiesPage.number == propertiesPage.totalPages - 1 >disabled</#if>">
                        <a class="page-link" href="?page=${propertiesPage.number + 2}${searchUrl}">
                            Наступна сторінка
                        </a>
                    </li>

                </ul>
            </nav>
        </#if>
    </#if>
</div>

<script>

    let search_form = document.getElementById("search_form");

    search_form.onsubmit = function () {

        let queryParams = new URLSearchParams(location.search);
        let inputs = document.getElementById("search_form").elements;

        let search = document.getElementById("search");
        let category = document.getElementById("category");
        let status = document.getElementById("status");

        queryParams.delete("q");
        queryParams.delete("category");
        queryParams.delete("status");

        if (inputs["q"] && inputs["q"].value !== "") {
            queryParams.set("q", inputs["q"].value);
        } else {
            search.disabled = true;
        }

        if (inputs["category"] && inputs["category"].value !== "-1") {
            queryParams.set("category", inputs["category"].value);
        } else {
            category.disabled = true;
        }

        if (inputs["status"] && inputs["status"].value !== "all") {
            queryParams.set("status", inputs["status"].value);
        } else {
            status.disabled = true;
        }

        history.replaceState(null, null, '?' + queryParams);
        search_form.submit();
    }
</script>

<#include "../../include/footer.ftl">
