package com.github.nikolaybabich.voting.util;

import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.to.MenuTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuUtil {

    public static Menu createFromTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getActualDate());
    }
}
