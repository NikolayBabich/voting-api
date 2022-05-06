package com.github.nikolaybabich.voting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(
        name = "dishes",
        uniqueConstraints = @UniqueConstraint(columnNames = { "name", "price" }, name = "uk__dishes__name__price")
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public Dish(Integer id, String name, BigDecimal price) {
        super(id, name);
        this.price = price;
    }
}
