<#include '../include/header.ftl'>

<div class="return-back-button">
    <a class="display-2 btn btn-outline-primary"
       href="/admin/categories<#if category??>/${category.id}</#if>">&#8592;</a>
</div>

<div class="category__container">

    <h1 class="user-select-none"> <#if category??> Оновити<#else > Створити нову</#if> категорію</h1>

    <form action="/admin/categories<#if category??>/update/${category.id}<#else >/create</#if>" method="POST">

        <div class="form-group">

            <label class="mb-3 h3 user-select-none" for="category-name">Назва категорії</label>
            <input type="text" class="form-control" name="name"
                   id="category-name" placeholder="Введіть назву категорії"
                   <#if category?? >value="${category.name}"</#if> autofocus>

        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <button class="mt-2 btn btn-primary" type="submit"><#if category??>Оновити<#else >Створити</#if></button>
    </form>

</div>


<#include '../../include/footer.ftl'>
