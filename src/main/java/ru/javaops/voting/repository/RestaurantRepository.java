package ru.javaops.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.voting.model.Restaurant;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findByNameContains(String name);

    @Transactional
    @Modifying
    @Query(value = """
                    DELETE FROM menu_dish
                        WHERE menu_dish.menu_id IN (
                            SELECT menu.id FROM menu WHERE menu.restaurant_id = :restaurantId);
                    DELETE FROM menu
                        WHERE menu.restaurant_id = :restaurantId ;
                    DELETE FROM restaurant
                        WHERE restaurant.id = :restaurantId
                   """, nativeQuery = true)
    void deleteById(int restaurantId);
}
