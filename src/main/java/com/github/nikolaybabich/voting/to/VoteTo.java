package com.github.nikolaybabich.voting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDate actualDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
