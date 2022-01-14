package ru.javaops.voting.web.user;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.voting.AbstractControllerTest;
import ru.javaops.voting.model.Vote;
import ru.javaops.voting.repository.VoteRepository;
import ru.javaops.voting.util.DateTimeUtil;
import ru.javaops.voting.util.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.voting.UserTestData.GOURMET_MAIL;
import static ru.javaops.voting.UserTestData.USER_MAIL;
import static ru.javaops.voting.VoteTestData.*;
import static ru.javaops.voting.error.UpdateRestrictionException.RestrictionType.VOTE_TIME_RESTRICTION;
import static ru.javaops.voting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE;
import static ru.javaops.voting.web.user.UserVoteController.DEADLINE_TO_CHANGE_VOTE;

class UserVoteControllerTest extends AbstractControllerTest {
    private static final String USER_VOTES_URL = UserVoteController.USER_VOTES_URL + '/';

    @Autowired
    VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForPastTwoDays() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_VOTES_URL)
                .param("start", LocalDate.now().minusDays(2).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote8, vote6));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(USER_VOTES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_NO_DETAILS_MATCHER.assertMatch(created, newVote);
        VOTE_NO_DETAILS_MATCHER.assertMatch(voteRepository.getById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = GOURMET_MAIL)
    void createDuplicate() throws Exception {
        Vote newVote = getNew();
        MvcResult result = perform(MockMvcRequestBuilders.post(USER_VOTES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(EXCEPTION_DUPLICATE_VOTE, String.valueOf(GOURMET_TODAY_VOTE_ID));
    }

    @Test
    @WithUserDetails(value = GOURMET_MAIL)
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_VOTES_URL + GOURMET_TODAY_VOTE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote11));
    }

    @Test
    @WithUserDetails(value = GOURMET_MAIL)
    void updateBeforeDeadline() throws Exception {
        Vote updated = getUpdated();
        LocalTime beforeDeadline = DEADLINE_TO_CHANGE_VOTE.minus(1, ChronoUnit.MINUTES);
        try (MockedStatic<DateTimeUtil> mockedStatic = mockStatic(DateTimeUtil.class)) {
            mockedStatic.when(DateTimeUtil::getCurrentTime).thenReturn(beforeDeadline);
            perform(MockMvcRequestBuilders.put(USER_VOTES_URL + GOURMET_TODAY_VOTE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            VOTE_NO_DETAILS_MATCHER.assertMatch(voteRepository.getById(GOURMET_TODAY_VOTE_ID), getUpdated());
        }
    }

    @Test
    @WithUserDetails(value = GOURMET_MAIL)
    void updateAfterDeadline() throws Exception {
        Vote updated = getUpdated();
        LocalTime afterDeadline = DEADLINE_TO_CHANGE_VOTE.plus(1, ChronoUnit.MINUTES);
        try (MockedStatic<DateTimeUtil> mockedStatic = mockStatic(DateTimeUtil.class)) {
            mockedStatic.when(DateTimeUtil::getCurrentTime).thenReturn(afterDeadline);
            MvcResult result = perform(MockMvcRequestBuilders.put(USER_VOTES_URL + GOURMET_TODAY_VOTE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andReturn();

            assertThat(result.getResponse().getContentAsString()).contains(VOTE_TIME_RESTRICTION.getMessage());
        }
    }
}
