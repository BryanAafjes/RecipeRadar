package com.bth.reciperadar.data.repositories

import com.bth.reciperadar.data.dtos.RecipeDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RecipeRepository(private val db: FirebaseFirestore) {
    private val recipesCollection = db.collection("recipes")
    private val ingredientCollection = db.collection("ingredients")
    private val ingredientTypeCollection = db.collection("ingredient_types")
    private val ingredientRepository = IngredientRepository(db)
    private val reviewRepository = ReviewRepository(db)
    private val dietaryInfoRepository = DietaryInfoRepository(db)
    private val cuisineRepository = CuisineRepository(db)

    suspend fun getRecipeById(recipeId: String, includeIngredients: Boolean, includeReferences: Boolean): RecipeDto? {
        return try {
            val documentSnapshot = recipesCollection.document(recipeId).get().await()
            val recipe = documentSnapshot.toObject(RecipeDto::class.java)

            if (recipe != null) {
                recipe.id = documentSnapshot.id
                recipe.prepTime = documentSnapshot.get("prep_time")?.toString()
                recipe.picturePath = documentSnapshot.get("picture_path")?.toString()
                recipe.userId = documentSnapshot.get("user_id")?.toString()

                val servingAmount = documentSnapshot.get("serving_amount") as Long
                recipe.servingAmount = servingAmount.toInt()

                if (includeIngredients) {
                    recipe.ingredients = ingredientRepository.getIngredientsForRecipe(documentSnapshot)
                }
                if (includeReferences) {
                    recipe.reviews = reviewRepository.getReviewsForRecipe(recipe.id)
                    recipe.dietaryInfo =
                        dietaryInfoRepository.getDietaryInfoForReferences(documentSnapshot)
                    recipe.cuisines = cuisineRepository.getDietaryInfoForRecipe(documentSnapshot)
                }
            }

            return recipe
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            null
        }
    }


    private suspend fun getRecipes(includeReferences: Boolean): List<RecipeDto> {
        return try {
            val querySnapshot = recipesCollection.get().await()
            return getRecipesFromQueryDocuments(querySnapshot.documents, includeReferences, includeReferences)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun getRecipesFromQueryDocuments(documents: List<DocumentSnapshot>, includeIngredients: Boolean, includeReferences: Boolean): List<RecipeDto> {
        val recipesList = ArrayList<RecipeDto>()

        for (document in documents) {
            val recipe = document.toObject(RecipeDto::class.java)

            if (recipe != null) {
                recipe.id = document.id
                recipe.prepTime = document.get("prep_time")?.toString()
                recipe.picturePath = document.get("picture_path")?.toString()
                recipe.userId = document.get("user_id")?.toString()

                val servingAmount = document.get("serving_amount") as Long
                recipe.servingAmount = servingAmount.toInt()

                if (includeIngredients) {
                    recipe.ingredients = ingredientRepository.getIngredientsForRecipe(document)
                }
                if (includeReferences) {
                    recipe.reviews = reviewRepository.getReviewsForRecipe(recipe.id)
                    recipe.dietaryInfo = dietaryInfoRepository.getDietaryInfoForReferences(document)
                    recipe.cuisines = cuisineRepository.getDietaryInfoForRecipe(document)
                }

                recipe.let {
                    recipesList.add(it)
                }
            }
        }
        return recipesList
    }

    suspend fun getRecipesWithoutReferences(): List<RecipeDto> {
        return getRecipes(includeReferences = false)
    }

    suspend fun getRecipesWithReferences(): List<RecipeDto> {
        return getRecipes(includeReferences = true)
    }

    suspend fun searchRecipesByTitle(lowercaseSearchWords: List<String>?, includeIngredients: Boolean): List<RecipeDto> {
        return try {
            val querySnapshot = lowercaseSearchWords?.let {
                recipesCollection.whereArrayContainsAny("search_title", it).get().await()
            } ?: recipesCollection.get().await()

            return getRecipesFromQueryDocuments(querySnapshot.documents, includeIngredients, true)
        } catch (e: Exception) {
            // Handle exceptions, such as network issues or Firestore errors
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun seedData() {
        seedIngredientTypes()
        seedIngredients()
        seedCuisines()
        seedDietaryInfo()
    }

     private suspend fun seedIngredients() {
        val ingredients = listOf(
            mapOf("name" to "Chicken breasts", "description" to "", "type" to getIngredientTypeRef("Meat")),
            mapOf("name" to "Lemon juice", "description" to "", "type" to getIngredientTypeRef("Fruits")),
            mapOf("name" to "Olive oil", "description" to "", "type" to getIngredientTypeRef("Oils")),
            mapOf("name" to "Fresh rosemary", "description" to "Chopped", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Fresh thyme", "description" to "Chopped", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Salt", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Pepper", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Quinoa", "description" to "", "type" to getIngredientTypeRef("Grains")),
            mapOf("name" to "Cherry tomatoes", "description" to "Halved", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Bell peppers", "description" to "Sliced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Red onion", "description" to "Thinly sliced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Olive oil", "description" to "", "type" to getIngredientTypeRef("Oils")),
            mapOf("name" to "Balsamic vinegar", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Fresh basil", "description" to "Chopped", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Lentils", "description" to "", "type" to getIngredientTypeRef("Grains")),
            mapOf("name" to "Carrots", "description" to "Diced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Celery", "description" to "Diced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Onion", "description" to "Diced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Garlic", "description" to "Minced", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Cumin", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Coriander", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Vegetable broth", "description" to "", "type" to getIngredientTypeRef("Other")),
            mapOf("name" to "Tomato paste", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Salmon fillets", "description" to "", "type" to getIngredientTypeRef("Seafood")),
            mapOf("name" to "Fresh dill", "description" to "Chopped", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Greek yogurt", "description" to "", "type" to getIngredientTypeRef("Dairy")),
            mapOf("name" to "Lemon juice", "description" to "", "type" to getIngredientTypeRef("Fruits")),
            mapOf("name" to "Garlic", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Salt", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Pepper", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Chickpeas", "description" to "Drained and rinsed", "type" to getIngredientTypeRef("Canned Goods")),
            mapOf("name" to "Spinach", "description" to "", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Coconut milk", "description" to "", "type" to getIngredientTypeRef("Canned Goods")),
            mapOf("name" to "Curry powder", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Cumin", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Turmeric", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Onion", "description" to "Diced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Garlic", "description" to "Minced", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Ginger", "description" to "Grated", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Sweet potatoes", "description" to "Peeled and diced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Black beans", "description" to "Drained and rinsed", "type" to getIngredientTypeRef("Canned Goods")),
            mapOf("name" to "Corn tortillas", "description" to "", "type" to getIngredientTypeRef("Grains")),
            mapOf("name" to "Avocado", "description" to "Sliced", "type" to getIngredientTypeRef("Fruits")),
            mapOf("name" to "Lime", "description" to "Juiced", "type" to getIngredientTypeRef("Fruits")),
            mapOf("name" to "Cilantro", "description" to "Chopped", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Cumin", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Paprika", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Firm tofu", "description" to "Pressed and cubed", "type" to getIngredientTypeRef("Meat Alternatives")),
            mapOf("name" to "Broccoli", "description" to "Florets", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Bell peppers", "description" to "Sliced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Snow peas", "description" to "", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Teriyaki sauce", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Sesame oil", "description" to "", "type" to getIngredientTypeRef("Oils")),
            mapOf("name" to "Garlic", "description" to "Minced", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Ginger", "description" to "Grated", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Eggplant", "description" to "Sliced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Marinara sauce", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Mozzarella cheese", "description" to "Shredded", "type" to getIngredientTypeRef("Dairy")),
            mapOf("name" to "Parmesan cheese", "description" to "Grated", "type" to getIngredientTypeRef("Dairy")),
            mapOf("name" to "Bread crumbs", "description" to "", "type" to getIngredientTypeRef("Baking Ingredients")),
            mapOf("name" to "Italian seasoning", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Garlic", "description" to "Minced", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Shrimp", "description" to "Peeled and deveined", "type" to getIngredientTypeRef("Seafood")),
            mapOf("name" to "Bell peppers", "description" to "Cut into chunks", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Cherry tomatoes", "description" to "", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Zucchini", "description" to "Sliced", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Olive oil", "description" to "", "type" to getIngredientTypeRef("Oils")),
            mapOf("name" to "Garlic", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Lemon juice", "description" to "", "type" to getIngredientTypeRef("Fruits")),
            mapOf("name" to "Paprika", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Pizza dough", "description" to "", "type" to getIngredientTypeRef("Grains")),
            mapOf("name" to "Tomato sauce", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Mozzarella cheese", "description" to "", "type" to getIngredientTypeRef("Dairy")),
            mapOf("name" to "Cherry tomatoes", "description" to "", "type" to getIngredientTypeRef("Vegetables")),
            mapOf("name" to "Fresh basil", "description" to "", "type" to getIngredientTypeRef("Herbs and Spices")),
            mapOf("name" to "Olive oil", "description" to "", "type" to getIngredientTypeRef("Oils")),
            mapOf("name" to "Salt", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces")),
            mapOf("name" to "Pepper", "description" to "", "type" to getIngredientTypeRef("Condiments and Sauces"))
        )

         // Add the search_name field to each ingredient
         val ingredientsWithSearchName = ingredients.map {
             it + ("search_name" to listOf(it["name"].toString().lowercase()))
         }

         // Remove duplicates based on ingredient names
         val uniqueIngredients = ingredientsWithSearchName.distinctBy { it["name"] }

        seedCollection("ingredients", uniqueIngredients)
    }

    private fun seedCuisines() {
        val cuisines = listOf(
            mapOf("name" to "Mediterranean", "description" to "Cuisine from the Mediterranean region"),
            mapOf("name" to "International", "description" to "Cuisine with diverse international flavors"),
            mapOf("name" to "Middle Eastern", "description" to "Cuisine from the Middle Eastern region"),
            mapOf("name" to "Scandinavian", "description" to "Cuisine from Scandinavia"),
            mapOf("name" to "Indian", "description" to "Cuisine from India"),
            mapOf("name" to "Mexican", "description" to "Cuisine from Mexico"),
            mapOf("name" to "Asian", "description" to "Cuisine from the Asian continent"),
            mapOf("name" to "Italian", "description" to "Cuisine from Italy"),
            mapOf("name" to "Grill", "description" to "Cuisine where the food can be cooked on a grill"),
        )

        seedCollection("cuisines", cuisines)
    }

    private fun seedIngredientTypes() {
        val ingredientTypes = listOf(
            mapOf("name" to "Meat"),
            mapOf("name" to "Fruits"),
            mapOf("name" to "Oils"),
            mapOf("name" to "Herbs and Spices"),
            mapOf("name" to "Condiments and Sauces"),
            mapOf("name" to "Grains"),
            mapOf("name" to "Vegetables"),
            mapOf("name" to "Other"),
            mapOf("name" to "Seafood"),
            mapOf("name" to "Dairy"),
            mapOf("name" to "Canned Goods"),
            mapOf("name" to "Meat Alternatives"),
            mapOf("name" to "Baking Ingredients")
        )

        seedCollection("ingredient_types", ingredientTypes)
    }


     private fun seedDietaryInfo() {
        val dietaryInfo = listOf(
            mapOf("name" to "Gluten-free"),
            mapOf("name" to "Dairy-free"),
            mapOf("name" to "Vegan"),
            mapOf("name" to "Vegetarian"),
            mapOf("name" to "Nut-free"),
            mapOf("name" to "Pescatarian")
        )

        seedCollection("dietary_info", dietaryInfo)
    }

    private fun seedCollection(collectionName: String, data: List<Map<String, Any>>) {
        data.forEachIndexed { index, documentData ->
            db.collection(collectionName).add(documentData)
                .addOnSuccessListener { documentReference ->
                    println("$collectionName document $index added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Error adding $collectionName document $index: $e")
                }
        }
    }

    private suspend fun getIngredientTypeRef(ingredientTypeName: String): DocumentReference {
        val querySnapshot = db.collection("ingredient_types").whereEqualTo("name", ingredientTypeName).limit(1).get().await()
        val document = querySnapshot.documents.firstOrNull()
        return document?.reference ?: throw NoSuchElementException("Ingredient Type not found: $ingredientTypeName")
    }

    private suspend fun getCuisineRef(cuisineName: String): DocumentReference {
        val querySnapshot = db.collection("cuisines").whereEqualTo("name", cuisineName).limit(1).get().await()
        val document = querySnapshot.documents.firstOrNull()
        return document?.reference ?: throw NoSuchElementException("Cuisine not found: $cuisineName")
    }

    private suspend fun getIngredientRef(ingredientName: String): DocumentReference {
        val querySnapshot = db.collection("ingredients").whereEqualTo("name", ingredientName).limit(1).get().await()
        val document = querySnapshot.documents.firstOrNull()
        return document?.reference ?: throw NoSuchElementException("Ingredient not found: $ingredientName")
    }

    private suspend fun getDietaryInfoRef(dietaryInfoName: String): DocumentReference {
        val querySnapshot = db.collection("dietary_info").whereEqualTo("name", dietaryInfoName).limit(1).get().await()
        val document = querySnapshot.documents.firstOrNull()
        return document?.reference ?: throw NoSuchElementException("Dietary Info not found: $dietaryInfoName")
    }

    suspend fun seedRecipes() {
        seedData()

        // Recipe 1: Grilled Lemon Herb Chicken
        val recipe1 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Mediterranean")),
            "description" to "Grilled Lemon Herb Chicken",
            "dietary_info_references" to listOf(getDietaryInfoRef("Gluten-free"), getDietaryInfoRef("Dairy-free")),
            "ingredients" to listOf(
                mapOf("amount" to "4", "ingredient" to getIngredientRef("Chicken breasts"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Lemon juice"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Olive oil"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Fresh rosemary"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Fresh thyme"), "picture_path" to null),
                mapOf("amount" to "To taste", "ingredient" to getIngredientRef("Salt"), "picture_path" to null),
                mapOf("amount" to "To taste", "ingredient" to getIngredientRef("Pepper"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Chicken breasts"),
                getIngredientRef("Lemon juice"),
                getIngredientRef("Olive oil"),
                getIngredientRef("Fresh rosemary"),
                getIngredientRef("Fresh thyme"),
                getIngredientRef("Salt"),
                getIngredientRef("Pepper")
            ),
            "search_title" to listOf("grilled lemon herb chicken"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Mix lemon juice, olive oil, chopped rosemary, thyme, salt, and pepper in a bowl.", "number" to 1, "picture_path" to null, "title" to "Prepare Marinade"),
                mapOf("description" to "Marinate chicken breasts in the mixture for 30 minutes.", "number" to 2, "picture_path" to null, "title" to "Marinate Chicken"),
                mapOf("description" to "Grill until fully cooked, basting with the marinade.", "number" to 3, "picture_path" to null, "title" to "Grill Chicken")
            ),
            "title" to "Grilled Lemon Herb Chicken",
            "user_id" to null
        )

        addRecipeToFirestore(recipe1, 1)

        // Recipe 2: Quinoa Salad with Roasted Vegetables
        val recipe2 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("International")),
            "description" to "Quinoa Salad with Roasted Vegetables",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegan"), getDietaryInfoRef("Gluten-free")),
            "ingredients" to listOf(
                mapOf("amount" to "1 cup", "ingredient" to getIngredientRef("Quinoa"), "picture_path" to null),
                mapOf("amount" to "1 cup", "ingredient" to getIngredientRef("Cherry tomatoes"), "picture_path" to null),
                mapOf("amount" to "2", "ingredient" to getIngredientRef("Bell peppers"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Red onion"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Olive oil"), "picture_path" to null),
                mapOf("amount" to "2 tablespoons", "ingredient" to getIngredientRef("Balsamic vinegar"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Fresh basil"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Quinoa"),
                getIngredientRef("Cherry tomatoes"),
                getIngredientRef("Bell peppers"),
                getIngredientRef("Red onion"),
                getIngredientRef("Olive oil"),
                getIngredientRef("Balsamic vinegar"),
                getIngredientRef("Fresh basil")
            ),
            "search_title" to listOf("quinoa salad with roasted vegetables"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Cook quinoa according to package instructions.", "number" to 1, "picture_path" to null, "title" to "Cook Quinoa"),
                mapOf("description" to "Roast vegetables with olive oil until tender.", "number" to 2, "picture_path" to null, "title" to "Roast Vegetables"),
                mapOf("description" to "Mix quinoa, roasted vegetables, chopped basil, and dress with balsamic vinaigrette.", "number" to 3, "picture_path" to null, "title" to "Assemble Salad")
            ),
            "title" to "Quinoa Salad with Roasted Vegetables",
            "user_id" to null
        )

        addRecipeToFirestore(recipe2, 2)

        // Recipe 3: Lentil Soup
        val recipe3 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Middle Eastern")),
            "description" to "Lentil Soup",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegetarian"), getDietaryInfoRef("Nut-free")),
            "ingredients" to listOf(
                mapOf("amount" to "1 cup", "ingredient" to getIngredientRef("Lentils"), "picture_path" to null),
                mapOf("amount" to "2", "ingredient" to getIngredientRef("Carrots"), "picture_path" to null),
                mapOf("amount" to "2 stalks", "ingredient" to getIngredientRef("Celery"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Onion"), "picture_path" to null),
                mapOf("amount" to "2 cloves", "ingredient" to getIngredientRef("Garlic"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Cumin"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Coriander"), "picture_path" to null),
                mapOf("amount" to "4 cups", "ingredient" to getIngredientRef("Vegetable broth"), "picture_path" to null),
                mapOf("amount" to "2 tablespoons", "ingredient" to getIngredientRef("Tomato paste"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Lentils"),
                getIngredientRef("Carrots"),
                getIngredientRef("Celery"),
                getIngredientRef("Onion"),
                getIngredientRef("Garlic"),
                getIngredientRef("Cumin"),
                getIngredientRef("Coriander"),
                getIngredientRef("Vegetable broth"),
                getIngredientRef("Tomato paste")
            ),
            "search_title" to listOf("lentil soup"),
            "serving_amount" to 6,
            "steps" to listOf(
                mapOf("description" to "Sauté onion, garlic, carrots, and celery.", "number" to 1, "picture_path" to null, "title" to "Sauté Vegetables"),
                mapOf("description" to "Add lentils, cumin, coriander, vegetable broth, and tomato paste. Simmer until lentils are tender.", "number" to 2, "picture_path" to null, "title" to "Cook Lentil Soup")
            ),
            "title" to "Lentil Soup",
            "user_id" to null
        )

        addRecipeToFirestore(recipe3, 3)

        // Recipe 4: Salmon with Dill Sauce
        val recipe4 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Scandinavian")),
            "description" to "Salmon with Dill Sauce",
            "dietary_info_references" to listOf(getDietaryInfoRef("Pescatarian")),
            "ingredients" to listOf(
                mapOf("amount" to "4", "ingredient" to getIngredientRef("Salmon fillets"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Fresh dill"), "picture_path" to null),
                mapOf("amount" to "1/2 cup", "ingredient" to getIngredientRef("Greek yogurt"), "picture_path" to null),
                mapOf("amount" to "2 tablespoons", "ingredient" to getIngredientRef("Lemon juice"), "picture_path" to null),
                mapOf("amount" to "2 cloves", "ingredient" to getIngredientRef("Garlic"), "picture_path" to null),
                mapOf("amount" to "To taste", "ingredient" to getIngredientRef("Salt"), "picture_path" to null),
                mapOf("amount" to "To taste", "ingredient" to getIngredientRef("Pepper"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Salmon fillets"),
                getIngredientRef("Fresh dill"),
                getIngredientRef("Greek yogurt"),
                getIngredientRef("Lemon juice"),
                getIngredientRef("Garlic"),
                getIngredientRef("Salt"),
                getIngredientRef("Pepper")
            ),
            "search_title" to listOf("salmon with dill sauce"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Mix chopped dill, yogurt, lemon juice, minced garlic, salt, and pepper.", "number" to 1, "picture_path" to null, "title" to "Prepare Dill Sauce"),
                mapOf("description" to "Coat salmon with the sauce and bake until fish flakes easily.", "number" to 2, "picture_path" to null, "title" to "Bake Salmon")
            ),
            "title" to "Salmon with Dill Sauce",
            "user_id" to null
        )

        addRecipeToFirestore(recipe4, 4)

        // Recipe 5: Chickpea and Spinach Curry
        val recipe5 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Indian")),
            "description" to "Chickpea and Spinach Curry",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegan"), getDietaryInfoRef("Gluten-free")),
            "ingredients" to listOf(
                mapOf("amount" to "2 cans", "ingredient" to getIngredientRef("Chickpeas"), "picture_path" to null),
                mapOf("amount" to "4 cups", "ingredient" to getIngredientRef("Spinach"), "picture_path" to null),
                mapOf("amount" to "1 can", "ingredient" to getIngredientRef("Coconut milk"), "picture_path" to null),
                mapOf("amount" to "2 tablespoons", "ingredient" to getIngredientRef("Curry powder"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Cumin"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Turmeric"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Onion"), "picture_path" to null),
                mapOf("amount" to "3 cloves", "ingredient" to getIngredientRef("Garlic"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Ginger"), "picture_path" to null)
            ),
            "prep_time" to "2 hours and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Chickpeas"),
                getIngredientRef("Spinach"),
                getIngredientRef("Coconut milk"),
                getIngredientRef("Curry powder"),
                getIngredientRef("Cumin"),
                getIngredientRef("Turmeric"),
                getIngredientRef("Onion"),
                getIngredientRef("Garlic"),
                getIngredientRef("Ginger")
            ),
            "search_title" to listOf("chickpea and spinach curry"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Sauté onion, garlic, and ginger.", "number" to 1, "picture_path" to null, "title" to "Sauté Onion and Garlic"),
                mapOf("description" to "Add chickpeas, spinach, coconut milk, and spices. Simmer until spinach wilts.", "number" to 2, "picture_path" to null, "title" to "Cook Curry")
            ),
            "title" to "Chickpea and Spinach Curry",
            "user_id" to null
        )

        addRecipeToFirestore(recipe5, 5)

        // Recipe 6: Roasted Sweet Potato Tacos
        val recipe6 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Mexican")),
            "description" to "Roasted Sweet Potato Tacos",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegetarian")),
            "ingredients" to listOf(
                mapOf("amount" to "2", "ingredient" to getIngredientRef("Sweet potatoes"), "picture_path" to null),
                mapOf("amount" to "1 can", "ingredient" to getIngredientRef("Black beans"), "picture_path" to null),
                mapOf("amount" to "8", "ingredient" to getIngredientRef("Corn tortillas"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Avocado"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Lime"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Cilantro"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Cumin"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Paprika"), "picture_path" to null)
            ),
            "prep_time" to "1 hour",
            "search_ingredients" to listOf(
                getIngredientRef("Sweet potatoes"),
                getIngredientRef("Black beans"),
                getIngredientRef("Corn tortillas"),
                getIngredientRef("Avocado"),
                getIngredientRef("Lime"),
                getIngredientRef("Cilantro"),
                getIngredientRef("Cumin"),
                getIngredientRef("Paprika")
            ),
            "search_title" to listOf("roasted sweet potato tacos"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Roast sweet potatoes with cumin and paprika.", "number" to 1, "picture_path" to null, "title" to "Roast Sweet Potatoes"),
                mapOf("description" to "Warm tortillas, fill with sweet potatoes, black beans, avocado, and cilantro.", "number" to 2, "picture_path" to null, "title" to "Assemble Tacos")
            ),
            "title" to "Roasted Sweet Potato Tacos",
            "user_id" to null
        )

        addRecipeToFirestore(recipe6, 6)

        // Recipe 7: Tofu Stir-Fry
        val recipe7 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Asian")),
            "description" to "Tofu Stir-Fry",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegetarian")),
            "ingredients" to listOf(
                mapOf("amount" to "1 block", "ingredient" to getIngredientRef("Firm tofu"), "picture_path" to null),
                mapOf("amount" to "2 cups", "ingredient" to getIngredientRef("Broccoli"), "picture_path" to null),
                mapOf("amount" to "2", "ingredient" to getIngredientRef("Bell peppers"), "picture_path" to null),
                mapOf("amount" to "1 cup", "ingredient" to getIngredientRef("Snow peas"), "picture_path" to null),
                mapOf("amount" to "1/4 cup", "ingredient" to getIngredientRef("Teriyaki sauce"), "picture_path" to null),
                mapOf("amount" to "2 tablespoons", "ingredient" to getIngredientRef("Sesame oil"), "picture_path" to null),
                mapOf("amount" to "2 cloves", "ingredient" to getIngredientRef("Garlic"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Ginger"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Firm tofu"),
                getIngredientRef("Broccoli"),
                getIngredientRef("Bell peppers"),
                getIngredientRef("Snow peas"),
                getIngredientRef("Teriyaki sauce"),
                getIngredientRef("Sesame oil"),
                getIngredientRef("Garlic"),
                getIngredientRef("Ginger")
            ),
            "search_title" to listOf("tofu stir fry"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Press tofu to remove excess water and cut into cubes.", "number" to 1, "picture_path" to null, "title" to "Prepare Tofu"),
                mapOf("description" to "Stir-fry tofu and vegetables in sesame oil. Add teriyaki sauce.", "number" to 2, "picture_path" to null, "title" to "Cook Stir-Fry")
            ),
            "title" to "Tofu Stir-Fry",
            "user_id" to null
        )

        addRecipeToFirestore(recipe7, 7)

        // Recipe 8: Eggplant Parmesan
        val recipe8 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Italian")),
            "description" to "Eggplant Parmesan",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegetarian")),
            "ingredients" to listOf(
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Eggplant"), "picture_path" to null),
                mapOf("amount" to "2 cups", "ingredient" to getIngredientRef("Marinara sauce"), "picture_path" to null),
                mapOf("amount" to "1 cup", "ingredient" to getIngredientRef("Mozzarella cheese"), "picture_path" to null),
                mapOf("amount" to "1/2 cup", "ingredient" to getIngredientRef("Parmesan cheese"), "picture_path" to null),
                mapOf("amount" to "1/2 cup", "ingredient" to getIngredientRef("Bread crumbs"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Italian seasoning"), "picture_path" to null),
                mapOf("amount" to "2 cloves", "ingredient" to getIngredientRef("Garlic"), "picture_path" to null)
            ),
            "prep_time" to "2 hours",
            "search_ingredients" to listOf(
                getIngredientRef("Eggplant"),
                getIngredientRef("Marinara sauce"),
                getIngredientRef("Mozzarella cheese"),
                getIngredientRef("Parmesan cheese"),
                getIngredientRef("Bread crumbs"),
                getIngredientRef("Italian seasoning"),
                getIngredientRef("Garlic")
            ),
            "search_title" to listOf("eggplant parmesan"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Slice eggplant and coat with breadcrumbs. Bake until golden.", "number" to 1, "picture_path" to null, "title" to "Bake Eggplant"),
                mapOf("description" to "Layer eggplant with marinara sauce and cheeses. Bake until cheese melts.", "number" to 2, "picture_path" to null, "title" to "Assemble and Bake")
            ),
            "title" to "Eggplant Parmesan",
            "user_id" to null
        )

        addRecipeToFirestore(recipe8, 8)

        // Recipe 9: Shrimp and Vegetable Skewers
        val recipe9 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Grill")),
            "description" to "Shrimp and Vegetable Skewers",
            "dietary_info_references" to listOf(getDietaryInfoRef("Pescatarian")),
            "ingredients" to listOf(
                mapOf("amount" to "1 pound", "ingredient" to getIngredientRef("Shrimp"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Bell peppers"), "picture_path" to null),
                mapOf("amount" to "1 pint", "ingredient" to getIngredientRef("Cherry tomatoes"), "picture_path" to null),
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Zucchini"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Olive oil"), "picture_path" to null),
                mapOf("amount" to "1 clove", "ingredient" to getIngredientRef("Garlic"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Lemon juice"), "picture_path" to null),
                mapOf("amount" to "1 teaspoon", "ingredient" to getIngredientRef("Paprika"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Shrimp"),
                getIngredientRef("Bell peppers"),
                getIngredientRef("Cherry tomatoes"),
                getIngredientRef("Zucchini"),
                getIngredientRef("Olive oil"),
                getIngredientRef("Garlic"),
                getIngredientRef("Lemon juice"),
                getIngredientRef("Paprika")
            ),
            "search_title" to listOf("shrimp and vegetable skewers"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Marinate shrimp in olive oil, garlic, lemon juice, and paprika.", "number" to 1, "picture_path" to null, "title" to "Marinate Shrimp"),
                mapOf("description" to "Thread shrimp and vegetables onto skewers. Grill until shrimp are cooked.", "number" to 2, "picture_path" to null, "title" to "Grill Skewers")
            ),
            "title" to "Shrimp and Vegetable Skewers",
            "user_id" to null
        )

        addRecipeToFirestore(recipe9, 9)

        // Recipe 10: Margherita Pizza
        val recipe10 = mapOf(
            "cuisine_references" to listOf(getCuisineRef("Italian")),
            "description" to "Margherita Pizza",
            "dietary_info_references" to listOf(getDietaryInfoRef("Vegetarian")),
            "ingredients" to listOf(
                mapOf("amount" to "1", "ingredient" to getIngredientRef("Pizza dough"), "picture_path" to null),
                mapOf("amount" to "1/2 cup", "ingredient" to getIngredientRef("Tomato sauce"), "picture_path" to null),
                mapOf("amount" to "1 1/2 cups", "ingredient" to getIngredientRef("Mozzarella cheese"), "picture_path" to null),
                mapOf("amount" to "1 cup", "ingredient" to getIngredientRef("Cherry tomatoes"), "picture_path" to null),
                mapOf("amount" to "1/2 cup", "ingredient" to getIngredientRef("Fresh basil"), "picture_path" to null),
                mapOf("amount" to "1 tablespoon", "ingredient" to getIngredientRef("Olive oil"), "picture_path" to null),
                mapOf("amount" to "To taste", "ingredient" to getIngredientRef("Salt"), "picture_path" to null),
                mapOf("amount" to "To taste", "ingredient" to getIngredientRef("Pepper"), "picture_path" to null)
            ),
            "prep_time" to "1 hour and 30 minutes",
            "search_ingredients" to listOf(
                getIngredientRef("Pizza dough"),
                getIngredientRef("Tomato sauce"),
                getIngredientRef("Mozzarella cheese"),
                getIngredientRef("Cherry tomatoes"),
                getIngredientRef("Fresh basil"),
                getIngredientRef("Olive oil"),
                getIngredientRef("Salt"),
                getIngredientRef("Pepper")
            ),
            "search_title" to listOf("margherita pizza"),
            "serving_amount" to 4,
            "steps" to listOf(
                mapOf("description" to "Roll out pizza dough and spread with tomato sauce. Top with cheese and tomatoes.", "number" to 1, "picture_path" to null, "title" to "Prepare Pizza"),
                mapOf("description" to "Bake until crust is golden and cheese is bubbly. Garnish with fresh basil.", "number" to 2, "picture_path" to null, "title" to "Bake Pizza")
            ),
            "title" to "Margherita Pizza",
            "user_id" to null
        )

        addRecipeToFirestore(recipe10, 10)
    }


    private fun addRecipeToFirestore(recipeData: Map<String, Any?>, index: Int) {
        db.collection("recipes").add(recipeData)
            .addOnSuccessListener { documentReference ->
                println("Recipe $index added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding recipe $index: $e")
            }
    }
}