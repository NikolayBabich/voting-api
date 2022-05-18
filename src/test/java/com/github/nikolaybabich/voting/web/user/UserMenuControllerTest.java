package com.github.nikolaybabich.voting.web.user;

import com.github.nikolaybabich.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.nikolaybabich.voting.web.testdata.MenuTestData.MENU_MATCHER;
import static com.github.nikolaybabich.voting.web.testdata.MenuTestData.menu10;
import static com.github.nikolaybabich.voting.web.testdata.MenuTestData.menu11;
import static com.github.nikolaybabich.voting.web.testdata.MenuTestData.menu12;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.RESTAURANT1_ID;
import static com.github.nikolaybabich.voting.web.testdata.UserTestData.GUEST_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserMenuController.REST_URL + '/';

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getAllForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menus/today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu10, menu11, menu12));
    }

    @Test
    @WithUserDetails(value = GUEST_MAIL)
    void getForTodayByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/menus/today"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu10));
    }
}
