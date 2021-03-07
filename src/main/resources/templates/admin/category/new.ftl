<#include '../include/header.ftl'>

<div class="category__container">
    
    <div class="mb-3">
        <a class="btn btn-outline-primary" href="/admin/categories">Back</a>
    </div>

    <h1>Category Create Page</h1>

    <div class="category__container">

        <form action="/admin/categories/create" method="POST">

            <div class="form-group">

                <label for="category-name">Category name</label>
                <input type="text" class="form-control" name="name"
                       id="category-name" placeholder="Enter category name" autofocus>

            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

            <button class="mt-2 btn btn-primary" type="submit">Create</button>
        </form>

    </div>

</div>


<#include '../../include/footer.ftl'>

