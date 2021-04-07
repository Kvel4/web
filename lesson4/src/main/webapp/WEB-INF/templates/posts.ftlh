<#-- @ftlvariable name="user_id" type="Long" -->
<#import "./common.ftlh" as c>

<link href="css/index.css" rel="stylesheet"/>
<@c.page>
    <#assign havePosts = false>
    <#if user_id?is_number && c.findBy(users, "id", user_id)!?has_content>
        <ul>
            <#list posts?reverse as p>
                <#if p.user_id == user_id>
                    <#assign havePosts = true>
                    <li>
                        <@c.displayPost p true />
                    </li>
                </#if>
            </#list>
        </ul>
        <#if havePosts == false>
            User has no posts
        </#if>
    <#else>
        No such user
    </#if>
</@c.page>