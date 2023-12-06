package com.bth.reciperadar.domain.enums

enum class ReviewRatingEnum(val displayName: String, val numericValue: Int) {
    OneStar("One star", 1),
    TwoStars("Two stars", 2),
    ThreeStars("Three stars", 3),
    FourStars("Four stars", 4),
    FiveStars("Five stars", 5),
}