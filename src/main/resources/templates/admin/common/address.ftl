<#include "../include/header.ftl">

<div class="container py-5">

    <label for="address" class="form-label">Уточніть яку ви хотіли ввести адресу з списку:</label>
    <form action="/admin/properties/save" method="post" id="address_form" onsubmit="setValues()" class="form-control">
        <select name="address" class="form-select" id="address">
            <#list addressesResponseDto.addresses as address>
                <option lat="${address.lat}" lon="${address.lon}">${address.address}</option>
            </#list>
        </select>

        <input type="hidden" name="lat" id="lat">
        <input type="hidden" name="lon" id="lon">

        <input name="imageUrl" type="hidden" value="${propertyAdminFormDto.imageUrl}">
        <input name="name" type="hidden" value="${propertyAdminFormDto.name}">
        <input name="categoryByPurposeId" type="hidden" value="#{propertyAdminFormDto.categoryByPurposeId}">
        <input name="propertyStatus" type="hidden" value="${propertyAdminFormDto.propertyStatus}">
        <input name="area" type="hidden" value="#{propertyAdminFormDto.area}" id="area">
        <input name="areaTransferred" type="hidden" value="#{propertyAdminFormDto.areaTransferred}" id="areaTransferred">
        <input name="areaTransferredPubliclyViewable" type="checkbox" hidden <#if propertyAdminFormDto.areaTransferredPubliclyViewable>checked</#if>/>
        <input name="balanceHolder" type="hidden" value="${propertyAdminFormDto.balanceHolder}" >
        <input name="balanceHolderPubliclyViewable" type="checkbox" hidden <#if propertyAdminFormDto.balanceHolderPubliclyViewable>checked</#if>/>
        <input name="owner" type="hidden" value="${propertyAdminFormDto.owner}">
        <input name="ownerPubliclyViewable" type="checkbox" hidden <#if propertyAdminFormDto.ownerPubliclyViewable>checked</#if>/>
        <input name="leaseAgreementEndDate" type="hidden" value="${propertyAdminFormDto.leaseAgreementEndDate}">
        <input name="leaseAgreementEndDatePubliclyViewable" type="checkbox" hidden <#if propertyAdminFormDto.leaseAgreementEndDatePubliclyViewable>checked</#if>/>
        <input name="amountOfRent" type="hidden" value="#{propertyAdminFormDto.amountOfRent}" id="amountOfRent">
        <input name="amountOfRentPubliclyViewable" type="checkbox" hidden <#if propertyAdminFormDto.amountOfRentPubliclyViewable>checked</#if>/>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary mt-2">Вибрати</button>
    </form>
</div>

<script>
    function setValues() {
        let selectedElem = document.forms['address_form'].elements['address'].options[document.forms['address_form'].elements['address'].selectedIndex];

        document.getElementById("lat").value = selectedElem.getAttribute('lat').replace(",", ".");
        document.getElementById("lon").value = selectedElem.getAttribute('lon').replace(",", ".");

        document.getElementById("amountOfRent").value = document.getElementById("amountOfRent").value.replace(",", ".");
        document.getElementById("area").value = document.getElementById('area').value.replace(",", ".");
        document.getElementById("areaTransferred").value = document.getElementById('areaTransferred').value.replace(",", ".");

        document.getElementById("address_form").submit();
    }
</script>

<#include "../../include/footer.ftl">