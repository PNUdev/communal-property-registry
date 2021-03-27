<#include "../include/header.ftl">

<div class="m-2">
    <a class="btn btn-outline-primary" href="/admin/properties">&#8592;</a>
</div>

<div class="w-100">
    <div class="col-md-5 mx-auto mt-5 py-3 px-5 rounded bg-light border">
        <form method="POST" id="attachmentForm">

            <div>
                <label for="note" class="form-label">Примітка</label>
                <input name="note" id="note" type="text" class="form-control" data-oldvalue="${(attachment.note)!}"
                       value="${(attachment.note)!}">
            </div>

            <div>
                <label for="link" class="form-label">Посилання</label>
                <input name="link" id="link" type="text" class="form-control" data-oldvalue="${(attachment.link)!}"
                       value="${(attachment.link)!}">
            </div>

            <div>
                <label for="attachmentCategory" class="form-label">Категорія прикріплення</label>
                <select name="attachmentCategoryId" class="form-select" id="attachmentCategory" required>
                    <#list attachmentCategories as attachmentCategory>
                        <option value="${attachmentCategory.id}"
                                class="<#if attachmentCategory.publiclyViewable>text-success<#else>text-danger</#if>"

                                <#if attachment?? && attachmentCategory.name == attachment.attachmentCategoryName>
                                    selected data-oldvalue="#{attachmentCategory.id}"
                                </#if>>
                            ${attachmentCategory.name}
                        </option>
                    </#list>
                </select>
            </div>

            <div>
                <label for="publiclyViewableDiv" class="form-label">Налаштування публічності</label>
                <div class="d-flex justify-content-between" id="publiclyViewableDiv">
                    <label class="form-label" for="publiclyViewable">
                        Прикріплення публічно доступне
                    </label>
                    <input name="publiclyViewable" id="publiclyViewable" type="checkbox" class="form-check-input h4"
                           data-oldvalue="${(attachment.publiclyViewable?string("1", "0"))!}"
                           <#if attachment?? && attachment.publiclyViewable>checked</#if>/>
                </div>
            </div>

            <p id="publiclyViewableHint"></p>

            <div class="d-flex justify-content-evenly mt-3">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <#if attachment??>
                    <button class="btn btn-outline-primary"
                            onclick="this.form.action='/admin/properties/#{propertyId}/attachments/update/#{attachment.id}'; submitForm()">
                        Оновити
                    </button>
                    <a href="/admin/properties/#{propertyId}/attachments/delete/#{attachment.id}" class="btn btn-outline-danger">Видалити</a>
                <#else >
                    <button type="submit" class="btn btn-outline-primary">Зберегти</button>
                </#if>
            </div>
        </form>
    </div>
</div>

<script>
    let attachmentForm = document.getElementById("attachmentForm");
    let attachmentCategory = document.getElementById("attachmentCategory");
    let publiclyViewable = document.getElementById("publiclyViewable");

    function attachmentCategoryOnChange() {
        let publiclyViewableHint = document.getElementById("publiclyViewableHint");
        let selectTag = document.getElementById("attachmentCategory");
        let selectedOption = selectTag.options[selectTag.selectedIndex];

        if (selectedOption.classList.contains("text-danger")) {
            publiclyViewable.setAttribute("disabled", "disabled");
            publiclyViewableHint.innerText = "Вибрана категорія прикріплення не є публічно видимою!";
            publiclyViewableHint.classList.add("alert", "alert-info");
            publiclyViewable.checked = false;
        } else {
            publiclyViewable.removeAttribute("disabled");
            publiclyViewableHint.innerText = "";
            publiclyViewableHint.classList.remove("alert", "alert-info");
        }
    }

    document.addEventListener('DOMContentLoaded', attachmentCategoryOnChange);
    attachmentCategory.onchange = attachmentCategoryOnChange;

    function isUnique(elem) {
        return elem.value !== elem.dataset.oldvalue;
    }

    attachmentForm.addEventListener("submit", submitForm);

    function submitForm(event) {
        event.preventDefault();

        let note = document.getElementById("note");
        let link = document.getElementById("link");
        let publiclyViewable = document.getElementById("publiclyViewable");
        let selectTag = document.getElementById("attachmentCategory");
        let selectedOption = selectTag.options[selectTag.selectedIndex];

        if (isUnique(note) || isUnique(link) || !selectedOption.dataset.oldvalue ||
            publiclyViewable.checked != publiclyViewable.dataset.oldvalue) {

            attachmentForm.submit();
        } else {
            if (!document.getElementById("popup")) {
                let msg = "<div class='alert alert-danger text-center' id='popup'>Немає змін</div>";
                attachmentForm.insertAdjacentHTML("beforebegin", msg);

                setTimeout(() => document.getElementById("popup").remove(), 5000);
            }
        }
    }
</script>

<#include "../../include/footer.ftl">
