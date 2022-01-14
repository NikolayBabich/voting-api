package ru.javaops.voting.web.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.voting.model.Menu;
import ru.javaops.voting.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class UserMenuController {
    static final String USER_MENU_URL = "/api/menus";

    private final MenuRepository menuRepository;

    @GetMapping(path = USER_MENU_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable("menus")
    public List<Menu> getForToday(@RequestParam(value="dish", required = false) String dishName) {
        if (StringUtils.hasText(dishName)) {
            log.info("get today menus containing \"{}\"", dishName);
            return menuRepository.findByDateAndDishName(LocalDate.now(), dishName.toLowerCase());
        } else {
            log.info("get all today menus");
            return menuRepository.findByDate(LocalDate.now());
        }
    }
}
