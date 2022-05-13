package com.github.nikolaybabich.voting.repository;

import com.github.nikolaybabich.voting.error.DataConflictException;
import com.github.nikolaybabich.voting.model.Menu;
import com.github.nikolaybabich.voting.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    // https://stackoverflow.com/a/46013654
    @EntityGraph(attributePaths = { "dishes" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restaurantId")
    Optional<Menu> findWithDishes(int id, int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restaurantId")
    Optional<Menu> find(int id, int restaurantId);

    @EntityGraph(attributePaths = { "dishes" }, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> findByActualDate(LocalDate actualDate);

    @EntityGraph(attributePaths = { "dishes" }, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Menu> findByRestaurantAndActualDate(Restaurant restaurant, LocalDate actualDate);

    @EntityGraph(attributePaths = { "dishes" }, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> findByRestaurantAndActualDateBetween(Restaurant restaurant, LocalDate fromDate, LocalDate toDate);

    default Menu checkBelong(int id, int restaurantId) {
        return find(id, restaurantId).orElseThrow(
                () -> new DataConflictException("Menu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}
