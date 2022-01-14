package ru.javaops.voting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}, name = "ux_restaurant_name_address"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String address;

    public Restaurant(Integer id, String name, String address) {
        super(id);
        this.name = name;
        this.address = address;
    }
}
