<#include '../include/header.ftl'>

<div class="d-flex justify-content-start">

    <div class="return-back-button">
        <a class="display-2 btn btn-outline-primary"
           href="/admin/categories<#if category??>/${category.id}</#if>">&#8592;</a>
    </div>

    <div class="category__container">

        <h1 class="user-select-none"> <#if category??> Оновити<#else > Створити нову</#if> категорію</h1>

        <form class="" action="/admin/categories<#if category??>/update/${category.id}<#else >/create</#if>"
              method="POST">
            <div class="form-group">

                <div class="d-flex justify-content-center">
                    <label class="mb-3 h3 user-select-none" for="category-name">Назва категорії</label>
                </div>

                <input type="text" class="form-control" name="name"
                       aria-describedby="nameMessage"
                       id="category-name" placeholder="Введіть назву категорії"
                       <#if category?? >value="${category.name}"</#if> autofocus>

                <#if message?? >
                    <div class="d-flex justify-content-center">
                        <small id="nameMessage" class="form-text text-warning bg-primary rounded px-4">
                            ${message}
                        </small>
                    </div>
                </#if>

            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

            <div class="d-flex justify-content-center">
                <button id="submit-button" class="mt-2 btn btn-primary "
                        type="submit"><#if category??>Оновити<#else >Створити</#if></button>
            </div>
        </form>

    </div>
</div>


<#if category??>
    <script>

        let input_field = document.getElementById("category-name");

        input_field.addEventListener("load", preventSameNameSubmit());
        input_field.addEventListener("change", preventSameNameSubmit);

        function preventSameNameSubmit() {

            let button = document.getElementById("submit-button");

            if (input_field.value == "${category.name}") {
                button.setAttribute("disabled", "disabled");

            } else {
                button.removeAttribute("disabled");
            }

        }

    </script>
</#if>

<#include '../../include/footer.ftl'>
