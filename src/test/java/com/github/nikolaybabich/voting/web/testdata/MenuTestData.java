package com.github.nikolaybabich.voting.web.testdata;

import com.github.nikolaybabich.voting.model.BaseEntity;
import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.to.MenuTo;
import com.github.nikolaybabich.voting.util.JsonUtil;
import com.github.nikolaybabich.voting.web.MatcherFactory;

import static com.github.nikolaybabich.voting.web.testdata.DishTestData.*;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant1;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant2;
import static com.github.nikolaybabich.voting.web.testdata.RestaurantTestData.restaurant3;
import static java.time.LocalDate.now;

@SuppressWarnings("unused")
public class MenuTestData {

    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", ".*\\$\\$_hibernate_interceptor");

    public static final int MENU1_ID = 1;
    public static final int TODAY_MENU1_ID = 10;

    public static final Menu menu1 = new Menu(MENU1_ID, now().minusDays(3), restaurant1, dish1, dish2, dish3);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, now().minusDays(3), restaurant2, dish4, dish5);
    public static final Menu menu3 = new Menu(MENU1_ID + 2, now().minusDays(3), restaurant3, dish6, dish7, dish8);
    public static final Menu menu4 = new Menu(MENU1_ID + 3, now().minusDays(2), restaurant1, dish1, dish2, dish3);
    public static final Menu menu5 = new Menu(MENU1_ID + 4, now().minusDays(2), restaurant2, dish4, dish5);
    public static final Menu menu6 = new Menu(MENU1_ID + 5, now().minusDays(2), restaurant3, dish7, dish8);
    public static final Menu menu7 = new Menu(MENU1_ID + 6, now().minusDays(1), restaurant1, dish1, dish2, dish3);
    public static final Menu menu8 = new Menu(MENU1_ID + 7, now().minusDays(1), restaurant2, dish4, dish5, dish9);
    public static final Menu menu9 = new Menu(MENU1_ID + 8, now().minusDays(1), restaurant3, dish10, dish8, dish11);
    public static final Menu menu10 = new Menu(TODAY_MENU1_ID, now(), restaurant1, dish1, dish3);
    public static final Menu menu11 = new Menu(TODAY_MENU1_ID + 1, now(), restaurant2, dish4, dish5, dish9);
    public static final Menu menu12 = new Menu(TODAY_MENU1_ID + 2, now(), restaurant3, dish6, dish10, dish8, dish11);

    public static Menu getNew() {
        return new Menu(null, now().plusDays(1), restaurant1, dish1, dish2);
    }

    public static Menu getUpdated() {
        return new Menu(TODAY_MENU1_ID, now(), restaurant1, dish1, dish2);
    }

    public static MenuTo getTestTo(Menu menu) {
        Integer[] dishIds = menu.getDishes().stream().map(BaseEntity::getId).toArray(Integer[]::new);
        return new MenuTo(menu.getId(), null, menu.getActualDate(), null, dishIds);
    }

    public static String jsonWithDishIds(MenuTo menu) {
        return JsonUtil.writeAdditionProps(menu, "dishIds", menu.getDishIds());
    }
}
