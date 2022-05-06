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

@Entity
@Table(
        name = "restaurants",
        uniqueConstraints = @UniqueConstraint(columnNames = { "address", "name" }, name = "uk__restaurants__address__name")
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    private String address;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }
}
