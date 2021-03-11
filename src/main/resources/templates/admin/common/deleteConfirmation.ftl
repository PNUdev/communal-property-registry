<#include "../include/header.ftl">
<div class="w-100">
    <div class="col-md-5 mx-auto mt-5 py-3 px-5 rounded bg-light border">
        <div class="h5 text-center">${message}</div>
        <div class="d-flex mt-5 d-flex justify-content-around">
            <a href="${returnBackUrl}" class="mt-2">
                <div class="btn btn-outline-primary">Ні, відмінити дію</div>
            </a>
            <form method="POST" class="mt-2">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="btn btn-outline-danger">Так, видалити!</button>
            </form>
        </div>
    </div>
</div>
<#include "../../include/footer.ftl">