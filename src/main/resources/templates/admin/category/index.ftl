<#include '../include/header.ftl'>


<div class="categories-container">

        <div class="category__new">
            <a class="m-2 btn btn-secondary" href="/admin/categories/new">New category</a>
        </div>

        <table class="table table-stripped">
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Name</th>
                    <th scope="col">&nbsp;</th>
                </tr>
            </thead>
            <tbody>

            <#list pagination.content as category>
                    <tr>
                        <th scope="row">${category.id}</th>
                        <td>${category.name}</td>
                        <td>
                            <a class=" btn btn-sm btn-primary" href="/admin/categories/${category.id}">Show</a>
                        </td>
                    </tr>
            </#list>

            </tbody>
        </table>

        <div class="m-5">

            <ul class="pagination">
                <#if !pagination.isFirstPage >
                    <li class="page-item"><a class="page-link" href="/admin/categories?page=0">First</a></li>
                </#if>

                <#list pagination.firstVisiblePage..pagination.lastVisiblePage as page>
                    <li class="page-item <#if page == pagination.page>active</#if>">
                        <a class="page-link" href="/admin/categories?page=${page}">${page + 1}</a>
                    </li>
                </#list>

                <#if !pagination.isLastPage >
                    <li class="page-item"><a class="page-link"
                                             href="/admin/categories?page=${pagination.totalPages - 1}">Last</a></li>
                </#if>

            </ul>

        </div>

    </div>

<#include '../../include/footer.ftl'>

