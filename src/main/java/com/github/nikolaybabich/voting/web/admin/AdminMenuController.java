package com.github.nikolaybabich.voting.web.admin;

import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.service.MenuService;
import com.github.nikolaybabich.voting.to.MenuTo;
import com.github.nikolaybabich.voting.util.DateTimeUtil;
import com.github.nikolaybabich.voting.util.MenuUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.nikolaybabich.voting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.nikolaybabich.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(path = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuController {

    static final String REST_URL = AdminRestaurantController.ADMIN_RESTAURANTS_URL + "/{restaurantId}/menus";

    private final MenuService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create {} of restaurant {}", menuTo, restaurantId);
        checkNew(menuTo);
        Menu created = service.create(MenuUtil.createFromTo(menuTo), restaurantId, menuTo.getDishIds());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource)
                .body(created);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody MenuTo menuTo, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} of restaurant {}", menuTo, restaurantId);
        assureIdConsistent(menuTo, id);
        service.update(MenuUtil.createFromTo(menuTo), restaurantId, menuTo.getDishIds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get menu {} of restaurant {}", id, restaurantId);
        return ResponseEntity.of(service.get(id, restaurantId));
    }

    @GetMapping
    public List<Menu> getHistory(@PathVariable int restaurantId,
                                 @RequestParam("from") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                 @RequestParam("to") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        log.info("get menu history of restaurant {} between dates [{} <-> {}]", restaurantId, fromDate, toDate);
        fromDate = DateTimeUtil.getOrMin(fromDate);
        toDate = DateTimeUtil.getOrMax(toDate);
        DateTimeUtil.checkDates(fromDate, toDate);

        return service.getBetween(restaurantId, fromDate, toDate);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu {} of restaurant {}", id, restaurantId);
        service.delete(id, restaurantId);
    }
}
