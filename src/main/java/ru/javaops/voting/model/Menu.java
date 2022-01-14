package ru.javaops.voting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lunch_date", "restaurant_id"}, name = "ux_menu_lunch_date_restaurant_id")
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant", "dishes"})
public class Menu extends BaseEntity {

    @Column(name = "lunch_date", nullable = false)
    @NotNull
    private LocalDate lunchDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "fk_menu_restaurant"), nullable = false)
    private Restaurant restaurant;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "menu_dish",
            joinColumns = @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_dish_menu")),
            inverseJoinColumns = @JoinColumn(name = "dish_id", foreignKey = @ForeignKey(name = "fk_menu_dish"))
    )
    @Valid
    private Set<Dish> dishes = new HashSet<>();

    public Menu(Integer id, LocalDate lunchDate, Restaurant restaurant, Dish... dishes) {
        super(id);
        this.lunchDate = lunchDate;
        this.restaurant = restaurant;
        Collections.addAll(this.dishes, dishes);
    }
}
