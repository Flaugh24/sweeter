<#import "parts/common.ftl" as c>

<@c.page>
    <h3>${username}</h3>

    <form method="post">
    <div class="form-group row">
        <label for="email" class="col-sm-2 col-form-label">Email:</label>
        <div class="col-sm-6">
            <input type="email" name="email" class="form-control ${(emailError??)?string('is-invalid', '')}" id="email" value="${email!''}"/>
            <#if emailError??>
                <div class="invalid-feedback">
                ${emailError}
                </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">New password:</label>
        <div class="col-sm-6">
            <input type="password" name="passwordNew" class="form-control ${(passwordNewError??)?string('is-invalid', '')}" placeholder="New password"/>
            <#if passwordNewError??>
                <div class="invalid-feedback">
                ${passwordNewError}
                </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Retype password:</label>
        <div class="col-sm-6">
            <input type="password" name="passwordConfirm" class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}" placeholder="Retype password"/>
            <#if passwordConfirmError??>
                <div class="invalid-feedback">
                ${passwordConfirmError}
                </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Password:</label>
        <div class="col-sm-6">
            <input type="password" name="password" class="form-control ${(passwordError??)?string('is-invalid', '')}" placeholder="Password"/>
            <#if passwordError??>
                <div class="invalid-feedback">
                ${passwordError}
                </div>
            </#if>
        </div>
    </div>

    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@c.page>