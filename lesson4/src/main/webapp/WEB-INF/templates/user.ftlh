<#-- @ftlvariable name="u" type="ru.itmo.web.lesson4.model.User" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<link href="css/user.css" rel="stylesheet"/>
<@c.page>
    <div class="data">
        <#if handle??>
            <#assign i = c.indexOf(users, "handle", handle)!/>
        <#elseif user_id?? && user_id?is_number>
            <#assign i = c.indexOf(users, "id", user_id)!/>
        </#if>
        <#if i?has_content>
            <#assign u = users[i]/>
            <div class="user">
                <div class="name">
                    <@c.userlink u/>
                </div>

                <div class="description">
                    <ul>
                        <li>id: ${u.id}</li>
                        <li>handle: ${u.handle}</li>
                        <li>name: ${u.name}</li>
                    </ul>
                    <a href="/posts?user_id=${u.id}">User posts</a>
                    <a href="#" class="profilePicture">
                        <img alt="profilePicture" title="profilePicture" src="/img/profilePicture.jpg">
                    </a>
                </div>
            </div>
            <div class="neighbourUsers">
                <#if i gt 0>
                    <a href="user?handle=${users[i - 1].handle}" class="prevUser">
                        ←
                    </a>
                <#else>
                    ←
                </#if>
                <#if i lt users?size - 1>
                    <a href="user?handle=${users[i + 1].handle}" class="nextUser">
                        →
                    </a>
                <#else>
                    →
                </#if>
            </div>
        <#else>
            No such user
        </#if>
    </div>
</@c.page>