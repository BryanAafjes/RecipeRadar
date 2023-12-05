package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRepository(db: FirebaseFirestore) {
    private val reviewCollection = db.collection("reviews")
    val recipeCollection = db.collection("recipes")

    suspend fun getReviewsForRecipe(recipeId: String): List<ReviewDto> {
        val reviewsList = ArrayList<ReviewDto>()

        return try {
            val querySnapshot = reviewCollection
                .whereEqualTo("recipe", recipeCollection.document(recipeId))
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val review = document.toObject(ReviewDto::class.java)
                review?.id = document.id

                review?.let {
                    reviewsList.add(it)
                }
            }

            return reviewsList

        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }
}