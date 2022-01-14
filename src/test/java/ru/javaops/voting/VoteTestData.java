package ru.javaops.voting;

import ru.javaops.voting.model.Restaurant;
import ru.javaops.voting.model.Vote;

import java.time.LocalDate;

import static ru.javaops.voting.RestaurantTestData.*;
import static ru.javaops.voting.UserTestData.admin;
import static ru.javaops.voting.UserTestData.gourmet;
import static ru.javaops.voting.UserTestData.user;

@SuppressWarnings("unused")
public class VoteTestData {
    public static final int GOURMET_TODAY_VOTE_ID = 11;

    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user.registered", "user.roles");
    public static final MatcherFactory.Matcher<Vote> VOTE_NO_DETAILS_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant.name", "restaurant.address");

    public static final Vote vote1 = new Vote(1, user, restaurant3, LocalDate.now().minusDays(4));
    public static final Vote vote2 = new Vote(2, admin, restaurant1, LocalDate.now().minusDays(4));
    public static final Vote vote3 = new Vote(3, user, restaurant3, LocalDate.now().minusDays(3));
    public static final Vote vote4 = new Vote(4, admin, restaurant2, LocalDate.now().minusDays(3));
    public static final Vote vote5 = new Vote(5, gourmet, restaurant4, LocalDate.now().minusDays(3));
    public static final Vote vote6 = new Vote(6, user, restaurant2, LocalDate.now().minusDays(2));
    public static final Vote vote7 = new Vote(7, gourmet, restaurant4, LocalDate.now().minusDays(2));
    public static final Vote vote8 = new Vote(8, user, restaurant1, LocalDate.now().minusDays(1));
    public static final Vote vote9 = new Vote(9, admin, restaurant1, LocalDate.now().minusDays(1));
    public static final Vote vote10 = new Vote(10, gourmet, restaurant4, LocalDate.now().minusDays(1));
    public static final Vote vote11 = new Vote(11, gourmet, restaurant3, LocalDate.now());

    public static Vote getNew() {
        return new Vote(null, null, new Restaurant(RESTAURANT1_ID, null, null), LocalDate.now());
    }

    public static Vote getUpdated() {
        return new Vote(GOURMET_TODAY_VOTE_ID, null, new Restaurant(RESTAURANT2_ID, null, null), LocalDate.now());
    }
}
