import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.eq

fun Route.classRouting() {
    route("/classReference"){
        get{
            call.respond(classCollection.find().toList().map { it->it.name })
        }
    }
    route("/classes") {
        get {
            call.respond(classCollection.find().toList())
        }
        get("{name?}") {
            val name= call.parameters["name"] ?: return@get call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            val pclass = classCollection.findOne(Feat::name eq name)?: return@get call.respondText(
                "No class with name $name",
                status = HttpStatusCode.NotFound
            )
            call.respond(pclass)
        }
    }
}