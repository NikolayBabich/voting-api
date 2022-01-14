package ru.javaops.voting.web.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
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
import ru.javaops.voting.model.Restaurant;
import ru.javaops.voting.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ru.javaops.voting.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.voting.util.ValidationUtil.checkNew;
import static ru.javaops.voting.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(path = AdminRestaurantController.ADMIN_RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantController {
    public static final String ADMIN_RESTAURANTS_URL = "/api/admin/restaurants";

    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Restaurant> getAll(@RequestParam("name") @Nullable String restaurantName) {
        if (StringUtils.hasText(restaurantName)) {
            log.info("get all restaurants containing \"{}\"", restaurantName);
            return restaurantRepository.findByNameContains(restaurantName);
        } else {
            log.info("get all restaurants");
            return restaurantRepository.findAll();
        }
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable("id") int restaurantId) {
        log.info("get restaurant {}", restaurantId);
        return checkNotFound(restaurantRepository.findById(restaurantId), Restaurant.class, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_RESTAURANTS_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "menus", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable("id") int restaurantId) {
        log.info("update {} with id={}", restaurant, restaurantId);
        assureIdConsistent(restaurant, restaurantId);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "menus", allEntries = true)
    public void delete(@PathVariable("id") int restaurantId) {
        log.info("delete restaurant {}", restaurantId);
        if (!restaurantRepository.existsById(restaurantId)) {
            checkNotFound(Optional.empty(), Restaurant.class, restaurantId);
        }
        restaurantRepository.deleteById(restaurantId);
    }
}
