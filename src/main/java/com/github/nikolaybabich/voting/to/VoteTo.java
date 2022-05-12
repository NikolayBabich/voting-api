package com.github.nikolaybabich.voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    LocalDate actualDate;

    Integer userId;

    @NotNull
    Integer restaurantId;

    public VoteTo(Integer id, LocalDate actualDate, Integer userId, Integer restaurantId) {
        super(id);
        this.actualDate = actualDate;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
