package ru.itmo.web.lesson4.util;

import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import ru.itmo.web.lesson4.model.Color;
import ru.itmo.web.lesson4.model.Post;
import ru.itmo.web.lesson4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov", Color.RED),
            new User(6, "pashka", "Pavel Mavrin", Color.RED),
            new User(9, "geranazarov555", "Georgiy Nazarov", Color.GREEN),
            new User(11, "tourist", "Gennady Korotkevich", Color.BLUE)
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(1L, 1L, "Test Post 1",
                    "looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong\n" +
                            "string"),
            new Post(2L, 6L, "Test Post 2",
                    "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n" +
                            "qwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\nqwe qwe qwe\n"),
            new Post(3L, 9L, "Test Post 3",
                    "asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd" +
                            "asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd\n" +
                            "asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd" +
                            "asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd\n" +
                            "asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd" +
                            "asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd asd\n"),
            new Post(5L, 1L, "Test Post 5", ""),
            new Post(6L, 1L, "Test Post 6",
                    "looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                            "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong\n" +
                            "string")
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);
        data.put("pageDir", request.getRequestURI());

        TemplateHashModel enums = new BeansWrapperBuilder(Configuration.VERSION_2_3_30).build().getEnumModels();
        data.put("enums", enums);

        for (int i = 0; i < USERS.size(); i++) {
            User user = USERS.get(i);
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
