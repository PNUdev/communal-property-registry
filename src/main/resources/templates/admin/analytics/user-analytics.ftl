<#include "../include/header.ftl">
<h2 class="text-center mt-3">Аналітика відвідувачів</h2>
<div class="col-md-10 mt-5 px-5 pb-2 pt-4 rounded bg-light mx-auto">
    <#if ipAddressAndCountDtoList.getTotalPages() == 0>
        <h3 class="text-center mt-3">Список аналітики пустий</h3>
    <#else>
        <div class="row">
            <div class="col-4">
                <div class="list-group" id="list-tab" role="tablist">
                    <#list ipAddressAndCountDtoList.getContent() as pair>
                        <li class="list-group-item list-group-item-action d-flex justify-content-between ${(pair?counter == 1)?then('active', '')}"
                            data-bs-toggle="list" data-id="${pair.ipAddress}">
                            <span><i class="fa fa-laptop fa-lg p-1" aria-hidden="true"></i>${pair.ipAddress}</span>
                            <span class="badge bg-primary rounded-pill">${pair.count}</span>
                        </li>
                    </#list>
                </div>
                <#if ipAddressAndCountDtoList.getTotalPages() != 0>
                    <div class="justify-content-center mt-2">
                        <ul class="pagination">
                            <li class="page-item  <#if !ipAddressAndCountDtoList.hasPrevious()> disabled</#if>">
                                <a class="page-link" href="?page=${ipAddressAndCountDtoList.getNumber() - 2}"
                                   tabindex="-1">&laquo;</a>
                            </li>
                            <#list 1..ipAddressAndCountDtoList.getTotalPages() as pageNumber>
                                <li class="page-item <#if ipAddressAndCountDtoList.getNumber() + 1 == pageNumber>active</#if>">
                                    <a class="page-link" href="?page=${pageNumber}">${pageNumber}</a>
                                </li>
                            </#list>
                            <li class="page-item <#if !ipAddressAndCountDtoList.hasNext()> disabled</#if>">
                                <a class="page-link" href="?page=${ipAddressAndCountDtoList.getNumber() + 2}"
                                   tabindex="-1">&raquo;</a>
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
        get_all_user_action_by_ip_address(ipAddress, 0)
    });

    $('.list-group').on('click', '.list-group-item', function (event) {
        event.preventDefault();
        const ipAddress = $(this).attr('data-id').trim();
        get_all_user_action_by_ip_address(ipAddress, 0)
    });

    function get_all_user_action_by_ip_address(ipp_address, page) {
        $.get(
            '/admin/user-actions/partial?ipAddress=' + ipp_address + "&page=" + page,
            function (response) {
                $('#partialUserActionsByIpAddress').html(response);
            })
    }
</script>
