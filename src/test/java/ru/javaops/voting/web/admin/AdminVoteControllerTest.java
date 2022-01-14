package ru.javaops.voting.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.voting.AbstractControllerTest;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.voting.RestaurantTestData.RESTAURANT2_ID;
import static ru.javaops.voting.UserTestData.ADMIN_MAIL;
import static ru.javaops.voting.VoteTestData.*;

class AdminVoteControllerTest extends AbstractControllerTest {
    private static final String ADMIN_VOTES_URL = AdminVoteController.ADMIN_VOTES_URL + '/';

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllFromYesterday() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES_URL)
                .param("start", LocalDate.now().minusDays(1).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote11, vote10, vote9, vote8));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(ADMIN_VOTES_URL)
                .param("rid", String.valueOf(RESTAURANT2_ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote6, vote4));
    }
}
