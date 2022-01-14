package ru.javaops.voting;

import ru.javaops.voting.model.Restaurant;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int NOT_FOUND = 123;

    public static final Restaurant restaurant1 = new Restaurant(1, "Тройка", "Загородный пр., 27/21");
    public static final Restaurant restaurant2 = new Restaurant(2, "Usoff", "наб. Лейтенанта Шмидта, 19");
    public static final Restaurant restaurant3 = new Restaurant(3, "Мари Vanna", "Мытнинская наб., 3");
    public static final Restaurant restaurant4 = new Restaurant(4, "Палкинъ", "Невский пр., 47");

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый рестик", "Садовая ул., 62");
    }
}
