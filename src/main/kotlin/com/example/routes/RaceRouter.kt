import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq

import com.parser.types.*

fun Route.raceRouting() {
    route("/raceReference"){
        get{
            call.respond(raceCollection.find().toList().map { it->it.name })
        }
    }
    route("/race") {
        get {
            call.respond(raceCollection.find().toList())
        }
        get("{name?}") {
            val name= call.parameters["name"] ?: return@get call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            val race = raceCollection.findOne(Race::name eq name)?: return@get call.respondText(
                "No race with name $name",
                status = HttpStatusCode.NotFound
            )
            call.respond(race)
        }
    }
}