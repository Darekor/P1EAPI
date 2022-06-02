import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq

fun Route.raceRouting() {
    route("/racesReference"){
        get{
            call.respond(raceCollection.find().toList().map { it->it.name })
        }
    }
    route("/races") {
        get {
            call.respond(raceCollection.find().toList())
        }
        get("{name?}") {
            val name= call.parameters["name"] ?: return@get call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            val race = raceCollection.findOne(Feat::name eq name)?: return@get call.respondText(
                "No race with name $name",
                status = HttpStatusCode.NotFound
            )
            call.respond(race)
        }
    }
}