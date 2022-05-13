package com.github.nikolaybabich.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.nikolaybabich.voting.model.Dish;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTo extends BaseTo {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer restaurantId;

    @NotNull
    @FutureOrPresent
    LocalDate actualDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Set<Dish> dishes = new HashSet<>();

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Integer> dishIds;

    public MenuTo(Integer id, Integer restaurantId, LocalDate actualDate, Set<Dish> dishes, Integer... dishIds) {
        super(id);
        this.restaurantId = restaurantId;
        this.actualDate = actualDate;
        this.dishes.addAll(dishes);
        this.dishIds = List.of(dishIds);
    }
}
