<#-- @ftlvariable name="title" type="java.lang.String" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <ul class="articles">
<#--        <li class="noData">No data</li>-->
        <template>
                <li class="article">
                    <@c.article/>
                </li>
        </template>
    </ul>

    <script>
        $(function () {
            const $noData = $(".articles .noData");
            $noData.text("Loading...");

            ajax(
                {
                    action: "action"
                },
                function(response) {
                    response["articles"].forEach(function(articleView) {
                        $noData.hide();
                        const article = articleView["article"];
                        const user = articleView["user"];

                        const $li = $($(".articles template").prop("content")).clone();
                        $li.find(".title").text(article["title"]);
                        $li.find(".information").text("By " + user["login"] + ", " + article["creationTime"]);
                        $li.find(".text").text(article["text"]);
                        $li.find(".footer .right").text(article["creationTime"]);
                        // $li.find("tr").attr("data-userId", user["id"]);
                        $(".articles").append($li);

                    });
                });
        })
    </script>
</@c.page>
