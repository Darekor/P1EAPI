import com.google.gson.GsonBuilder
import org.jsoup.Jsoup
import java.io.File

import com.parser.types.*

const val PFSRD = "https://www.d20pfsrd.com"

fun collectFeatsInfo():List<Feat> {
    return Jsoup.connect("$PFSRD/feats").get()
        .select("h4 [id$=Feats] a[href]")
        .mapNotNull{ scrapeFeatPage(it.attr("href"))}
        .flatten()
        .distinctBy{it.name}
}

fun collectRacesInfo():List<Race> {
    return Jsoup.connect("$PFSRD/races")
        .get()
        .select("table:has(caption:matchesOwn(Table: Racial Features)) tbody tr td:first-of-type a")
        .mapNotNull{ scrapeRace(it.attr("href"), it.text()) }
}

fun collectClassesInfo():List<PClass> {
    return Jsoup.connect("$PFSRD/classes")
        .get()
        .select("tbody tr td:first-of-type b a")
        .mapNotNull{ scrapeClass(it.attr("href")) }
}

private fun <T> generateJson(list:List<T>,fname:String = "file",fpath:String? = null)
{
    val gBuilder = GsonBuilder().setPrettyPrinting().create()
    when (fpath)
    {
        null -> return File("$fname.json").writeText(gBuilder.toJson(list))
        else -> return File("$fpath\\$fname.json").writeText(gBuilder.toJson(list))
    }
}