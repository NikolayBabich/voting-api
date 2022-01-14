package ru.javaops.voting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.voting.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.voting.MenuTestData.*;
import static ru.javaops.voting.UserTestData.USER_MAIL;

class UserMenuControllerTest extends AbstractControllerTest {
    private static final String USER_MENU_URL = UserMenuController.USER_MENU_URL + '/';

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_MENU_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu17, menu18, menu19, menu20));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getContainingDishForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_MENU_URL)
                .param("dish", "Борщ"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu17, menu18));
    }
}
