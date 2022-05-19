package com.github.nikolaybabich.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "menus",
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "actual_date", "restaurant_id" }, name = "uk__menus__actual_date__restaurant_id")
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "actual_date", nullable = false)
    @NotNull
    @FutureOrPresent
    private LocalDate actualDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk__menus__restaurants"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(
            name = "menu_dishes",
            joinColumns = @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk__menu_dishes__menus")),
            inverseJoinColumns = @JoinColumn(name = "dish_id", foreignKey = @ForeignKey(name = "fk__menu_dishes__dishes"))
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Set<Dish> dishes = new HashSet<>();

    public Menu(Menu menu) {
        this(menu.id, menu.actualDate, menu.restaurant, menu.dishes);
    }

    public Menu(Integer id, LocalDate actualDate) {
        super(id);
        this.actualDate = actualDate;
    }

    public Menu(Integer id, LocalDate actualDate, Restaurant restaurant, Dish... dishes) {
        this(id, actualDate, restaurant, Set.of(dishes));
    }

    public Menu(Integer id, LocalDate actualDate, Restaurant restaurant, Set<Dish> dishes) {
        this(id, actualDate);
        this.restaurant = restaurant;
        this.dishes.addAll(dishes);
    }
}
