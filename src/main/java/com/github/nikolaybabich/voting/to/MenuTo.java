package com.github.nikolaybabich.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTo extends BaseTo {

    @NotNull
    @FutureOrPresent
    LocalDate actualDate;

    @NotNull
    @JsonProperty("dishes")
    List<Integer> dishIds;

    public MenuTo(Integer id, LocalDate actualDate, Integer... dishes) {
        super(id);
        this.actualDate = actualDate;
        this.dishIds = List.of(dishes);
    }
}
