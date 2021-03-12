<#include '../include/header.ftl'>

<div class="m-2">
    <a class="btn btn-outline-primary" href="/admin/attachmentCategories">&#8592;</a>
</div>

<div>
    <form action="/admin/attachmentCategories/new" method="post">
        <h2 class="text-center">Створити категорію</h2>
        <div class="d-flex justify-content-center">
            <label class="form-label" for="name">Назва категорії&#x00A0;&#x00A0;</label>
            <input type="text" name="name" id="name" class="mb-2" placeholder="Наприклад: файл " required />
        </div>

        <div class="d-flex justify-content-center">
            <label class="form-check-label" for="checkbox">Публічні дані?&#x00A0;&#x00A0;</label>
            <input type="checkbox" id="checkbox" class="form-check-input" name="PubliclyViewable" />
        </div>

        <div class="d-flex flex-row justify-content-center">
            <button class="btn btn-info m-2">Зберегти</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </div>

    </form>
</div>

<#include '../../include/footer.ftl'>