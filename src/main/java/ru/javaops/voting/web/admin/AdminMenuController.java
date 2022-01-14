package ru.javaops.voting.web.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import ru.javaops.voting.error.IllegalRequestDataException;
import ru.javaops.voting.model.Dish;
import ru.javaops.voting.model.Menu;
import ru.javaops.voting.model.Restaurant;
import ru.javaops.voting.repository.MenuRepository;
import ru.javaops.voting.repository.RestaurantRepository;
import ru.javaops.voting.util.DateTimeUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.springframework.format.annotation.DateTimeFormat.ISO;
import static ru.javaops.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.voting.util.ValidationUtil.checkMenuUpdateRestriction;
import static ru.javaops.voting.util.ValidationUtil.checkNew;
import static ru.javaops.voting.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(path = AdminRestaurantController.ADMIN_RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuController {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping("/{restaurantId}/menus/{menuId}")
    public Menu get(@PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("get menu {} for restaurant {}", menuId, restaurantId);
        return checkNotFound(menuRepository.getWithDishes(restaurantId, menuId), Menu.class, menuId, restaurantId);
    }

    @GetMapping("/{restaurantId}/menus")
    public List<Menu> getBetween(@PathVariable int restaurantId,
                                 @RequestParam(name = "start", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
                                 @RequestParam(name = "end", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        log.info("get menus between dates ({} <-> {}) for restaurant with id={}", startDate, endDate, restaurantId);
        checkNotFound(restaurantRepository.findById(restaurantId), Restaurant.class, restaurantId);
        startDate = DateTimeUtil.getOrMin(startDate);
        endDate = DateTimeUtil.getOrMax(endDate);
        if (endDate.isBefore(startDate)) {
            throw new IllegalRequestDataException("startDate must be earlier than endDate");
        }
        return menuRepository.findBetweenDates(restaurantId, startDate, endDate);
    }

    @PostMapping(path = "/{restaurantId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(cacheNames = "menus", allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable int restaurantId) {
        log.info("create menu for restaurant {}", restaurantId);
        checkNew(menu);
        checkMenuUpdateRestriction(menu);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));

        // When any of dishes already has id (so in detached state)
        // merge() should be used instead of persist(), so need to call save() twice
        Set<Dish> tmp = menu.getDishes();
        menu.setDishes(null);
        menuRepository.save(menu);
        menu.setDishes(tmp);

        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AdminRestaurantController.ADMIN_RESTAURANTS_URL + "/{restaurantId}/menus/{menuId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = "/{restaurantId}/menus/{menuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "menus", allEntries = true)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("update menu {} for restaurant {}", menuId, restaurantId);
        assureIdConsistent(menu, menuId);
        checkMenuUpdateRestriction(menu);
        checkNotFound(menuRepository.findByIdAndRestaurantId(menuId, restaurantId), Menu.class, menuId, restaurantId);
        log.debug("found menu {} for restaurant {}", menuId, restaurantId);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        menuRepository.save(menu);
    }

    @DeleteMapping(path = "/{restaurantId}/menus/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "menus", allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("delete menu {} for restaurant {}", menuId, restaurantId);
        Menu menu = checkNotFound(menuRepository.findByIdAndRestaurantId(menuId, restaurantId), Menu.class, menuId, restaurantId);
        log.debug("found menu {} for restaurant {}", menuId, restaurantId);
        menuRepository.delete(menu);
    }
}
