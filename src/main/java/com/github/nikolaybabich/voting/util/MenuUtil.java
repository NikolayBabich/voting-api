package com.github.nikolaybabich.voting.util;

import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.to.MenuTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MenuUtil {

    public static Menu createFromTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getActualDate());
    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getId(), menu.getRestaurant().getId(), menu.getActualDate(), menu.getDishes());
    }

    public static List<MenuTo> createTos(Collection<Menu> votes) {
        return votes.stream().map(MenuUtil::createTo).toList();
    }
}
