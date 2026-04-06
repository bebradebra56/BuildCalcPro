package com.buildcal.probuild.ui.screens.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.ShoppingItemEntity
import com.buildcal.probuild.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ShoppingUiState(
    val items: List<ShoppingItemEntity> = emptyList(),
    val pendingCount: Int = 0,
    val isLoading: Boolean = false
)

class ShoppingViewModel(private val repo: ShoppingRepository) : ViewModel() {

    val uiState: StateFlow<ShoppingUiState> = combine(
        repo.getAllShoppingItems(),
        repo.getPendingItemCount()
    ) { items, pending ->
        ShoppingUiState(items = items, pendingCount = pending)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ShoppingUiState(isLoading = true))

    fun addItem(name: String, quantity: Double, unit: String, notes: String = "", estimatedPrice: Double = 0.0) {
        viewModelScope.launch {
            repo.insertShoppingItem(ShoppingItemEntity(name = name, quantity = quantity, unit = unit, notes = notes, estimatedPrice = estimatedPrice))
        }
    }

    fun togglePurchased(item: ShoppingItemEntity) {
        viewModelScope.launch { repo.setItemPurchased(item.id, !item.isPurchased) }
    }

    fun deleteItem(item: ShoppingItemEntity) {
        viewModelScope.launch { repo.deleteShoppingItem(item) }
    }
}
