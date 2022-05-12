package com.github.nikolaybabich.voting.service;

import com.github.nikolaybabich.voting.repository.DishRepository;
import com.github.nikolaybabich.voting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    @Transactional
    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
        dishRepository.deleteOrphans();
    }
}
