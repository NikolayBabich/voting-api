package ru.javaops.voting.web.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.voting.model.Dish;
import ru.javaops.voting.repository.MenuRepository;

import static ru.javaops.voting.util.ValidationUtil.checkNotFound;

@RestController
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    private final MenuRepository menuRepository;

    @GetMapping(path = "/api/admin/dishes/{dishId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish get(@PathVariable int dishId) {
        log.info("get dish {}", dishId);
        return checkNotFound(menuRepository.findDishById(dishId), Dish.class, dishId);
    }
}