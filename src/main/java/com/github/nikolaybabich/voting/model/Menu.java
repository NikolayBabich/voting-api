package com.github.nikolaybabich.voting.model;

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
    private LocalDate actualDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk__menus__restaurants"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
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

    public Menu(Integer id, LocalDate actualDate) {
        super(id);
        this.actualDate = actualDate;
    }
}
