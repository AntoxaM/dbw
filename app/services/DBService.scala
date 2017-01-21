package services

import javax.inject.Singleton

import play.Logger
import play.api.libs.json.Json

import scala.sys.process._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
Cmd to get
/opt/deadbeef-devel/deadbeef --nowplaying-tf "%artist% - %album% - %year% - %title% - %length_ex% - %isplaying% - %list_index%"

  */
case class PlayInfo(artist: String="", album: String="", year: String="", title: String="", length: String="", isPlaying: Boolean=false, index: String="")

object PlayInfo{
  implicit val format = Json.format[PlayInfo]
}

@Singleton
class DBService {

  val dbCmd = "/opt/deadbeef-devel/deadbeef --"
  val infoCommand = dbCmd + "nowplaying-tf \"%artist% @X- %album% @X- %year% @X- %title% @X- %length_ex% @X- %isplaying% @X- %list_index%\""

  def getState: Future[PlayInfo] = Future{
    val res = runCmd(infoCommand)
    parseInfo(res)
  }

  def parseInfo(res: Stream[String]): PlayInfo = {
    val last = res.last

    val rxPI = "(.+) @X- (.+) @X- (.+) @X- (.+) @X- (.+) @X- (.+) @X- (.+)".r
    val snd = rxPI.findFirstIn(last)
    if (snd.nonEmpty) {
      snd match {
        case Some(rxPI(artist, album, year, title, length, isPlaying, play_index)) => PlayInfo(artist, album, year, title, length, isPlaying == "1", play_index)
      }
    }
    else PlayInfo()
  }

  def runCmd(command: ProcessBuilder):Stream[String] = {
    Logger.debug(s"cmd: $command")
    val res = command.lineStream_!
    Logger.debug(s"out: $res")
    res
  }

  def togglePlay: Future[PlayInfo] = Future{
    val res = runCmd("toggle-pause" #&&  infoCommand)
    parseInfo(res)
  }

  def next: Future[PlayInfo] = Future{
    val res = runCmd("next" #&& infoCommand)
    parseInfo(res)
  }
  def previous: Future[PlayInfo] = Future{
    val res = runCmd("prev" #&& infoCommand)
    parseInfo(res)
  }
  def random: Future[PlayInfo] = Future{
    val res = runCmd("random" #&& infoCommand)
    parseInfo(res)
  }

}
