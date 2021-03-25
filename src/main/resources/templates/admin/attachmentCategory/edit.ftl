<#include '../include/header.ftl'>
<script>
    const publiclyViewableBefore = ${attachmentCategory.publiclyViewable?then('true', 'false')}
</script>
<div class="m-2">
    <a class="btn btn-outline-primary" href="/admin/attachment-categories/${attachmentCategory.id}">&#8592;</a>
</div>

<div>
    <form action="/admin/attachment-categories/update/${attachmentCategory.id}" method="post" id="form">
        <h2 class="text-center">Оновити категорію</h2>
        <div class="d-flex justify-content-center">
            <label class="form-label" for="name">Назва категорії&#x00A0;&#x00A0;</label>
            <input type="text" name="name" id="name" class="mb-2" value="${attachmentCategory.name}" required>
            <input type="hidden" id="oldName" class="mb-2" value="${attachmentCategory.name}">
        </div>

        <div class="d-flex justify-content-center">
            <label class="form-check-label" for="checkbox">Публічні дані?&#x00A0;&#x00A0;</label>
            <input type="checkbox" id="checkbox" class="form-check-input" name="PubliclyViewable"
                    <#if attachmentCategory.publiclyViewable> checked </#if> />
        </div>

        <div class="d-flex flex-row justify-content-center">
            <input type="submit" class="btn btn-primary m-1" value="Зберегти">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </div>

    </form>
</div>
<script>
    document.getElementById("form").addEventListener("submit", function (event) {
        event.preventDefault();

        const newName = document.getElementById("name").value;
        const oldName = document.getElementById("oldName").value;

        const publiclyViewableAfter = document.getElementById("checkbox").checked;

        if (oldName !== newName || publiclyViewableBefore !== publiclyViewableAfter) {
            document.getElementById("form").submit();
        } else if (!document.getElementById("popup")) {
            let msg = "<div class='alert alert-danger m-5' id='popup'> <p class=text-center>Немає змін</p> </div>";
            document.getElementById("form").insertAdjacentHTML("beforebegin", msg);

            setTimeout(() => document.getElementById("popup").remove(), 5000);
        }
    });
</script>
<#include '../../include/footer.ftl'>