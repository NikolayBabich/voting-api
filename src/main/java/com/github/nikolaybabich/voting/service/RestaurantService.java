package com.github.nikolaybabich.voting.service;

import com.github.nikolaybabich.voting.model.Restaurant;
import com.github.nikolaybabich.voting.repository.DishRepository;
import com.github.nikolaybabich.voting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public Restaurant createOrUpdate(Restaurant restaurant) { // TODO move to controller
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAll() { // TODO move to controller
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> get(int id) { // TODO move to controller
        return restaurantRepository.findById(id);
    }

    @Transactional
    public void delete(int id) {
        restaurantRepository.deleteExisted(id);
        dishRepository.deleteOrphans();
    }
}
