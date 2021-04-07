<#-- @ftlvariable name="noticeForm" type="ru.itmo.wp.form.UserCredentials" -->
<#import "/spring.ftl" as spring>
<#import "commons.ftlh" as c>

<@c.page>
    <div class="notice-form">
        <form method="post" action="">
            <div class="name">
                <label for="content">Content</label>
            </div>
            <div class="value">
                <textarea id="content" name="content" rows="15" cols="60"></textarea>
            </div>
            <@c.error "noticeForm.content"/>
            <div class="button">
                <input type="submit" value="create">
            </div>
        </form>
</@c.page>