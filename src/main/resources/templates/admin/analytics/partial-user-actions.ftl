<#assign currentPage = partialUserActionsByIpAddress.getNumber() + 1>
<div>
    <div class="row">
        <ul class="list-group">
            <#list partialUserActionsByIpAddress.getContent() as userAction>
                <li class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="row">
                        <p class="mb-1 col-8 text-break text-decoration-underline fw-bold">${userAction.url}</p>
                        <small class="col-4 d-flex justify-content-center">
                            <i class="fa fa-clock-o fa-lg p-2"></i>
                            <b>${userAction.time.format(formatter)}</b>
                        </small>
                    </div>
                    <p class="mb-1">ЗІ СТОРІНКИ: ${userAction.referrerUrl}</p>
                    <small>ТИП ЗАПИТУ: <span class="fw-bold">${userAction.httpMethod}</span></small>
                </li>
            </#list>
        </ul>
    </div>
    <div class="mt-2" id="pagination">
        <ul class="pagination user-action-list pagination justify-content-center">
            <li class="page-item  <#if !partialUserActionsByIpAddress.hasPrevious()> disabled</#if>">
                <button onclick="handlePageChange(event)" class="page-link" data-page="${currentPage - 1}">&laquo;</button>
            </li>
            <li class="page-item active">
                <span class="page-link">${currentPage}</span>
            </li>
            <li class="page-item <#if !partialUserActionsByIpAddress.hasNext()> disabled</#if>">
                <button onclick="handlePageChange(event)" class="page-link" data-page="${currentPage + 1}">&raquo;</button>
            </li>
        </ul>
    </div>
</div>
