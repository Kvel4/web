<#import "./common.ftlh" as c>

<link href="/css/users.css" rel="stylesheet"/>
<@c.page>
    <div class="datatable">
        <div class="caption">Users</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Handle</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <td>${u.id}</td>
                    <td>${u.handle}</td>
                    <td><@c.userlink u/></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>