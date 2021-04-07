<#-- @ftlvariable name="pageDir" type="java.lang.String" -->

<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-vs-coronavirus-65.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home", "subDirectories": "index"},
            {"href": "/users", "name": "Users", "subDirectories": "user"},
            {"href": "/misc/help", "name": "Help", "subDirectories": "misc"},
            {"href": "/contests", "name": "Contests", "subDirectories": "contest"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if pageDir?contains(item.subDirectories)>
                            <a class="currentPage" href="${item.href}">${item.name}</a>
                        <#else>
                            <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>
            <section>
                <div class="header">Post #${p.id}</div>
                <div class="body"><@displayPostText p true/></div>
                <div class="footer">
                    <a href="/post?post_id=${p.id}">View all</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2020 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly=false>
    <#assign Color=enums['ru.itmo.web.lesson4.model.Color']>
    <#if !nameOnly>
        <#switch user.color>
            <#case Color.RED>
                <a class="ratedUser redUser" href="/user?handle=${user.handle}">${user.name}</a>
                <#break>
            <#case Color.GREEN>
                <a class="ratedUser greenUser" href="/user?handle=${user.handle}">${user.name}</a>
                <#break>
            <#case Color.BLUE>
                <a class="ratedUser blueUser" href="/user?handle=${user.handle}">${user.name}</a>
                <#break>
        </#switch>
    <#else>
        <a href="/user?handle=${user.handle}">${user.name}</a>
    </#if>
</#macro>

<#macro displayPost post hideContent=false>
    <article>
        <div class="title">${post.title}</div>
        <div class="information">By <@userlink findBy(users, "id", post.user_id)/></div>
        <div class="body">
            <@displayPostText post hideContent/>
        </div>
        <ul class="attachment">
            <li>Announcement of <a href="#">Some event</a></li>
            <li>Announcement of <a href="#">Some event</a></li>
        </ul>
        <div class="footer">
            <div class="left">
                <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+173</span>
                <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                2 days ago
                <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
    </article>
</#macro>

<#macro displayPostText post hideContent=false>
    <#if hideContent && post.text?length gt 250>
        ${post.text?substring(0, 251)}
        ...
    <#else>
        ${post.text}
    </#if>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function indexOf items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item_index/>
        </#if>
    </#list>
</#function>

