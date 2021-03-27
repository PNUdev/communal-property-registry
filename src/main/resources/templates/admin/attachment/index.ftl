<#include "../include/header.ftl">

<#if SUCCESS_FLASH_MESSAGE?? || ERROR_FLASH_MESSAGE??>
    <div class="alert mt-4 col-md-10 mx-auto py-3 <#if SUCCESS_FLASH_MESSAGE??>alert-success<#else>alert-danger</#if>"
         id="popup" role="alert">
        ${(SUCCESS_FLASH_MESSAGE)!}${(ERROR_FLASH_MESSAGE)!}
    </div>
</#if>

<div class="col-md-10 mx-auto py-3">

    <div class="row">

        <div class="col-auto">
            <a class="btn btn-outline-primary" href="/admin/properties">&#8592;</a>
        </div>

        <div class="h4 col">
            Назва майна: ${(property.name)!"-"}
        </div>

        <div class="h4 col">
            Адреса майна: ${property.address}
        </div>

        <div class="col">
            <a class="btn btn-outline-primary" href="/admin/properties/#{property.id}/attachments/new">
                Створити нове прикріплення
            </a>
        </div>
    </div>

    <hr>

    <#if !(attachments?has_content)>
        <p class="h4 text-center">
            Прикріплення відсутні!
        </p>
    <#else >
        <table class="table table-striped table-hover mt-5">
            <thead>
            <tr>
                <th>#</th>
                <th>Категорія прикріплення</th>
                <th>Категорія прикріплення публічно видима</th>
                <th>Примітка</th>
                <th>Посилання</th>
                <th>Прикріплення публічно видиме</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <#list attachments as attachment>
                <tr>
                    <td>${attachment.id}</td>
                    <td>${attachment.attachmentCategoryName}</td>
                    <td>${attachment.attachmentCategoryPubliclyViewable?string("Так","Ні")}</td>
                    <td>${(attachment.note)!"-"}</td>
                    <td>
                        <#if attachment?? && attachment.link?has_content>
                            <a href="${attachment.link}">Перейти за посиланням</a>
                        <#else >
                            Посилання відсутнє
                        </#if>
                    </td>
                    <td>
                        <#if attachment.attachmentCategoryPubliclyViewable>
                            ${attachment.publiclyViewable?string("Так","Ні")}<#else >-</#if>
                    </td>
                    <td>
                        <a class="btn btn-outline-primary"
                           href="/admin/properties/#{property.id}/attachments/edit/#{attachment.id}">
                            Редагувати
                        </a>
                    </td>
                    <td>
                        <a class="btn btn-outline-danger" href="/admin/properties/#{property.id}/attachments/delete/#{attachment.id}">
                            Видалити
                        </a>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </#if>

</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        setTimeout(() => document.getElementById("popup").hidden = true, 5000);
    });
</script>

<#include "../../include/footer.ftl">
