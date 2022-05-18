package com.github.nikolaybabich.voting.web.user;

import com.github.nikolaybabich.voting.repository.VoteRepository;
import com.github.nikolaybabich.voting.to.VoteTo;
import com.github.nikolaybabich.voting.util.DateTimeUtil;
import com.github.nikolaybabich.voting.util.JsonUtil;
import com.github.nikolaybabich.voting.util.VoteUtil;
import com.github.nikolaybabich.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;

import static com.github.nikolaybabich.voting.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_VOTE;
import static com.github.nikolaybabich.voting.web.testdata.UserTestData.ADMIN_MAIL;
import static com.github.nikolaybabich.voting.web.testdata.UserTestData.USER_MAIL;
import static com.github.nikolaybabich.voting.web.testdata.VoteTestData.*;
import static com.github.nikolaybabich.voting.web.user.VoteController.DEADLINE_TO_CHANGE_VOTE;
import static com.github.nikolaybabich.voting.web.user.VoteController.TOO_LATE_MESSAGE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + '/';
    private static final LocalTime BEFORE_DEADLINE = DEADLINE_TO_CHANGE_VOTE.minusMinutes(1);
    private static final LocalTime AFTER_DEADLINE = DEADLINE_TO_CHANGE_VOTE.plusMinutes(1);

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception {
        VoteTo newVote = VoteUtil.createTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_TO_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createTo(voteRepository.getById(newId)), newVote);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        VoteTo newVote = VoteUtil.createTo(getNew());
        perform(MockMvcRequestBuilders.post(REST_URL + "today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_VOTE.formatted(ADMIN_TODAY_VOTE_ID))));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        // https://www.baeldung.com/mockito-mock-static-methods#mocking-a-no-argument-static-method
        try (MockedStatic<DateTimeUtil> mockedStatic = mockStatic(DateTimeUtil.class)) {
            mockedStatic.when(DateTimeUtil::getCurrentTime).thenReturn(BEFORE_DEADLINE);

            VoteTo updated = VoteUtil.createTo(getUpdated());
            perform(MockMvcRequestBuilders.put(REST_URL + "today")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            VOTE_MATCHER.assertMatch(voteRepository.getById(ADMIN_TODAY_VOTE_ID), getUpdated());
        }
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateTooLate() throws Exception {
        try (MockedStatic<DateTimeUtil> mockedStatic = mockStatic(DateTimeUtil.class)) {
            mockedStatic.when(DateTimeUtil::getCurrentTime).thenReturn(AFTER_DEADLINE);

            VoteTo updated = VoteUtil.createTo(getUpdated());
            perform(MockMvcRequestBuilders.put(REST_URL + "today")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updated)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(content().string(containsString(TOO_LATE_MESSAGE)));
        }
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.createTos(vote1, vote3, vote5)));
    }
}
