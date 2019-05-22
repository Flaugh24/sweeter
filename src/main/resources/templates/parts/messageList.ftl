<#include "security.ftl">
<#import "pager.ftl" as p>

<@p.pager url page />
<div class="card-columns">
    <#list page.content as message>
        <div class="card my-3">
        <#if message.fileName??>
            <img src="/img/${message.fileName}" class="card-img-top" alt="picture">
        </#if>
        <div class="m-2">
        <span>${message.text}</span><br/>
        <i>#${message.tag}</i>
        </div>
            <div class="card-footer text-muted container">
                <div class="row">
                <a class="col align-self-center" href="/messages/user/${message.author.id}">${message.author.username}</a>
                <a class="col align-self-center" href="/messages/${message.id}/like">
                    <#if message.meLiked>
                        <i class="fas fa-heart"></i>
                        <#else >
                        <i class="far fa-heart"></i>
                    </#if>
                    ${message.likes}
                </a>
                    <#if message.author.id == currentUserId>
                    <a class="col btn btn-primary" href="/messages/user/${message.author.id}?message=${message.id}">Edit</a>
                    </#if>
                    <#if isAdmin || isModerator>
                        <a class="col align-self-center" href="/messages/${message.id}/delete">X</a>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        No message
    </#list>
</div>
<@p.pager url page />