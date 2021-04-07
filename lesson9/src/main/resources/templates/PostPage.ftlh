<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="comments" type="ru.itmo.wp.domain.Comment[]" -->
<#import "commons.ftlh" as c>

<@c.page>
        <@c.post post=post/>

        <#if user??>
                <form class="form" method="post" action="">
                        <div class="field">
                                <div class="name">
                                        <label for="text">Text</label>
                                </div>
                                <div class="value">
                                        <textarea id="text" name="text"></textarea>
                                </div>
                                <@c.error "comment.text"/>
                        </div>
                        <div class="button-field">
                                <input type="submit" value="Write">
                        </div>
                </form>
        </#if>

        <#list comments as comm>
            <@c.comment comment=comm/>
        </#list>
</@c.page>
