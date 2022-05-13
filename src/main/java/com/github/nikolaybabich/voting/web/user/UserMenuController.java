package com.github.nikolaybabich.voting.web.user;

import com.github.nikolaybabich.voting.service.MenuService;
import com.github.nikolaybabich.voting.to.MenuTo;
import com.github.nikolaybabich.voting.util.MenuUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = UserMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserMenuController {

    static final String REST_URL = "/api/restaurants";

    private final MenuService service;

    @GetMapping("/menus/today")
    public List<MenuTo> getForToday() {
        log.info("get all menus for today");
        return MenuUtil.createTos(service.getAllByDate(LocalDate.now()));
    }

    @GetMapping("/{id}/menus/today")
    public ResponseEntity<MenuTo> getForTodayByRestaurant(@PathVariable("id") int restaurantId) {
        log.info("get menu of restaurant {} for today", restaurantId);
        return ResponseEntity.of(service.getByDate(restaurantId, LocalDate.now()).map(MenuUtil::createTo));
    }
}
