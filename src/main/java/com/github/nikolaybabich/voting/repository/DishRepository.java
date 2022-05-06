package com.github.nikolaybabich.voting.repository;

import com.github.nikolaybabich.voting.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Transactional
    @Modifying
    @Query(value = """
            DELETE
            FROM dishes
            WHERE id IN (
                SELECT dishes.id
                FROM dishes
                    LEFT JOIN menu_dishes ON dishes.id = menu_dishes.dish_id
                WHERE menu_dishes.menu_id IS NULL)""",
            nativeQuery = true)
    void deleteOrphans();
}
