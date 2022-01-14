package ru.javaops.voting.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.voting.AbstractControllerTest;
import ru.javaops.voting.model.Menu;
import ru.javaops.voting.repository.MenuRepository;
import ru.javaops.voting.util.JsonUtil;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.voting.MenuTestData.*;
import static ru.javaops.voting.RestaurantTestData.RESTAURANT1_ID;
import static ru.javaops.voting.RestaurantTestData.RESTAURANT2_ID;
import static ru.javaops.voting.UserTestData.ADMIN_MAIL;
import static ru.javaops.voting.error.UpdateRestrictionException.RestrictionType.MENU_DATE_RESTRICTION;
import static ru.javaops.voting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH;

class AdminMenuControllerTest extends AbstractControllerTest {
    private static final String ADMIN_RESTAURANTS_URL = AdminRestaurantController.ADMIN_RESTAURANTS_URL + '/';
    private static final String RESTAURANT1_MENUS_URL = ADMIN_RESTAURANTS_URL + RESTAURANT1_ID + "/menus/";
    private static final String RESTAURANT2_MENUS_URL = ADMIN_RESTAURANTS_URL + RESTAURANT2_ID + "/menus/";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT1_MENUS_URL + MENU17_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu17));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT2_MENUS_URL)
                .param("start", LocalDate.now().minusDays(1).toString())
                .param("end", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu18, menu14));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(RESTAURANT1_MENUS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        //noinspection OptionalGetWithoutIsPresent
        MENU_MATCHER.assertMatch(menuRepository.getWithDishes(RESTAURANT1_ID, newId).get(), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(RESTAURANT1_MENUS_URL + MENU17_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        //noinspection OptionalGetWithoutIsPresent
        MENU_MATCHER.assertMatch(menuRepository.getWithDishes(RESTAURANT1_ID, MENU17_ID).get(), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateMenuWithDuplicateDish() throws Exception {
        Menu updated = getWithDuplicateDish();
        MvcResult result = perform(MockMvcRequestBuilders.put(RESTAURANT1_MENUS_URL + MENU17_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated)))
                        .andDo(print())
                        .andExpect(status().isConflict())
                        .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(EXCEPTION_DUPLICATE_DISH, String.valueOf(DISH1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateYesterdayMenu() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.put(RESTAURANT1_MENUS_URL + MENU13_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menu13)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(MENU_DATE_RESTRICTION.getMessage());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(RESTAURANT1_MENUS_URL + MENU17_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(menuRepository.findById(MENU17_ID).isPresent());
    }
}