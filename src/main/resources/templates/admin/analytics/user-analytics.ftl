<#include "../include/header.ftl">
<#assign currentPage = ipAddressAndCountDtoList.getNumber() + 1>
<h2 class="text-center mt-3">Аналітика відвідувачів</h2>
<div class="col-md-10 mt-1 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <#if ipAddressAndCountDtoList.getTotalPages() == 0>
        <h3 class="text-center mt-3">Список аналітики пустий</h3>
    <#else>
        <div class="row justify-content-end">
            <div class="col-2 mb-2">
                <form action="/admin/user-actions/download/txt">
                    <button class="btn btn-primary">Завантажити аналітику</button>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="col-4">
                <div class="list-group action-pairs">
                    <#list ipAddressAndCountDtoList.getContent() as pair>
                        <li class="list-group-item list-group-item-action
                            ${(pair?counter == 1)?then('active', '')}" data-bs-toggle="list" data-id="${pair.ipAddress}">
                            <div class="d-flex justify-content-between row">
                                <span class="col-11 text-break"><i class="fa fa-laptop fa-lg p-1" aria-hidden="true"></i>${pair.ipAddress}</span>
                                <span class="badge bg-primary rounded-pill col-1">${pair.count}</span>
                            </div>
                        </li>
                    </#list>
                </div>
                <#if ipAddressAndCountDtoList.getTotalPages() != 0>
                    <div class="mt-2">
                        <ul class="pagination justify-content-center">
                            <li class="page-item  <#if !ipAddressAndCountDtoList.hasPrevious()> disabled</#if>">
                                <a class="page-link" href="?page=${currentPage - 1}">&laquo;</a>
                            </li>
                            <li class="page-item active">
                                <span class="page-link">${currentPage}</span>
                            </li>
                            <li class="page-item <#if !ipAddressAndCountDtoList.hasNext()> disabled</#if>">
                                <a class="page-link" href="?page=${currentPage + 1}">&raquo;</a>
                            </li>
                        </ul>
                    </div>
                </#if>
            </div>
            <div class="col-8" id="partialUserActionsByIpAddress"></div>
        </div>
    </#if>
</div>
<#include "../../include/footer.ftl">


<script>
    document.addEventListener('DOMContentLoaded', function () {
        const listItems = document.querySelectorAll('.action-pairs li');
        for (let i = 0; i < listItems.length; i++) {
            const item = listItems[i];
            item.addEventListener("click", function (e) {
                handleSelectIpAddress(e.currentTarget);
            });
        }
        const activeElement = document.querySelector('.action-pairs li.active');
        handleSelectIpAddress(activeElement);
    });

    function handleSelectIpAddress(node) {
        const ipAddress = node.getAttribute('data-id').trim();
        getAllUserActionsByIpAddress(ipAddress, 1);
    }

    function handlePageChange(e) {
        const ipAddress = document
            .querySelector('li.list-group-item.active')
            .getAttribute('data-id').trim();
        const page = e.currentTarget.getAttribute('data-page').trim();
        getAllUserActionsByIpAddress(ipAddress, page);
        //🧐
    }

    function getAllUserActionsByIpAddress(ipAddress, page) {
        const url = '/admin/user-actions/partial?ipAddress=' + ipAddress + "&page=" + page;
        fetch(url, {
            method: 'GET',
        }).then(function (response) {
            return response.text();
        }).then(function (responseText) {
            document.getElementById('partialUserActionsByIpAddress').innerHTML = responseText;
        })
    }
</script>
