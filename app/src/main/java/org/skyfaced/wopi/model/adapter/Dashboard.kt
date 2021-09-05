package org.skyfaced.wopi.model.adapter

data class DashboardHeader(
    val title: String,
) : Item

data class DashboardItem(
    val id: Int,
    val city: String,
    val imageUrl: String,
    val temperature: String,
) : Item

class DashboardAdd : Item