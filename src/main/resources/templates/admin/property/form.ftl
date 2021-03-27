<#include "../include/header.ftl">

    <#assign addressExample = "Наприклад: вулиця Гуцульська, 18, Коломия, Івано-Франківська область, 78200"/>

<div class="m-2">
    <a class="btn btn-outline-primary" href="/admin/properties">&#8592;</a>
</div>

<div class="w-100">
    <div class="col-md-5 mx-auto mt-5 py-3 px-5 rounded bg-light border">
        <form action="/admin/properties/new" method="POST" id="save_form">

            <div>
                <label for="address" class="form-label">Адреса</label>
                <input name="address" id="address" type="text" class="form-control"
                       value="${(propertyAdminDto.address)!}" required>
                <div id="addressHelpBlock" class="form-text">
                    ${addressExample}
                </div>
            </div>

            <div>
                <label for="image-url" class="form-label">Посилання на зображення</label>
                <input name="imageUrl" id="image-url" class="form-control" value="${(propertyAdminDto.imageUrl)!}">
            </div>

            <div>
                <label for="name" class="form-label">Назва</label>
                <input name="name" type="text" id="name" class="form-control" value="${(propertyAdminDto.name)!}">
            </div>

            <div>
                <label for="categoryByPurpose" class="form-label">Категорія за призначенням</label>
                <select name="categoryByPurposeId" class="form-select" id="categoryByPurpose" required>
                    <#list categoriesByPurpose as categoryByPurpose>
                        <option value="${categoryByPurpose.id}"
                                <#if propertyAdminDto?? && categoryByPurpose.name == propertyAdminDto.categoryByPurposeName>selected</#if>>
                            ${categoryByPurpose.name}
                        </option>
                    </#list>
                </select>
            </div>

            <div>
                <label for="propertyStatus" class="form-label">Статус</label>
                <select name="propertyStatus" class="form-select" id="propertyStatus" required>
                    <option value="RENT"
                            <#if propertyAdminDto?? && propertyAdminDto.propertyStatus == "RENT">selected</#if>>
                        Орендовані
                    </option>

                    <option value="NON_RENT"
                            <#if propertyAdminDto?? && propertyAdminDto.propertyStatus == "NON_RENT">selected</#if>>
                        Не орендовані
                    </option>

                    <option value="FIRST_OR_SECOND_TYPE_LIST"
                            <#if propertyAdminDto?? && propertyAdminDto.propertyStatus == "FIRST_OR_SECOND_TYPE_LIST">selected</#if>>
                        I-II типу
                    </option>

                    <option value="PRIVATIZED"
                            <#if propertyAdminDto?? && propertyAdminDto.propertyStatus == "PRIVATIZED">selected</#if>>
                        Приватизовані
                    </option>

                    <option value="USED_BY_CITY_COUNCIL"
                            <#if propertyAdminDto?? && propertyAdminDto.propertyStatus == "USED_BY_CITY_COUNCIL">selected</#if>>
                        Вик. міськвладою
                    </option>
                </select>
            </div>

            <div>
                <label for="area" class="form-label">Площа</label>
                <input name="area" type="number" class="form-control" step="0.01" id="area"
                       value="${(propertyAdminDto.area?string("0.00;; decimalSeparator='.'"))!}" required>
            </div>

            <div>
                <label for="areaTransferred" class="form-label">Передана площа</label>
                <div class="input-group">
                    <input name="areaTransferred" type="number" step="0.01" class="form-control" id="areaTransferred"
                           value="${(propertyAdminDto.areaTransferred?string("0.00;; decimalSeparator='.'"))!}">

                    <div class="input-group-text">
                        <label class="form-check-label" for="checkbox1">Публічні дані?&#x00A0;&#x00A0;</label>
                        <input name="areaTransferredPubliclyViewable" class="form-check-input" type="checkbox"
                               id="checkbox1"
                               <#if propertyAdminDto?? && propertyAdminDto.areaTransferredPubliclyViewable>checked</#if>/>
                    </div>
                </div>
            </div>

            <div>
                <label for="balanceHolder" class="form-label">Балансоутримувач</label>
                <div class="input-group" id="balanceHolder">
                    <input name="balanceHolder" type="text" class="form-control"
                           value="${(propertyAdminDto.balanceHolder)!}" required>

                    <div class="input-group-text">
                        <label class="form-check-label" for="checkbox2">Публічні дані?&#x00A0;&#x00A0;</label>
                        <input name="balanceHolderPubliclyViewable" class="form-check-input" type="checkbox"
                               id="checkbox2"
                               <#if propertyAdminDto?? && propertyAdminDto.balanceHolderPubliclyViewable>checked</#if>/>
                    </div>
                </div>
            </div>

            <div>
                <label for="owner" class="form-label">Власник</label>
                <div class="input-group" id="owner">
                    <input name="owner" type="text" class="form-control" value="${(propertyAdminDto.owner)!}" required>

                    <div class="input-group-text">
                        <label class="form-check-label" for="checkbox3">Публічні дані?&#x00A0;&#x00A0;</label>
                        <input name="ownerPubliclyViewable" class="form-check-input" type="checkbox" id="checkbox3"
                               <#if propertyAdminDto?? && propertyAdminDto.ownerPubliclyViewable>checked</#if>/>
                    </div>
                </div>
            </div>

            <div>
                <label for="leaseAgreementEndDate" class="form-label">Дата закінчення договору оренди</label>
                <div class="input-group" id="leaseAgreementEndDate">
                    <input name="leaseAgreementEndDate" type="date" max="3000-01-01" class="form-control"
                           value="${(propertyAdminDto.leaseAgreementEndDate)!}">

                    <div class="input-group-text">
                        <label class="form-check-label" for="checkbox4">Публічні дані?&#x00A0;&#x00A0;</label>
                        <input name="leaseAgreementEndDatePubliclyViewable" class="form-check-input" type="checkbox"
                               id="checkbox4"
                               <#if propertyAdminDto?? && propertyAdminDto.leaseAgreementEndDatePubliclyViewable>checked</#if>/>
                    </div>
                </div>
            </div>

            <div>
                <label for="amountOfRent" class="form-label">Сума за оренду</label>
                <div class="input-group">
                    <input name="amountOfRent" type="number" step="0.01" class="form-control" id="amountOfRent"
                           value="${(propertyAdminDto.amountOfRent?string("0.00;; decimalSeparator='.'"))!}">

                    <div class="input-group-text">
                        <label class="form-check-label" for="checkbox5">Публічні дані?&#x00A0;&#x00A0;</label>
                        <input name="amountOfRentPubliclyViewable" class="form-check-input" type="checkbox"
                               id="checkbox5"
                               <#if propertyAdminDto?? && propertyAdminDto.amountOfRentPubliclyViewable>checked</#if>/>
                    </div>
                </div>
            </div>

            <div>
                <div>Попередній перегляд зображення</div>
                <div id="image-preview-block">
                    <img src="" class="mw-100 mh-100" id="image-display" alt="Попередній перегляд зображення">
                </div>
                <div id="image-loading-error">
                    <div class="h2">Зображення не знайдено</div>
                </div>
            </div>

            <div class="d-flex justify-content-evenly mt-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <#if propertyAdminDto??>
                    <button type="submit" class="btn btn-outline-primary" formaction="/admin/properties/update/${propertyAdminDto.id}">
                        Оновити
                    </button>
                    <a href="/admin/properties/delete/#{propertyAdminDto.id}" class="link-danger">Видалити</a>
                <#else >
                    <button type="submit" class="btn btn-outline-primary">Зберегти</button>
                </#if>
            </div>
        </form>
    </div>
</div>

<script>

    let imageUrl = document.getElementById('image-url');
    let imageDisplay = document.getElementById('image-display');
    let imagePreviewBlock = document.getElementById('image-preview-block');
    let imageLoadingErrorBlock = document.getElementById('image-loading-error');

    let setImageSrcUrl = function () {
        imageDisplay.src = imageUrl.value;
    };

    imageUrl.addEventListener('change', setImageSrcUrl);
    document.addEventListener('DOMContentLoaded', setImageSrcUrl);

    imageDisplay.addEventListener('load', function () {
        imagePreviewBlock.hidden = false;
        imageLoadingErrorBlock.hidden = true;
    });

    imageDisplay.addEventListener('error', function () {
        imagePreviewBlock.hidden = true;
        imageLoadingErrorBlock.hidden = false;
    });
</script>

<#include "../../include/footer.ftl">