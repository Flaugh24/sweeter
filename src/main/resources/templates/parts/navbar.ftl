<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Sweeter</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <#if user??>
            <li class="nav-item">
                <a class="nav-link" href="/messages">Messages</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/messages/user/${currentUserId}">My messages</a>
            </li>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/users">Users list</a>

                </li>
            </#if>
                <li class="nav-item">
                    <a class="nav-link" href="/users/profile">Profile</a>

                </li>
            </#if>
        </ul>
        <div class="navbar-text mr-3"><#if user??>${username}<#else>Please, login</#if></div>
            <div><@l.logout/></div>
    </div>
</nav>