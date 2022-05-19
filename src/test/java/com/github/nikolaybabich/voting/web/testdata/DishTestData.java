package com.github.nikolaybabich.voting.web.testdata;

import com.github.nikolaybabich.voting.model.Dish;
import com.github.nikolaybabich.voting.web.MatcherFactory;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class DishTestData {

    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class);

    public static final int DISH1_ID = 1;

    public static final Dish dish1 = new Dish(DISH1_ID, "Утка по-пекински", 1200);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "Фуа-гра", 4500);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "Паста болоньезе", 650);
    public static final Dish dish4 = new Dish(DISH1_ID + 3, "Котлета по-киевски", 750);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "Борщ украинский", 600);
    public static final Dish dish6 = new Dish(DISH1_ID + 5, "Суп гороховый", 500);
    public static final Dish dish7 = new Dish(DISH1_ID + 6, "Щи малосольные", 400);
    public static final Dish dish8 = new Dish(DISH1_ID + 7, "Плов из говядины", 950);
    public static final Dish dish9 = new Dish(DISH1_ID + 8, "Солянка по-домашнему", 750);
    public static final Dish dish10 = new Dish(DISH1_ID + 9, "Щи малосольные", 350);
    public static final Dish dish11 = new Dish(DISH1_ID + 10, "Люля-кебаб", 900);

    public static Dish getNew() {
        return new Dish(null, "New dish", 200);
    }

    public static Collection<Dish> getAllTestDishes() {
        return List.of(dish1, dish2, dish3, dish4, dish5, dish6, dish7, dish8, dish9, dish10, dish11);
    }
}
