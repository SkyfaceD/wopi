package org.skyfaced.wopi.model.adapter

data class DashboardHeader(
    val title: String,
) : Item

data class DashboardItem(
    val id: Int,
    val city: String,
    val imageRes: Int,
    val temperature: String,
) : Item

class DashboardAdd : Item