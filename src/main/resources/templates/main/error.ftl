<!doctype html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "../include/coreDependencies.ftl" >
    <title>Виникла помилка!</title>
</head>
<body>
<div class="w-100">
    <div class="col-lg-8 mx-auto mt-5 py-3 px-5 rounded bg-light border">
        <h2 class="text-center">${errorMessage}</h2>
        <div class="col-md-12 mt-5 d-flex justify-content-center py-2 px-4 rounded bg-white">
            <#if previousLocation?? >
                <a href="${previousLocation}" class="h4">Повернутися назад</a>
            <#else >
                <a href="/" class="h4">Головна сторінка</a>
            </#if>
        </div>
    </div>
</div>
</body>
</html>