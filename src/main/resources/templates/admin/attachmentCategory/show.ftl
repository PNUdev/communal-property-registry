<#include '../include/header.ftl'>

<div class="m-2">
    <a class="btn btn-outline-primary" href="/admin/attachment-categories">&#8592;</a>
</div>

<div>
    <h2 class="text-center">Інформація про категорію </h2>
    <h3 class="text-center"><b>Назва:</b><i> ${attachmentCategory.name}</i></h3>
    <h3 class="text-center"><b>Публічно видима: </b><i>${attachmentCategory.isPubliclyViewable()?string("Так", "Ні")}</i></h3>

    <div class="d-flex flex-row justify-content-center">
        <a class="btn btn-primary m-1" href="/admin/attachment-categories/edit/${attachmentCategory.id}">Редагувати</a>
        <a class="btn btn-danger m-1" href="/admin/attachment-categories/delete/${attachmentCategory.id}">Видалити</a>
    </div>

</div>

<#include '../../include/footer.ftl'>