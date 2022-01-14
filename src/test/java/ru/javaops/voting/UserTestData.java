package ru.javaops.voting;

import ru.javaops.voting.model.Role;
import ru.javaops.voting.model.User;

public class UserTestData {
    public static final String USER_MAIL = "user@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String GOURMET_MAIL = "gourmet@toprest.fr";

    public static final User user = new User(1, "User", USER_MAIL, Role.USER);
    public static final User admin = new User(2, "Admin", ADMIN_MAIL, Role.USER, Role.ADMIN);
    public static final User gourmet = new User(3, "Gourmet", GOURMET_MAIL, Role.USER);
}
