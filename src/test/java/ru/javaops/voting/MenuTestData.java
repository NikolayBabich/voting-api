package ru.javaops.voting;

import ru.javaops.voting.model.Dish;
import ru.javaops.voting.model.Menu;

import java.time.LocalDate;

import static ru.javaops.voting.RestaurantTestData.restaurant1;
import static ru.javaops.voting.RestaurantTestData.restaurant2;
import static ru.javaops.voting.RestaurantTestData.restaurant3;
import static ru.javaops.voting.RestaurantTestData.restaurant4;

@SuppressWarnings("unused")
public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            Menu.class,
            "restaurant", // may be lazy-initialized
            "dishes.id"); // identity generator doesn't roll back

    public static final int DISH1_ID = 1;

    public static final Dish dish1 = new Dish(1, "Утка по-пекински", 1200);
    public static final Dish dish2 = new Dish(2, "Котлета по-киевски", 750);
    public static final Dish dish3 = new Dish(3, "Борщ украинский", 600);
    public static final Dish dish4 = new Dish(4, "Фуа-гра", 4500);
    public static final Dish dish5 = new Dish(5, "Паста болоньезе", 650);
    public static final Dish dish6 = new Dish(6, "Суп гороховый", 500);
    public static final Dish dish7 = new Dish(7, "Щи малосольные", 400);
    public static final Dish dish8 = new Dish(8, "Люля-кебаб", 900);

    public static final Dish createdDish = new Dish(null, "Новое блюдо", 3000);

    public static final int MENU1_ID = 1;
    public static final int MENU13_ID = 13;
    public static final int MENU17_ID = 17;

    public static final Menu menu1 = new Menu(1, LocalDate.now().minusDays(4), restaurant1, dish1, dish2, dish3);
    public static final Menu menu2 = new Menu(2, LocalDate.now().minusDays(4), restaurant2, dish3, dish4);
    public static final Menu menu3 = new Menu(3, LocalDate.now().minusDays(4), restaurant3, dish6, dish7, dish8);
    public static final Menu menu4 = new Menu(4, LocalDate.now().minusDays(4), restaurant4, dish4, dish5);
    public static final Menu menu5 = new Menu(5, LocalDate.now().minusDays(3), restaurant1, dish1, dish3);
    public static final Menu menu6 = new Menu(6, LocalDate.now().minusDays(3), restaurant2, dish3, dish4, dish5);
    public static final Menu menu7 = new Menu(7, LocalDate.now().minusDays(3), restaurant3, dish6, dish7, dish8);
    public static final Menu menu8 = new Menu(8, LocalDate.now().minusDays(3), restaurant4, dish4, dish5, dish1);
    public static final Menu menu9 = new Menu(9, LocalDate.now().minusDays(2), restaurant1, dish1, dish2, dish3);
    public static final Menu menu10 = new Menu(10, LocalDate.now().minusDays(2), restaurant2, dish3, dish4);
    public static final Menu menu11 = new Menu(11, LocalDate.now().minusDays(2), restaurant3, dish6, dish7, dish8);
    public static final Menu menu12 = new Menu(12, LocalDate.now().minusDays(2), restaurant4, dish4, dish5);
    public static final Menu menu13 = new Menu(13, LocalDate.now().minusDays(1), restaurant1, dish1, dish2, dish3);
    public static final Menu menu14 = new Menu(14, LocalDate.now().minusDays(1), restaurant2, dish3, dish4);
    public static final Menu menu15 = new Menu(15, LocalDate.now().minusDays(1), restaurant3, dish6, dish7, dish8);
    public static final Menu menu16 = new Menu(16, LocalDate.now().minusDays(1), restaurant4, dish4, dish5);
    public static final Menu menu17 = new Menu(17, LocalDate.now(), restaurant1, dish1, dish3);
    public static final Menu menu18 = new Menu(18, LocalDate.now(), restaurant2, dish3, dish7);
    public static final Menu menu19 = new Menu(19, LocalDate.now(), restaurant3, dish6, dish7, dish8);
    public static final Menu menu20 = new Menu(20, LocalDate.now(), restaurant4, dish4, dish5, dish1);

    public static Menu getNew() {
        return new Menu(null, LocalDate.now().plusDays(1), restaurant1, dish1, dish2, createdDish);
    }

    public static Menu getUpdated() {
        return new Menu(MENU17_ID, LocalDate.now(), restaurant1, createdDish, dish2);
    }

    public static Menu getWithDuplicateDish() {
        Dish duplicateDish = new Dish(dish1);
        return new Menu(17, LocalDate.now(), restaurant1, duplicateDish, dish2);
    }
}
