<#include '../include/header.ftl'>

<div class="m-2">
    <a class="btn btn-outline-primary" href="/admin/attachment-categories/${attachmentCategory.id}">&#8592;</a>
</div>

<div>
    <form action="/admin/attachment-categories/edit/${attachmentCategory.id}" method="post">
        <h2 class="text-center">Оновити категорію</h2>
        <div class="d-flex justify-content-center">
            <label class="form-label" for="name">Назва категорії&#x00A0;&#x00A0;</label>
            <input type="text" name="name" id="name" class="mb-2" value="${attachmentCategory.name}" required>
        </div>

        <div class="d-flex justify-content-center">
            <label class="form-check-label" for="checkbox">Публічні дані?&#x00A0;&#x00A0;</label>
            <input type="checkbox" id="checkbox" class="form-check-input" name="PubliclyViewable"
                   <#if attachmentCategory.publiclyViewable> checked </#if> />
        </div>

        <div class="d-flex flex-row justify-content-center">
            <button class="btn btn-primary m-1">Зберегти</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </div>

    </form>
</div>

<#include '../../include/footer.ftl'>