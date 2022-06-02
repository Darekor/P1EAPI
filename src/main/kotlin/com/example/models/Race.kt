package com.example.models

import kotlinx.serialization.Serializable

val raceStorage = mutableListOf<Race>()

@Serializable
data class Race(
    val name:String,
    val sources:Map<String,String>,
    val lore:String,
    val baseFeatures:Array<RacialFeature>,
    val replacementFeatures:Array<RacialFeature>,
    val favouredClassOptions:Array<FCO>
) {
    @Serializable
    data class RacialFeature(
        val name:String,
        val sources: Map<String,String>,
        val description:String,
        val replaces:Array<RacialFeature>,
        val type:String
        )
    @Serializable
    data class FCO (
        val race:String,
        val pclass:String,
        val benefit:String
        )
}