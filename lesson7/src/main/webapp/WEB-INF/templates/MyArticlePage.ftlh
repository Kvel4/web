<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="my_article datatable">
    <table>
    <thead>
        <tr>
            <th>Id</th>
            <th>Title</th>
            <th>Hidden</th>
        </tr>
    </thead>
    <tbody>
        <#list articles as article>
            <tr>
                <td class="article_id">${article.id}</td>
                <td class="article_title">${article.title}</td>
                <td class="hidden"><input data-articleId="${article.id}" type="submit" <#if article.hidden>value="show" <#else>value="hide"</#if>></td>
            </tr>
        </#list>
    </tbody>
    </table>
    </div>
    <script>
        $(function () {
            $(".my_article tr .hidden input").click(function () {
                $clicked_button = $(this);
                ajax(
                    {
                        action: "changeSecrecy",
                        buttonValue: $(this).val(),
                        articleId: $(this).attr("data-/......"),
                    },
                    function (response) {
                        $clicked_button.val(response["buttonValue"])
                    });
            });

            // ajax(
            //     {
            //         action: "changeSecrecy"
            //     },
            //     function () {
            //
            //     });
        });

    </script>
</@c.page>