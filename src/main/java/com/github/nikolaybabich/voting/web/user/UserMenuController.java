package com.github.nikolaybabich.voting.web.user;

import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.service.MenuService;
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

    @GetMapping("/menu-today")
    public List<Menu> getForToday() {
        log.info("get all menus for today");
        return service.getAllByDate(LocalDate.now());
    }

    @GetMapping("/{id}/menu-today")
    public ResponseEntity<Menu> getForTodayByRestaurant(@PathVariable("id") int restaurantId) {
        log.info("get menu of restaurant {} for today", restaurantId);
        return ResponseEntity.of(service.getByDate(restaurantId, LocalDate.now()));
    }
}
