<#include "../include/header.ftl">

<#assign searchUrl = searchQuery???then("&q=${searchQuery}","")
+ searchCategoryByPurposeId???then("&category=${searchCategoryByPurposeId}", "")
+ searchPropertyStatus???then("&status=${searchPropertyStatus}", "")/>

<div class="container">

    <#if SUCCESS_FLASH_MESSAGE?? || ERROR_FLASH_MESSAGE??>
        <div class="alert mt-4 <#if SUCCESS_FLASH_MESSAGE??>alert-success<#else>alert-danger</#if>" id="popup" role="alert">
            ${(SUCCESS_FLASH_MESSAGE)!}${(ERROR_FLASH_MESSAGE)!}
        </div>
    </#if>

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
                        <input name="q" type="search" class="form-control" id="search" value="${(searchQuery)!}" placeholder="Пошук">
                    </div>

                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">&#9906;</button>
                    </div>

                    &#x00A0;&#x00A0;
                    <div class="col-auto">
                        <a href="/api/properties/admin/report<#if searchUrl?length gt 1>?${searchUrl?substring(1)}</#if>"
                           class="btn-outline-primary mb-3" download>Завантажити звіт</a>
                    </div>
                </div>

                <div class="row g-2 mt-1">
                    <div class="col-auto">
                        <select name="status" id="status" class="form-select">
                            <option value="all" <#if !searchPropertyStatus??>selected</#if>>
                                Будь-який статус</option>
                            <option value="RENT" <#if searchPropertyStatus?? && searchPropertyStatus = "RENT">selected</#if>>
                                Орендовані</option>
                            <option value="NON_RENT" <#if searchPropertyStatus?? && searchPropertyStatus = "NON_RENT">selected</#if>>
                                Не орендовані</option>
                            <option value="FIRST_OR_SECOND_TYPE_LIST" <#if searchPropertyStatus?? && searchPropertyStatus = "FIRST_OR_SECOND_TYPE_LIST">selected</#if>>
                                I-II типу</option>
                            <option value="PRIVATIZED" <#if searchPropertyStatus?? && searchPropertyStatus = "PRIVATIZED">selected</#if>>
                                Приватизовані</option>
                            <option value="USED_BY_CITY_COUNCIL" <#if searchPropertyStatus?? && searchPropertyStatus = "USED_BY_CITY_COUNCIL">selected</#if>>
                                Вик. міськвладою</option>
                        </select>
                    </div>

                    <div class="col-auto">
                        <select name="category" id="category" class="form-select">
                            <option value="-1" <#if !searchCategoryByPurposeId??>selected</#if>>Будь-яка категорія</option>
                            <#list categoriesByPurpose as categoryByPurpose>
                                <option value="${categoryByPurpose.id}" <#if searchCategoryByPurposeId?? && searchCategoryByPurposeId = categoryByPurpose.id>selected</#if>>
                                    ${categoryByPurpose.name}</option>
                            </#list>
                        </select>
                    </div>

                    <div class="col-auto">
                        <a href="/admin/properties" class="h3 text-decoration-none">&#128473;</a>
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

        <div class="h-4 p-1">
            Знайдено <b>#{propertiesPage.totalElements}</b> результат(ів):
        </div>

        <div class="py-1">
            <#list propertiesPage.content as property>
                <div class="row my-3 bg-light rounded p-3">

                    <div class="col-md-4">
                        <img src="${(property.imageUrl)!}" alt="Ілюстрація майна" class="mw-100 mh-100 w-100" onerror="this.src='/images/default_img.png'">
                    </div>

                    <div class="col-md-5 d-flex flex-column col-md-5 justify-content-between">
                        <div class="h3">
                            Назва: ${(property.name)!"-"}
                        </div>

                        <div>
                            Адреса: ${property.address}
                        </div>

                        <div>
                            Площа: #{property.area; m2} м<sup>2</sup>
                        </div>

                        <div class="<#if property.areaTransferredPubliclyViewable>text-success<#else>text-danger</#if>">
                            Передана площа: <#if property.areaTransferred??>#{property.areaTransferred; m2} м<sup>2</sup><#else>-</#if>
                        </div>

                        <div class="<#if property.balanceHolderPubliclyViewable>text-success<#else>text-danger</#if>">
                            Балансо утримувач: ${property.balanceHolder}
                        </div>

                        <div class="<#if property.ownerPubliclyViewable>text-success<#else>text-danger</#if>">
                            Власник: ${property.owner}
                        </div>

                        <div class="<#if property.leaseAgreementEndDatePubliclyViewable>text-success<#else>text-danger</#if>">
                            Оренда дійсна до: ${(property.leaseAgreementEndDate)!"-"}
                        </div>

                        <div class="<#if property.amountOfRentPubliclyViewable>text-success<#else>text-danger</#if>">
                            Сума за оренду: <#if property.amountOfRent??>#{property.amountOfRent; m2}<#else>-</#if>
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

                        <a href="/admin/properties/${property.id}/attachments" class="pt-3">
                            Показати прикріплення
                        </a>

                        <a href="/admin/properties/edit/${property.id}" class="pt-3">
                            <div class="btn btn-primary">Редагувати</div>
                        </a>
                    </div>
                </div>
            </#list>
        </div>

        <#if propertiesPage.number == 0 && !propertiesPage.content?has_content>
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
                            &#129040;
                        </a>
                    </li>

                    <#function max x y>
                        <#if (x<y)><#return y><#else><#return x></#if>
                    </#function>
                    <#function min x y>
                        <#if (x<y)><#return x><#else><#return y></#if>
                    </#function>
                    <#assign p = propertiesPage.number + 1/>
                    <#assign size = propertiesPage.totalPages/>
                    <#if (p<=4)>
                        <#assign interval = 1..(min(5,size))>
                    <#elseif ((size-p)<4)>
                        <#assign interval = (max(1,(size-4)))..size >
                    <#else>
                        <#assign interval = (p-2)..(p+2)>
                    </#if>
                    <#if !(interval?seq_contains(1))>
                        <li class="page-item">
                            <a class="page-link" href="?page=0${searchUrl}">
                                1
                            </a>
                        </li>
                        <li class="page-item disabled">
                            <a class="page-link">
                                ...
                            </a>
                        </li>
                    </#if>
                    <#list interval as page>
                        <li class="page-item <#if page = p>active</#if>">
                            <a class="page-link" href="?page=${page}${searchUrl}">
                                ${page}
                            </a>
                        </li>
                    </#list>
                    <#if !(interval?seq_contains(size))>
                        <li class="page-item disabled">
                            <a class="page-link">
                                ...
                            </a>
                        </li>
                        <li class="page-item">
                            <a class="page-link" href="?page=${size}${searchUrl}">
                                ${size}
                            </a>
                        </li>
                    </#if>
                    <li class="page-item <#if propertiesPage.number == propertiesPage.totalPages - 1 >disabled</#if>">
                        <a class="page-link" href="?page=${propertiesPage.number + 2}${searchUrl}">
                            &#129042;
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

    document.addEventListener('DOMContentLoaded', function () {
        let modal = document.getElementById("popup");
        setTimeout(function () {
            modal.hidden = true;
        }, 5000);
    });
</script>

<#include "../../include/footer.ftl">
