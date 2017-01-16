package controllers

import javax.inject.Inject

import play.api.libs.json.{JsObject, JsValue, Json, Writes}
import play.api.mvc.{Action, Controller}
import services.DBService
import scala.concurrent.ExecutionContext.Implicits.global

class DB @Inject()(dbService: DBService) extends Controller {

  private def toDataField[A](value: A)(implicit writes: Writes[A]): JsValue = {
    JsObject(Seq("data" -> Json.toJson(value)))
  }

  def getState() = Action.async {
    dbService.getState.map{ s => Ok(toDataField(s)) }
  }

  def togglePlay() = Action.async {
    dbService.togglePlay.map{ s => Ok("") }
  }
  def next() = Action.async {
    dbService.next.map{ s => Ok("") }
  }
  def previous() = Action.async {
    dbService.previous.map{ s => Ok("") }
  }
  def random() = Action.async {
    dbService.random.map{ s => Ok("") }
  }
}
