# RecipeRadar

![Recipe Radar Logo](/logo.png)

## Table of Contents

- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [License](#license)

## About

RecipeRadar is an Android application built using Kotlin, Jetpack Compose and Google Firebase. It leverages Google Firebase for real-time data storage and authentication. The app provides a platform for users to discover new and exiting recipes.

## Features

The following features are currently implemented in RecipeRadar:

1. **Ingredient-based Recipe Suggestions:**

   - Input the ingredients you have at home, and the app will suggest recipes based on your available ingredients.

2. **Detailed Recipe Information:**

   - View comprehensive recipes with detailed information, including ingredients, cooking instructions, and estimated preparation time.

3. **Explore Cuisines:**

   - Explore a diverse variety of cuisines and filter recipes by cuisine types from all around the world.

4. **User Profiles with Dietary Preferences:**

   - Create a profile with dietary preferences (e.g., vegetarian, vegan, allergies) to receive personalized recipe suggestions.

5. **Generate Shopping List:**

   - Generate a shopping list based on selected ingredients in a recipe to ensure you have all the necessary ingredients for a recipe.

6. **Ingredient Inventory:**

   - Register and keep track of your ingredients in the app to search recipes with the ingredients you have available at home.

## Getting Started

### Prerequisites

Make sure you have the following installed before setting up RecipeRadar:

- [Android Studio](https://developer.android.com/studio)
- [Kotlin](https://kotlinlang.org/)
- [Google Firebase Project](https://console.firebase.google.com/)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/BryanAafjes/RecipeRadar.git
   ```

2. Open the project in Android Studio.

3. Connect the app to your Firebase project by following the instructions in the [Firebase documentation](https://firebase.google.com/docs/android/setup) (This is mainly downloading and putting the google-services.json into the app level of folder structure).

4. Sync Gradle dependencies.

5. Run the app on an a physical Android device or the built in Android Emulator.

## Usage

1. Sign in with your existing account or create a new account within the app.
2. Search recipes on the platform and get cooking.
3. Create your own shopping list with ingredients you need.
4. Keep track of the ingredients you have with the ingredient inventory.
5. Create a public profile for reviews in the future.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
