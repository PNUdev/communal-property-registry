<#include '../include/header.ftl'>


<div class="categories-container">

        <div class="category__new">
            <a class="m-2 btn btn-secondary" href="/admin/categories/new">Створити категорію</a>
        </div>

        <table class="table table-stripped">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Назва</th>
                    <th scope="col">&nbsp;</th>
                </tr>
            </thead>
            <tbody>

            <#list categoriesPage.content as category>
                    <tr>
                        <th scope="row">${category.id}</th>
                        <td>${category.name}</td>
                        <td>
                            <a class=" btn btn-sm btn-primary" href="/admin/categories/${category.id}">Детальніше</a>
                        </td>
                    </tr>
            </#list>

            </tbody>
        </table>

        <div class="m-5">

            <ul class="pagination mx-auto">

                <#if !categoriesPage.isFirstPage >
                    <form action="/admin/categories" method="GET">
                        <li class="page-item">
                            <button type="submit"
                                    class="page-link">
                                Перша
                            </button>
                        </li>
                        <input type="hidden" name="page" value="0">
                    </form>
                </#if>

                <#list 1..categoriesPage.totalPages as pageNumber>
                    <form action="/admin/categories" method="GET">
                        <li class="page-item <#if pageNumber - 1 == categoriesPage.page>active</#if>">
                            <button type="submit"
                                    class="page-link">
                                ${pageNumber}
                            </button>
                        </li>
                        <input type="hidden" name="page" value="${pageNumber - 1}">
                    </form>
                </#list>

                <#if !categoriesPage.isLastPage >
                    <form action="/admin/categories" method="GET">
                        <li class="page-item">
                            <button type="submit"
                                    class="page-link">
                                Остання
                            </button>
                        </li>
                        <input type="hidden" name="page" value="${categoriesPage.totalPages - 1}">
                    </form>
                </#if>

            </ul>

        </div>

    </div>

<#include '../../include/footer.ftl'>

