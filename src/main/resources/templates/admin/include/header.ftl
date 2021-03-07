<!doctype html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "../../include/coreDependencies.ftl" >
    <title>Admin panel</title>
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>
<nav class="navbar navbar-dark bg-primary">
    <div class="container-fluid">
        <div>
            <a class="navbar-brand" href="#">#</a>
        </div>
        <div>
            <form method="POST" action="/logout" class="m-0 p-0">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-light">Вийти</button>
            </form>
        </div>
    </div>
</nav>
