import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq

fun Route.featsRouting() {
    route("/featsReference"){
        get{
            call.respond(featCollection.find().toList().map { it->it.name })
        }
    }
    route("/feats") {
        get {
            call.respond(featCollection.find().toList())
        }
        get("{name?}") {
            val name= call.parameters["name"] ?: return@get call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            val feat = featCollection.findOne(Feat::name eq name)?: return@get call.respondText(
                "No feat with name $name",
                status = HttpStatusCode.NotFound
            )
            call.respond(feat)
        }
    }
}