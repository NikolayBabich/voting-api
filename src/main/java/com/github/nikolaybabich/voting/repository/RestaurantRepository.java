package com.github.nikolaybabich.voting.repository;

import com.github.nikolaybabich.voting.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
