<#-- @ftlvariable name="post_id" type="String"-->
<#import "./common.ftlh" as c>

<link href="css/post.css" rel="stylesheet"/>
<@c.page>
    <div class="data">
        <#if post_id?has_content && post_id?is_number>
            <#assign p = c.findBy(posts, "id", post_id)!>
        </#if>
        <#if p?has_content>
            <@c.displayPost p/>
        <#else>
            No such post
        </#if>
    </div>
</@c.page>