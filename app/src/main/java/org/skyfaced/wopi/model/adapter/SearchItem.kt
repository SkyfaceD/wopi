package org.skyfaced.wopi.model.adapter

data class SearchItem(
    val id: Int,
    val city: String,
    val description: String,
) : Item