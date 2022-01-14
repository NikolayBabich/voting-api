package ru.javaops.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "dish", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "price"}, name = "ux_dish_name_price"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"menus"})
public class Dish extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "price", nullable = false)
    @Range(min = 10, max = 100000)
    private int priceInCents;

    @ManyToMany(mappedBy = "dishes", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private final Set<Menu> menus = new HashSet<>();

    public Dish(Integer id, String name, int priceInCents) {
        super(id);
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public Dish(Dish copiedDish) {
        this(null, copiedDish.name, copiedDish.priceInCents);
        this.menus.addAll(copiedDish.menus);
    }
}
