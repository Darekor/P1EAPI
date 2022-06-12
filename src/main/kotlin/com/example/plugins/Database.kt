import com.mongodb.ConnectionString
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

import com.parser.types.*

lateinit var featCollection: CoroutineCollection<Feat>
lateinit var classCollection: CoroutineCollection<PClass>
lateinit var raceCollection: CoroutineCollection<Race>



fun Application.configureDatabase() {

    val connectionString: ConnectionString? = System.getenv("MONGODB_URI")?.let {
        ConnectionString(it)
    }
    val client = if (connectionString != null) KMongo.createClient(connectionString).coroutine else KMongo.createClient().coroutine
    val database = client.getDatabase(connectionString?.database ?: "p1e_data")
    featCollection = database.getCollection<Feat>()
    classCollection = database.getCollection<PClass>()
    raceCollection = database.getCollection<Race>()
    }

fun updateDB()
{
    initFeats(collectFeatsInfo())
    initClasses(collectClassesInfo())
    initRaces(collectRacesInfo())
}

fun initFeats(FeatList:List<Feat>)
{
    runBlocking {
        featCollection.deleteMany()
        featCollection.insertMany(FeatList)
    }
}

fun initClasses(ClassList:List<PClass>)
{
    runBlocking {
        classCollection.deleteMany()
        classCollection.insertMany(ClassList)
    }
}

fun initRaces(RacesList:List<Race>)
{
    runBlocking {
        raceCollection.deleteMany()
        raceCollection.insertMany(RacesList)
    }
}
