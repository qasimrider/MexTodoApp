package com.mex.todoapp.model

data class FilterCriteria(
    var category: Category = Category.ALL,
    var priority: Priority = Priority.ALL,
    var query: String = "",
    var isClearAll: Boolean = false,
    var isCompleted: Boolean = false
) {

}

fun FilterCriteria.resetFilters(): FilterCriteria {
    return this.apply {
        category = Category.ALL
        priority = Priority.ALL
        query = ""
        isClearAll = false
        isCompleted = false
    }
}