<div>
    <div class="row">
        <ul class="list-group">
            <#list partialUserActionsByIpAddress.getContent() as userAction>
                <li class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">${userAction.url}</h5>
                        <small>
                            <i class="fa fa-clock-o fa-lg p-2"></i>
                            <b>${userAction.time.format(formatter)}</b>
                        </small>
                    </div>
                    <p class="mb-1">REFERER_URL: ${userAction.referrerUrl}</p>
                    <small>METHOD: ${userAction.httpMethod}</small>
                </li>
            </#list>
        </ul>
    </div>
    <div class="justify-content-center mt-2 row justify-content-md-center">
        <ul class="pagination user-action-list">
            <li class="page-item  <#if !partialUserActionsByIpAddress.hasPrevious()> disabled</#if>">
                <button class="page-link" data-page="${partialUserActionsByIpAddress.getNumber() - 2}">&laquo;</button>
            </li>
            <#list 1..partialUserActionsByIpAddress.getTotalPages() as pageNumber>
                <li class="page-item <#if partialUserActionsByIpAddress.getNumber() + 1 == pageNumber>active</#if>">
                    <button class="page-link" data-page="${pageNumber}">${pageNumber}</button>
                </li>
            </#list>
            <li class="page-item <#if !partialUserActionsByIpAddress.hasNext()> disabled</#if>">
                <button class="page-link" data-page="${partialUserActionsByIpAddress.getNumber() + 2}">&raquo;</button>
            </li>
        </ul>
    </div>
</div>

<script>
    $('.user-action-list button').click(function (event) {
        const page = $(event.target).attr('data-page').trim();
        const ipAddress = $('li.list-group-item.active').attr("data-id").trim();

        get_all_user_action_by_ip_address(ipAddress, page)
    })
</script>
