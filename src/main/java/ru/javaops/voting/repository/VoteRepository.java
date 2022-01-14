package ru.javaops.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    // https://stackoverflow.com/a/46013654
    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Vote> findByIdAndUserId(Integer id, Integer userId);

    @Query("""
                SELECT v
                  FROM Vote v
            JOIN FETCH v.user u
            JOIN FETCH v.restaurant r
                 WHERE u.id = :userId AND r.id = :restaurantId
                       AND (v.lunchDate BETWEEN :startDate AND :endDate)
              ORDER BY v.lunchDate DESC, u.username
            """)
    List<Vote> findByRestaurantIdAndUserId(Integer restaurantId, Integer userId, LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT v
                  FROM Vote v
            JOIN FETCH v.user u
            JOIN FETCH v.restaurant r
                 WHERE r.id = :restaurantId
                       AND (v.lunchDate BETWEEN :startDate AND :endDate)
              ORDER BY v.lunchDate DESC, u.username
            """)
    List<Vote> findByRestaurantIdBetweenDates(Integer restaurantId, LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT v
                  FROM Vote v
            JOIN FETCH v.restaurant r
                 WHERE v.user.id = :userId
                       AND (v.lunchDate BETWEEN :startDate AND :endDate)
              ORDER BY v.lunchDate DESC, r.name
            """)
    List<Vote> findByUserIdBetweenDates(Integer userId, LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT v
                  FROM Vote v
            JOIN FETCH v.user u
            JOIN FETCH v.restaurant r
                 WHERE v.lunchDate BETWEEN :startDate AND :endDate
              ORDER BY v.lunchDate DESC, r.name, u.username
            """)
    List<Vote> findAllBetweenDates(LocalDate startDate, LocalDate endDate);
}
