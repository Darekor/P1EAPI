import com.example.models.*
import com.google.gson.Gson
import com.mongodb.ConnectionString
import io.ktor.server.application.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.io.File

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

    initFeats("feats.json")
    initClasses("classes.json")
    initRaces("races.json")
    }

fun initFeats(file:String)
{
    val gson = Gson()
    val featStorage = gson.fromJson(File(file).readText(), Array<Feat>::class.java)

    runBlocking {
        featCollection.deleteMany()
        featCollection.insertMany(featStorage.toList())
    }
}

fun initClasses(file:String)
{
    val gson = Gson()
    val classStorage = gson.fromJson(File(file).readText(), Array<PClass>::class.java)

    runBlocking {
        classCollection.deleteMany()
        classCollection.insertMany(classStorage.toList())
    }
}

fun initRaces(file:String)
{
    val gson = Gson()
    val raceStorage = gson.fromJson(File(file).readText(), Array<Race>::class.java)

    runBlocking {
        raceCollection.deleteMany()
        raceCollection.insertMany(raceStorage.toList())
    }
}
