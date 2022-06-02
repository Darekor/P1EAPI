package com.example.models

import kotlinx.serialization.Serializable

val featStorage= mutableListOf<Feat>()

@Serializable
data class Feat(
    val name:String,
    val source:String,
    val sourceSite:String,
    val flavor:String,
    val prerequisites:List<List<String>>,
    val description:String,
    val normal:String,
    val special:String,
    val tags:List<String>,
    val type:String
)