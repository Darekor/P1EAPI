package com.example.models

import kotlinx.serialization.Serializable

val classStorage = mutableListOf<PClass>()

@Serializable
data class PClass(
    val name:String,
    val description:String,
    val hitDice:Int,
    val wealthDiceCount: Int,
    val skillPoints:Int,
    val bab:String,
    val saves:Array<String>,
    val special:Array<Map<String,Int>>,
    val classFeatures:Array<ClassFeature>
    ){
    @Serializable
    data class ClassFeature(
        val name:String,
        val description: String,
        val type:String)
}


