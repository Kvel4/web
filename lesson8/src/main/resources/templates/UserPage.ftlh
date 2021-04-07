<#-- @ftlvariable name="user" type="ru.itmo.wp.domain.User" -->
<#-- @ftlvariable name="viewedUser" type="ru.itmo.wp.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="user-info">
        <#if viewedUser?has_content>
            <ul>
                <li>${viewedUser.id}</li>
                <li>${viewedUser.login}</li>
                <li>${viewedUser.creationTime}</li>
            </ul>
        <#else>
            No such user
        </#if>
    </div>
</@c.page>
