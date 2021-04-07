<#import "./common.ftlh" as c>

<link href="css/index.css" rel="stylesheet"/>
<@c.page>
    <ul>
        <#list posts?reverse as p>
            <li>
                <@c.displayPost p true />
            </li>
        </#list>
    </ul>
</@c.page>
