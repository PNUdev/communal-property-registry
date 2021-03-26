<#include "../include/header.ftl">
<#assign currentPage = ipAddressAndCountDtoList.getNumber() + 1>
<h2 class="text-center mt-3">Аналітика відвідувачів</h2>
<div class="col-md-10 mt-1 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <#if ipAddressAndCountDtoList.getTotalPages() == 0>
        <h3 class="text-center mt-3">Список аналітики пустий</h3>
    <#else>
        <div class="row justify-content-end">
            <div class="col-2 mb-2">
                <a class="btn btn-primary" href="/admin/user-actions/download/txt">Завантажити статистику</a>
            </div>
        </div>
        <div class="row">
            <div class="col-4">
                <div class="list-group">
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
    $(window).load(function () {
        const ipAddress = $('li.list-group-item.active').attr('data-id').trim();
        get_all_user_action_by_ip_address(ipAddress, 1)
    });

    $('.list-group').on('click', '.list-group-item', function (event) {
        event.preventDefault();
        const ipAddress = $(this).attr('data-id').trim();
        get_all_user_action_by_ip_address(ipAddress, 1)
    });

    function get_all_user_action_by_ip_address(ipp_address, page) {
        $.get(
            '/admin/user-actions/partial?ipAddress=' + ipp_address + "&page=" + page,
            function (response) {
                $('#partialUserActionsByIpAddress').html(response);
            })
    }
</script>
