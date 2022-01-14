package ru.javaops.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.voting.model.Dish;
import ru.javaops.voting.model.Menu;

import javax.persistence.QueryHint;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    Optional<Menu> findByIdAndRestaurantId(Integer menuId, Integer restaurantId);

    @Query("""
                SELECT m
                  FROM Menu m
            JOIN FETCH m.dishes
                 WHERE m.id = :menuId
                       AND m.restaurant.id = :restaurantId
            """)
    Optional<Menu> getWithDishes(int restaurantId, int menuId);

    @Query("""
                SELECT DISTINCT m
                  FROM Menu m
            JOIN FETCH m.dishes
            JOIN FETCH m.restaurant
                 WHERE m.restaurant.id = :restaurantId
                       AND (m.lunchDate BETWEEN :startDate AND :endDate)
              ORDER BY m.lunchDate DESC
            """)
    @QueryHints(@QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false"))
    List<Menu> findBetweenDates(int restaurantId, LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT DISTINCT m
                  FROM Menu m
            JOIN FETCH m.restaurant
            JOIN FETCH m.dishes d
                 WHERE m.lunchDate = :lunchDate
            """)
    @QueryHints(@QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false"))
    List<Menu> findByDate(LocalDate lunchDate);

    @Query("""
                SELECT DISTINCT m
                  FROM Menu m
            JOIN FETCH m.restaurant
            JOIN FETCH m.dishes d
                 WHERE m.lunchDate = :lunchDate
                       AND m.id IN (SELECT m.id FROM m, m.dishes d
                                     WHERE LOWER(d.name) LIKE CONCAT('%', :dishName, '%'))
               """)
    @QueryHints(@QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false"))
    List<Menu> findByDateAndDishName(LocalDate lunchDate, String dishName);

    @Query("""
                SELECT m
                  FROM Menu m
            JOIN FETCH m.dishes d
                 WHERE d.id = :dishId
               """)
    List<Menu> findByDishId(Integer dishId);

    default Optional<Dish> findDishById(int dishId) {
        return findByDishId(dishId).stream()
                .flatMap(menu -> menu.getDishes().stream())
                .findFirst();
    }
}
