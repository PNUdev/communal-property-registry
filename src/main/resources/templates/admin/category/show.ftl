<#include '../include/header.ftl'>

    <div class="category__container d-flex flex-column">

        <div class="mb-3">
            <a class="btn btn-outline-primary" href="/admin/categories">Back</a>
        </div>

        <div class="category__name">
            <h2><b>Name: </b><i>${category.name}</i></h2>
        </div>

        <div class="d-flex flex-row">

            <div class="category__function">
                <a class="btn btn-info" href="/admin/categories/edit/${category.id}">Edit</a>
            </div>

            <div class="category__function">
                <a class="btn btn-danger" href="/admin/categories/delete/${category.id}">Delete</a>
            </div>

        </div>

    </div>

<#include '../../include/footer.ftl'>