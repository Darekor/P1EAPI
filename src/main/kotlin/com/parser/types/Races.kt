package com.parser.types

import kotlinx.serialization.Serializable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

enum class FeatureTypes
{
    EX, SP, SU, RCL, UTP
}

data class Subrace(val name:String,
                   val sources:Map<String,String>,
                   val lore:String,
                   val features:List<RacialFeature>)

interface Feature{
    val name:String
    val description:String
    val type:FeatureTypes
}

@Serializable
data class RacialFeature(override val name:String,
                         val sources:Map<String,String>,
                         override val description:String,
                         val replaces:List<String> = listOf(),
                         override val type:FeatureTypes= FeatureTypes.UTP): Feature
@Serializable
data class Race(val name:String,
                val sources:Map<String,String>,
                val lore:String,
                val baseFeatures:List<RacialFeature>,
                val replacementFeatures:List<RacialFeature>,
                val favouredClassOptions: List<FavouredClassOption>)

@Serializable
data class FavouredClassOption(val race:String,
                               val pclass:String,
                               val benefit:String)

fun scrapeRace(url: String,raceName:String = ""):Race?
{
    val racePage: Document
    try
    {
        racePage = Jsoup.connect(url).get()
    }
    catch (e:Exception)
    {
        return null
    }
    return raceFromData(racePage,raceName)
}

fun raceFromData(raceData: Document, raceName:String = ""):Race {
    val sources: MutableMap<String, String> = mutableMapOf()
    var lore = ""
    val baseFeatures: MutableList<RacialFeature> = mutableListOf()
    val replacementFeatures: MutableList<RacialFeature> = mutableListOf()
    val favouredClassOptions: MutableList<FavouredClassOption> = mutableListOf()

    raceData.select("div.article-content").html().split("(?=(<h3>.*?</h3>))".toRegex())
        .map {Jsoup.parse(it)}
        .forEach {
            when{
                (it.select("h3").isEmpty())->
                    it.select("p:not([class])").forEach{
                    lore+=it.text()+"\n" }
                (it.text().startsWith("Standard Racial Traits"))->
                    it.select("li").forEach{
                        val features = """<b>(.*?)</b>:?(.*)""".toRegex().matchEntire(it.html())?.groupValues
                        if (features != null) {
                            if (features[1].contains("speed",true))
                                baseFeatures.add(RacialFeature("Base Speed", mapOf(),"\\d+".toRegex().find(Jsoup.parse(features[2]).text())?.value?:Jsoup.parse(features[2]).text(), listOf(),FeatureTypes.RCL))
                            else if (features[1].contains("size",true))
                                features[2].findAnyOf(listOf("Large","Medium","Small"),0,true)?.second?.let {
                                    baseFeatures.add(RacialFeature(Jsoup.parse(features[1]).text(), mapOf(),it, listOf(),FeatureTypes.RCL)) }
                            else
                                baseFeatures.add(RacialFeature(Jsoup.parse(features[1]).text(), mapOf(),Jsoup.parse(features[2]).text(), listOf(),FeatureTypes.RCL))
                        }
                    }
                (it.text().startsWith("Alternate Racial Traits"))->
                    it.select("li").forEach{val features = """<b>(.*?)</b>:?(.*)""".toRegex().matchEntire(it.html())?.groupValues
                        if (features != null) {
                            replacementFeatures.add(RacialFeature(Jsoup.parse(features[1]).text(), mapOf(),Jsoup.parse(features[2]).text(),listOf(),FeatureTypes.RCL))
                        }

                    }
                (it.text().startsWith("Favored Class Options"))->
                    it.select("li").forEach{val features = """<b>(.*?)</b>:?(.*)""".toRegex().matchEntire(it.html())?.groupValues
                        if (features != null) {
                            favouredClassOptions.add(FavouredClassOption(raceName,Jsoup.parse(features[1]).text(),Jsoup.parse(features[2]).text()))
                        }
            }
                }
        }

    return Race(raceName, sources, lore, baseFeatures, replacementFeatures,favouredClassOptions)
}
