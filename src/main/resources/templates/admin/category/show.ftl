<#include '../include/header.ftl'>

<div class="d-flex justify-content-start">

    <div class="return-back-button">
        <a class="btn btn-outline-primary" href="/admin/categories">&#8592;</a>
    </div>

    <div class="category__container">

        <div class="category__name">
            <h2 class="user-select-none"><p><b>Назва: </b><i>${category.name}</i></p></h2>
        </div>

        <div class="d-flex flex-row">

            <div class="category__function">
                <a class="btn btn-info" href="/admin/categories/edit/${category.id}">Редагувати</a>
            </div>

            <div class="category__function">
                <a class="btn btn-danger" href="/admin/categories/delete/${category.id}">Видалити</a>
            </div>

        </div>

    </div>

</div>
<#include '../../include/footer.ftl'>