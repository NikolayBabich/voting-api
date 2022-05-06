package com.github.nikolaybabich.voting.service;

import com.github.nikolaybabich.voting.model.Dish;
import com.github.nikolaybabich.voting.repository.DishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DishService { // TODO move all to controller

    private final DishRepository repository;

    public Dish createOrUpdate(Dish dish) {
        return repository.save(dish);
    }

    public Optional<Dish> get(int id) {
        return repository.findById(id);
    }

    public void delete(int id) {
        repository.deleteExisted(id);
    }
}
