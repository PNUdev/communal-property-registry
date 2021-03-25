<#include '../include/header.ftl'>

<div>
    <a class="m-2 btn btn-secondary" href="/admin/attachment-categories/new">Створити категорію прикріплення</a>
</div>

<#if ERROR_FLASH_MESSAGE?? || SUCCESS_FLASH_MESSAGE??>
    <div class="alert mt-4 <#if SUCCESS_FLASH_MESSAGE??>alert-success<#else>alert-danger</#if>" id="popup" role="alert">
        ${(SUCCESS_FLASH_MESSAGE)!}${(ERROR_FLASH_MESSAGE)!}
    </div>
</#if>

<#if attachmentCategoriesPage.getNumberOfElements() gt 0 >
    <table class="table table-stripped">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Назва</th>
            <th scope="col">Публічно доступна</th>
            <th scope="col">&nbsp;</th>
        </tr>
        </thead>

        <tbody>

        <#list attachmentCategoriesPage.content as category>
            <tr>
                <th scope="row">${category.id}</th>
                <td>${category.name}</td>
                <td>${category.isPubliclyViewable()?string("Так", "Ні")}</td>
                <td>
                    <a class="btn btn-sm btn-primary" href="/admin/attachment-categories/${category.id}">Детальніше</a>
                </td>
            </tr>
        </#list>

        </tbody>
    </table>

    <div>
        <ul class="justify-content-center pagination mx-auto ">
            <#if !attachmentCategoriesPage.first >
                <form action="/admin/attachment-categories" method="GET">
                    <li class="page-item">
                        <button type="submit" class="page-link"> Перша</button>
                    </li>
                    <input type="hidden" name="page" value="0">
                </form>
            </#if>

            <#list 1..attachmentCategoriesPage.totalPages as pageNumber>
                <form action="/admin/attachment-categories" method="GET">
                    <li class="page-item <#if pageNumber - 1 == attachmentCategoriesPage.number>active</#if>">
                        <button type="submit" class="page-link"> ${pageNumber} </button>
                    </li>
                    <input type="hidden" name="page" value="${pageNumber}">
                </form>
            </#list>

            <#if !attachmentCategoriesPage.last >
                <form action="/admin/attachment-categories" method="GET">
                    <li class="page-item">
                        <button type="submit" class="page-link"> Остання</button>
                    </li>
                    <input type="hidden" name="page" value="${attachmentCategoriesPage.totalPages}">
                </form>
            </#if>

        </ul>
    </div>
<#else>
    <h2 class="text-center">Немає жодної категорії</h2>

</#if>
<script>
    setTimeout(() => document.getElementById("popup").hidden = true, 5000);
</script>
<#include '../../include/footer.ftl'>


