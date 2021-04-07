<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="article-form">
        <form method="post" action="">
            <input type="hidden" name="action" value="create"/>

            <div class="title">
                <div class="name">
                    <label for="title">Title</label>
                </div>
                <div class="value">
                    <input id="title" name="title" value=""/>
                </div>
            </div>

            <div class="text">
                <div class="name">
                    <label for="text">Text</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text" rows="15" cols="60"></textarea>
                </div>
            </div>

            <div class="error"></div>
            <div class="button">
                <input type="submit" value="Create">
            </div>
        </form>

    </div>

    <script>
        $(function () {
            $(".article-form form").submit(function () {
                const title = $(this).find("input[name='title']").val();
                const text = $(this).find("textarea[name='text']").val();
                const $error = $(this).find(".error");

                ajax(
                    {
                        action: "create",
                        title,
                        text
                    },
                    function (response) {
                        if (response["error"]) {
                            $error.text(response["error"]);
                        } else {
                            location.href = "/"
                        }
                    });
                return false;
            });
        })
    </script>

</@c.page>