package com.github.nikolaybabich.voting.web.admin;

import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.repository.MenuRepository;
import com.github.nikolaybabich.voting.to.MenuTo;
import com.github.nikolaybabich.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.nikolaybabich.voting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_MENU;
import static com.github.nikolaybabich.voting.web.testdata.MenuTestData.*;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.RESTAURANT1_ID;
import static com.github.nikolaybabich.voting.web.testdata.UserTestData.ADMIN_MAIL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String ADMIN_RESTAURANTS_URL = AdminRestaurantController.REST_URL + '/';
    private static final String RESTAURANT1_MENUS_URL = ADMIN_RESTAURANTS_URL + RESTAURANT1_ID + "/menus/";
    private static final String YESTERDAY_DATE = LocalDate.now().minusDays(1).toString();

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newMenu = getTestTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(RESTAURANT1_MENUS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithDishIds(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        Menu expected = getNew();
        expected.setId(newId);
        MENU_MATCHER.assertMatch(created, expected);
        MENU_MATCHER.assertMatch(menuRepository.getById(newId), expected);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuTo duplicate = getTestTo(new Menu(menu10));
        duplicate.setId(null);
        perform(MockMvcRequestBuilders.post(RESTAURANT1_MENUS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithDishIds(duplicate)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_MENU.formatted(TODAY_MENU1_ID))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo updated = getTestTo(getUpdated());
        perform(MockMvcRequestBuilders.put(RESTAURANT1_MENUS_URL + TODAY_MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithDishIds(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(menuRepository.getById(TODAY_MENU1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT1_MENUS_URL + TODAY_MENU1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu10));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT1_MENUS_URL)
                .param("from", YESTERDAY_DATE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu7, menu10));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(RESTAURANT1_MENUS_URL + TODAY_MENU1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findById(TODAY_MENU1_ID).isPresent());
    }
}
