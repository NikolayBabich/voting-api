package com.github.nikolaybabich.voting.service;

import com.github.nikolaybabich.voting.model.Dish;
import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.repository.DishRepository;
import com.github.nikolaybabich.voting.repository.MenuRepository;
import com.github.nikolaybabich.voting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    @Transactional
    public Menu create(Menu menu, int restaurantId, List<Integer> dishIds) {
        return prepareAndSave(menu, restaurantId, dishIds);
    }

    @Transactional
    public void update(Menu menu, int restaurantId, List<Integer> dishIds) {
        menuRepository.checkBelong(menu.id(), restaurantId);
        prepareAndSave(menu, restaurantId, dishIds);
    }

    private Menu prepareAndSave(Menu menu, int restaurantId, List<Integer> dishIds) {
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        Dish[] dishes = dishIds.stream().map(dishRepository::getById).toArray(Dish[]::new);
        menu.setDishes(Set.of(dishes));
        return menuRepository.save(menu);
    }

    public List<Menu> getAllByDate(LocalDate actualDate) {
        return menuRepository.findByActualDate(actualDate);
    }

    public Optional<Menu> getByDate(int restaurantId, LocalDate actualDate) {
        return menuRepository.findByRestaurantAndActualDate(restaurantRepository.getById(restaurantId), actualDate);
    }

    public List<Menu> getBetween(int restaurantId, LocalDate fromDate, LocalDate toDate) {
        return menuRepository.findByRestaurantAndActualDateBetween(
                restaurantRepository.getById(restaurantId), fromDate, toDate);
    }

    @Transactional
    public void delete(int id, int restaurantId) {
        Menu menu = menuRepository.checkBelong(id, restaurantId);
        menuRepository.delete(menu);
        dishRepository.deleteOrphans();
    }
}
