package com.github.nikolaybabich.voting.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@DynamicUpdate
@Table(
        name = "votes",
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "user_id", "actual_date" }, name = "uk__votes__user_id__actual_date")
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "actual_date", nullable = false)
    private LocalDate actualDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk__votes__users"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk__votes__restaurants"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate actualDate) {
        super(id);
        this.actualDate = actualDate;
    }

    @Override
    public String toString() {
        return "Vote:" + id + '[' + actualDate + " User:" + user.getId() + " -> Restaurant:" + restaurant.getId() + ']';
    }
}
