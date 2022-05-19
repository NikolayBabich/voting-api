package com.github.nikolaybabich.voting.web.testdata;

import com.github.nikolaybabich.voting.model.Vote;
import com.github.nikolaybabich.voting.to.VoteTo;
import com.github.nikolaybabich.voting.web.MatcherFactory;

import java.time.LocalDate;

import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant1;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant2;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant3;
import static com.github.nikolaybabich.voting.web.testdata.UserTestData.admin;
import static com.github.nikolaybabich.voting.web.testdata.UserTestData.user;

@SuppressWarnings("unused")
public class VoteTestData {

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class,
            "user.password", "user.registered", ".*\\$\\$_hibernate_interceptor");

    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int VOTE1_ID = 1;
    public static final int ADMIN_TODAY_VOTE_ID = 6;

    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now().minusDays(3), user, restaurant1);
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, LocalDate.now().minusDays(3), admin, restaurant2);
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, LocalDate.now().minusDays(2), user, restaurant1);
    public static final Vote vote4 = new Vote(VOTE1_ID + 3, LocalDate.now().minusDays(2), admin, restaurant1);
    public static final Vote vote5 = new Vote(VOTE1_ID + 4, LocalDate.now().minusDays(1), user, restaurant3);
    public static final Vote vote6 = new Vote(ADMIN_TODAY_VOTE_ID, LocalDate.now(), admin, restaurant2);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), user, restaurant3);
    }

    public static Vote getUpdated() {
        return new Vote(ADMIN_TODAY_VOTE_ID, LocalDate.now(), admin, restaurant3);
    }
}
