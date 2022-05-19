package com.github.nikolaybabich.voting.web.testdata;

import com.github.nikolaybabich.voting.model.Restaurant;
import com.github.nikolaybabich.voting.web.MatcherFactory;

@SuppressWarnings("unused")
public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Тройка", "Загородный пр., 27/21");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Usoff", "наб. Лейтенанта Шмидта, 19");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Палкинъ", "Невский пр., 47");

    public static Restaurant getNew() {
        return new Restaurant(null, "New restaurant", "New address");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Updated name", "Updated address");
    }
}
