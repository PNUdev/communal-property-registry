<#include '../include/header.ftl'>

    <div class="category__container">

        <div class="mb-3">
            <a class="btn btn-outline-primary" href="${returnBackUrl}">Повернутися назад</a>
        </div>

        <h1 class="user-select-none" >${actionType} категорію</h1>

        <div class="category__container">

            <form action="${postUrl}" method="POST">

                <div class="form-group">

                    <label class="mb-3 h3 user-select-none" for="category-name">Назва категорії</label>
                    <input type="text" class="form-control" name="name"
                           id="category-name" placeholder="Введіть назву категорії"
                           <#if category?? >value="${category.name}"</#if> autofocus>

                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <button class="mt-2 btn btn-primary" type="submit">${actionType}</button>
            </form>

        </div>

    </div>


<#include '../../include/footer.ftl'>