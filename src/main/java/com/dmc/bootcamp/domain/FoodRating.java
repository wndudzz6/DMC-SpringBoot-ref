package com.dmc.bootcamp.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "food_rating")
public class FoodRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float rating;
    private float starRating;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser user;

    @Builder
    public FoodRating(float rating, float starRating, AppUser user) {
        this.rating = rating;
        this.starRating = starRating;
        this.user = user;
    }
}