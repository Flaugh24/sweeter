<#macro login path isRegisterForm>
    <form action="${path}" method="post">
    <div class="form-group row">
<label class="col-sm-2 col-form-label">User Name :</label>
    <div class="col-sm-6">
<input type="text" name="username" class="form-control ${(usernameError??)?string('is-invalid', '')}"
       placeholder="Username" value="<#if user??>${user.username}</#if>"/>
    <#if usernameError??>
        <div class="invalid-feedback">
        ${usernameError}
        </div>
    </#if>
    </div>
    </div>
    <div class="form-group row">
<label class="col-sm-2 col-form-label">Password:</label>
    <div class="col-sm-6">
<input type="password" name="password" class="form-control ${(passwordError??)?string('is-invalid', '')}"
       placeholder="Password"/>
    <#if passwordError??>
        <div class="invalid-feedback">
        ${passwordError}
        </div>
    </#if>
    </div>
    </div>
    <#if isRegisterForm>
        <div class="form-group row">
        <label class="col-sm-2 col-form-label">Retype password:</label>
        <div class="col-sm-6">
        <input type="password" name="passwordConfirm" class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}"
    placeholder="Retype password"/>
        <#if passwordConfirmError??>
            <div class="invalid-feedback">
            ${passwordConfirmError}
            </div>
        </#if>
        </div>
        </div>

        <div class="form-group row">
        <label class="col-sm-2 col-form-label">Email:</label>
        <div class="col-sm-6">
        <input type="email" name="email" class="form-control ${(emailError??)?string('is-invalid', '')}"
    placeholder="e@mail.com" value="<#if user??>${user.email}</#if>"/>
        <#if emailError??>
            <div class="invalid-feedback">
            ${emailError}
            </div>
        </#if>
        </div>
        </div>
        <div class="col-sm-3">
        <div class="g-recaptcha" data-sitekey="6Ld15IcUAAAAADIdR9FRpJU50V_bu-J3TWUAzWk9"></div>
        <#if captchaError??>
            <div class="alert alert-danger" role="alert">
            ${captchaError}
            </div>
        </#if>
        </div>
    </#if>
    <#if !isRegisterForm><a href="/registration">Sign up</a></#if>
<input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Sign up<#else>Sign In</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit"><#if Session.SPRING_SECURITY_CONTEXT??>Sign Out<#else>Log in</#if></button>
    </form>
</#macro>