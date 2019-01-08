<#import "parts/common.ftl" as c>

<@c.page>
    <div class="form-row">
    <div class="form-group col-md-6">
    <form method="get" action="/messages" class="form-inline">
    <input type="text" name="filter" value="${filter!}" class="form-control" placeholder="Search by tag"/>
    <button type="submit" class="btn btn-primary ml-2">Search</button>
    </form>
    </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#newMessageForm" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        Add new message
    </a>

    <div class="collapse <#if message??>show</#if>" id="newMessageForm">
    <div class="form-group mt-3">
    <form method="post" action="/messages" enctype="multipart/form-data">
    <div class="form-group">
<input type="text" class="form-control ${(textError??)?string('is-invalid', '')}" name="text"
       placeholder="Message" value="<#if message??>${message.text}</#if>"/>
    <#if textError??>
        <div class="invalid-feedback">
        ${textError}
        </div>
    </#if>
    </div>
    <div class="form-group">
<input type="text" class="form-control ${(tagError??)?string('is-invalid', '')}" name="tag" placeholder="Tag">
    <#if tagError??>
        <div class="invalid-feedback">
        ${tagError}
        </div>
    </#if>
    </div>
<div class="form-group">
    <div class="custom-file">
        <input type="file" name="file" id="file">
        <label class="custom-file-label" for="file">Choose file</label>
    </div>
</div>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <div class="form-group">
        <button type="submit" class="btn btn-primary ml-2">Add</button>
    </div>
    </form>
    </div>
    </div>

    <div class="card-columns">
    <#list messages as message>
        <div class="card my-3">
        <#if message.fileName??>
            <img src="/img/${message.fileName}" class="card-img-top" alt="picture">
        </#if>
        <div class="m-2">
        <span>${message.text}</span>
        <i>${message.tag}</i>
        </div>
        <div class="card-footer text-muted">
        ${message.author.username}
        </div>
        </div>
    <#else>
        No message
    </#list>
    </div>
</@c.page>