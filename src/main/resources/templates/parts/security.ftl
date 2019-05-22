<#assign known = Session.SPRING_SECURITY_CONTEXT??>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    username = user.getUsername()
    isAdmin = user.isAdmin()
    isModerator = user.isModerator()
    currentUserId = user.getId()
    >
<#else>
    <#assign
    username = "unknown"
    isAdmin = false
    currentUserId=-1
    >
</#if>

