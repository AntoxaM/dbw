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

  val dbCmd = "/opt/deadbeef-devel/deadbeef"
  private val separator: String = "@X4-"
  val infoCommand = Seq(dbCmd , "--nowplaying-tf",s" %artist% $separator %album% $separator %year% $separator %title% $separator %length_ex% $separator %isplaying% $separator %list_index%")

  def getState: Future[PlayInfo] = Future{
    val res = runCmd(infoCommand)
    parseInfo(res)
  }

  def togglePlay: Future[PlayInfo] = Future{
    val res = runCmd(getCmd("--toggle-pause"))
    parseInfo(res)
  }

  def next: Future[PlayInfo] = Future{
    val res = runCmd(getCmd("--next"))
    parseInfo(res)
  }
  def previous: Future[PlayInfo] = Future{
    val res = runCmd(getCmd("--prev"))
    parseInfo(res)
  }
  def random: Future[PlayInfo] = Future{
    val res = runCmd(getCmd("--random"))
    parseInfo(res)
  }

  def parseInfo(res: Stream[String]): PlayInfo = {
    val last = if (res.isEmpty) "" else res.last

    Logger.debug(s"Playing: $last")

    val rxPI = s"(.*) $separator (.*) $separator (.*) $separator (.*) $separator (.*) $separator (.*) $separator (.*)".r
    val snd = rxPI.findFirstIn(last)
    if (snd.nonEmpty) {
      snd match {
        case Some(rxPI(artist, album, year, title, length, isPlaying, play_index)) =>
          val info: PlayInfo = PlayInfo(artist, album, year, title, length, isPlaying == "1", play_index)
          Logger.debug(s"PI:$info")
          info
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

  def getCmd(cmdName: String ): ProcessBuilder = {
    Seq(dbCmd, cmdName) #&& infoCommand
  }
}
