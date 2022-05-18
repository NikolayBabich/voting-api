package com.github.nikolaybabich.voting.web.testdata;

import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.web.MatcherFactory;

import static com.github.nikolaybabich.voting.web.testdata.DishTestData.*;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant1;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant2;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant3;
import static java.time.LocalDate.now;

@SuppressWarnings("unused")
public class MenuTestData {

    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant");

    public static final int MENU1_ID = 1;

    public static final Menu menu1 = new Menu(MENU1_ID, now().minusDays(3), restaurant1, dish1, dish2, dish3);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, now().minusDays(3), restaurant2, dish4, dish5);
    public static final Menu menu3 = new Menu(MENU1_ID + 2, now().minusDays(3), restaurant3, dish6, dish7, dish8);
    public static final Menu menu4 = new Menu(MENU1_ID + 3, now().minusDays(2), restaurant1, dish1, dish2, dish3);
    public static final Menu menu5 = new Menu(MENU1_ID + 4, now().minusDays(2), restaurant2, dish4, dish5);
    public static final Menu menu6 = new Menu(MENU1_ID + 5, now().minusDays(2), restaurant3, dish7, dish8);
    public static final Menu menu7 = new Menu(MENU1_ID + 6, now().minusDays(1), restaurant1, dish1, dish2, dish3);
    public static final Menu menu8 = new Menu(MENU1_ID + 7, now().minusDays(1), restaurant2, dish4, dish5, dish9);
    public static final Menu menu9 = new Menu(MENU1_ID + 8, now().minusDays(1), restaurant3, dish10, dish8, dish11);
    public static final Menu menu10 = new Menu(MENU1_ID + 9, now(), restaurant1, dish1, dish3);
    public static final Menu menu11 = new Menu(MENU1_ID + 10, now(), restaurant2, dish4, dish5, dish9);
    public static final Menu menu12 = new Menu(MENU1_ID + 11, now(), restaurant3, dish6, dish10, dish8, dish11);
}
