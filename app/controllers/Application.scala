package controllers

import play.api.libs.EventSource
import play.api.libs.iteratee.{Enumeratee, Enumerator, Concurrent}
import play.api.libs.json.{Json, JsValue}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.data.Form
import play.api.data.Forms._

import java.io.FileWriter

class Application extends Controller {

  val (chatOut, chatChannel) = Concurrent.broadcast[JsValue]

	def index = Action {
    Ok(views.html.index())
  }

  // Central hub for distributing chat messages
  def chat = Action { implicit req =>
    Ok(views.html.chat(routes.Application.chatFeed, routes.Application.postMessage))
  }

  // Detects disconnect SSE stream
  def connDeathWatch(addr: String): Enumeratee[JsValue, JsValue] =
    Enumeratee.onIterateeDone{ () => println(addr + " - SSE disconnected") }

  def welcome = Enumerator.apply[JsValue](
    Json.obj(
      "user"    -> "Local",
      "message" -> "Hola, ¿cómo estás, qué te puedo recomendar: comida, entretenimiento o cultura?"
    )
  )

  // Serves chat msgs to clients
  def chatFeed = Action { req =>
    println("Someone connected" + req.remoteAddress)
    Ok.chunked(
      welcome >>> chatOut
        &> connDeathWatch(req.remoteAddress)
        &> EventSource()
    ).as("text/event-stream")
  }

  // Posts chat msgs
  def postMessage = Action(parse.json) { req =>
    chatChannel.push(req.body)
    Ok
  }

  def userSignup = Action { implicit req =>
    val user = userParamsBinding.bindFromRequest.get
    val fw   = new FileWriter("signups.txt", true)
    try {
      fw.write(user.toString + "\n")
    } finally fw.close()
    NoContent
  }

  private val userParamsBinding = Form(
    mapping(
      "type" -> nonEmptyText,
      "email" -> nonEmptyText
    )(UserParams.apply)(UserParams.unapply)
  )

  case class UserParams(`type`: String, email: String)

}
