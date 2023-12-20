import com.bth.reciperadar.data.dtos.InventoryDto
import com.bth.reciperadar.data.repositories.InventoryRepository
import com.bth.reciperadar.domain.models.Ingredient
import com.bth.reciperadar.domain.models.Inventory
import com.bth.reciperadar.domain.controllers.AuthController
import com.bth.reciperadar.domain.controllers.InventoryController
import com.bth.reciperadar.domain.models.toDomain
import com.bth.reciperadar.domain.models.toDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.spy

class InventoryControllerUnitTests {
    private lateinit var authController: AuthController
    private lateinit var inventoryRepository: InventoryRepository
    private lateinit var inventoryController: InventoryController

    @Before
    fun arrange() {
        authController = mock(AuthController::class.java)
        inventoryRepository = mock(InventoryRepository::class.java)
        inventoryController = InventoryController(authController, inventoryRepository)
    }

    @Test
    fun `getInventory should return null when userId is null`() = runTest {
        // Arrange
        `when`(authController.getCurrentUserId()).thenReturn(null)

        // Act
        val result = inventoryController.getInventory()

        // Assert
        assertNull(result)
    }

    @Test
    fun `getInventory should return inventory when userId is not null`() = runTest {
        // Arrange
        val userId = "user123"
        `when`(authController.getCurrentUserId()).thenReturn(userId)
        val inventoryDto = InventoryDto(id = "1", userId = userId, ingredients = emptyList())
        `when`(inventoryRepository.getInventoryByUserId(userId)).thenReturn(inventoryDto)

        // Act
        val result = inventoryController.getInventory()

        // Assert
        assertEquals(inventoryDto.toDomain(), result)
    }

    @Test
    fun `createOrUpdateInventory should return false when userId is null`() = runTest {
        // Arrange
        val inventory = Inventory(id = "1", userId = "", ingredients = emptyList())
        `when`(authController.getCurrentUserId()).thenReturn(null)

        // Act
        val result = inventoryController.createOrUpdateInventory(inventory)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `createOrUpdateInventory should return true when userId is not null`() = runTest {
        // Arrange
        val userId = "user123"
        val inventory = Inventory(id = "1", userId = userId, ingredients = emptyList())
        `when`(authController.getCurrentUserId()).thenReturn(userId)
        `when`(inventoryRepository.createOrUpdateInventory(inventory.toDto())).thenReturn(true)

        // Act
        val result = inventoryController.createOrUpdateInventory(inventory)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `addIngredientListToInventory should return false when getInventory is null`() = runTest {
        // Arrange
        val ingredients = listOf(Ingredient(id = "101", name = "Carrot"))
        val inventoryControllerSpy = spy(inventoryController) // Create a spy

        // Mock the behavior of getInventory() in the spy
        doReturn(null).`when`(inventoryControllerSpy).getInventory()

        // Act
        val result = inventoryControllerSpy.addIngredientListToInventory(ingredients)

        // Assert
        assertFalse(result)
    }


    @Test
    fun `addIngredientListToInventory should return true when getInventory is not null`() = runTest {
        // Arrange
        val ingredients = listOf(Ingredient(id = "101", name = "Carrot"))
        val existingInventory = Inventory(id = "1", userId = "user123", ingredients = emptyList())
        val inventoryControllerSpy = spy(inventoryController) // Create a spy

        // Mock the behavior of getInventory() in the spy
        doReturn(existingInventory).`when`(inventoryControllerSpy).getInventory()
        doReturn(true).`when`(inventoryControllerSpy).createOrUpdateInventory(existingInventory)

        // Act
        val result = inventoryControllerSpy.addIngredientListToInventory(ingredients)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `removeIngredientListFromInventory should return false when getInventory is null`() = runTest {
        // Arrange
        val ingredients = listOf(Ingredient(id = "101", name = "Carrot"))
        val inventoryControllerSpy = spy(inventoryController) // Create a spy

        // Mock the behavior of getInventory() in the spy
        doReturn(null).`when`(inventoryControllerSpy).getInventory()

        // Act
        val result = inventoryControllerSpy.removeIngredientListFromInventory(ingredients)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `removeIngredientListFromInventory should return true when getInventory is not null`() = runTest {
        // Arrange
        val ingredients = listOf(Ingredient(id = "101", name = "Carrot"))
        val existingInventory = Inventory(id = "1", userId = "user123", ingredients = ingredients)
        val inventoryControllerSpy = spy(inventoryController) // Create a spy

        // Mock the behavior of getInventory() in the spy
        doReturn(existingInventory).`when`(inventoryControllerSpy).getInventory()
        doReturn(true).`when`(inventoryControllerSpy).createOrUpdateInventory(existingInventory)

        // Act
        val result = inventoryControllerSpy.removeIngredientListFromInventory(ingredients)

        // Assert
        assertEquals(true, result)
    }
}
