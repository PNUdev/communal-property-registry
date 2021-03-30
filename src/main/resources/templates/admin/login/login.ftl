<html>
<head>
    <#include "../../include/coreDependencies.ftl" >
    <title>Login</title>
</head>
<body>

<div class="w-100 h-100 d-flex align-items-center justify-content-center">
    <div class="col-md-8">
        <form method="POST" action="/login" class="form-signin">
            <h2 class="form-heading text-center p-3">Вхід</h2>
            <div class="form-group">
                <#if userLogoutMessage??>
                    <div class="form-control m-3 alert alert-success text-center" role="alert">${userLogoutMessage}</div>
                </#if>
                <#if incorrectDataMessage??>
                    <div class="form-control m-3 alert alert-danger text-center" role="alert">${incorrectDataMessage}</div>
                </#if>
                <input name="username" type="text" class="form-control m-3" placeholder="Логін"
                       autofocus="true"/>
                <input name="password" type="password" class="form-control m-3" placeholder="Пароль"/>
                <div class="form-row justify-content-center d-flex">
                    <button class="btn btn-primary p-3 w-50" type="submit">Увійти</button>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</div>

</body>
</html>