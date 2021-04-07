<#-- @ftlvariable name="talkViews" type="ru.itmo.wp.model.service.TalkService.TalkView[]" -->
<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-talk" xmlns:c="http://www.w3.org/1999/XSL/Transform">
        <form method="post" action="">
            <input type="hidden" name="action" value="sendMessage"/>
            <div class="user-choice">
                <label for="users">Choose a recipient:</label>
                <select id="users" name="chosenUser">
                    <#list users as user>
                        <option value=${user.id} <#if chosenUser?? && chosenUser == user.id?c>selected</#if>>${user.login} </option>
                    </#list>
                </select>
            </div>
            <div class="talk-data">
                <div class="name">
                    <label for="text">Message</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text" rows="5" cols="30">${text!}</textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit">
            </div>
        </form>
        <#if error??>
            <div class="error">
                ${error!}
            </div>
        </#if>
    </div>
    <div class="datatable">
        <div class="caption">Talks</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>From</th>
                <th>To</th>
                <th>Text</th>
                <th>Time</th>
            </tr>
            </thead>
            <tbody>
            <#list talkViews as talkView>
                <tr>
                    <td>${talkView.talk.id}</td>
                    <td>${talkView.sourceUser.login}</td>
                    <td>${talkView.targetUser.login}</td>
                    <td>${talkView.talk.text}</td>
                    <td>${talkView.talk.creationTime}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>