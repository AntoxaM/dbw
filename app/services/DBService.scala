package services

import javax.inject.Singleton

import play.Logger
import play.api.libs.json.Json

import scala.sys.process._
import scala.concurrent.Future

/**
Cmd to get
/opt/deadbeef-devel/deadbeef --nowplaying-tf "%artist% - %album% - %year% - %title% - %length_ex% - %isplaying% - %list_index%"

  */
case class PlayInfo(artist: String, album: String, year: String, title: String, length: String, isPlaying: Boolean, index: String)

object PlayInfo{
  implicit val format = Json.format[PlayInfo]
}

@Singleton
class DBService {

  val dbCmd = "/opt/deadbeef-devel/deadbeef --"
  def getState: Future[PlayInfo] = Future.successful{
    val res = runCmd("nowplaying-tf \"%artist% @X- %album% @X- %year% @X- %title% @X- %length_ex% @X- %isplaying% @X- %list_index%\"")
    val last = res.last
    val split = last.split("@X-")

    val artist: String = split(0)
    val album: String = split(1)
    val year: String = split(2)
    val title: String = split(3)
    val length: String = split(4)
    val isPlaying: Boolean = split(5)=="1"
    val play_index: String = split(6)

    PlayInfo(artist, album, year, title, length, isPlaying , play_index)
  }

  def runCmd(command: String):Stream[String] = {
    val cmd = dbCmd+command
    Logger.debug(s"cmd: ${cmd}")
    val res = cmd.lineStream_!
    Logger.debug(s"out: ${res}")
    res
  }

  def togglePlay: Future[Unit] = Future.successful{
    runCmd("toggle-pause")
  }
  def next: Future[Unit] = Future.successful{
    runCmd("next")
  }
  def previous: Future[Unit] = Future.successful{
    runCmd("prev")
  }
  def random: Future[Unit] = Future.successful{
    runCmd("random")
  }

}
