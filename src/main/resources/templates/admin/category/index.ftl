<#include '../include/header.ftl'>


<div class="categories">

    <div class="categories__new">
        <a class="m-2 btn btn-secondary" href="/admin/categories/new">Створити категорію</a>
    </div>

    <div class="vertical-scroll">
        <table class="categories__list table table-stripped">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Назва</th>
                <th scope="col">&nbsp;</th>
            </tr>
            </thead>
            <tbody>

            <#list categoriesByPurpose as category>
                <tr>
                    <th scope="row">${category.id}</th>
                    <td class="">${category.name}</td>
                    <td>
                        <a class=" btn btn-sm btn-primary" href="/admin/categories/${category.id}">Детальніше</a>
                    </td>
                </tr>
            </#list>

            </tbody>
        </table>
    </div>

    <#if categoriesByPurpose?size == 0>
        <div class="alert alert-primary w-75 text-center mx-auto mt-5">
            Список категорій порожній
        </div>
    </#if>

</div>

<#include '../../include/footer.ftl'>

