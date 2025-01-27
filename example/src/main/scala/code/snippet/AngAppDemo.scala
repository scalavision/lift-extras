package code
package snippet

import scala.xml.{NodeSeq, Text}

import net.liftweb.common._
import net.liftweb.http.S
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE._
import net.liftweb.json._
import net.liftweb.util._
import Helpers._
import JsonDSL._

import net.liftmodules.extras._
import NgJE._
import NgJsCmds._

object AngAppDemo extends SnippetHelper with Loggable {
  implicit val formats = DefaultFormats

  def render(in: NodeSeq): NodeSeq = {
    val elementId = "angular-demo"

    /**
      * A test function that sends a success notice back to the client.
      */
    def sendSuccess(): JsCmd = LiftNotice.success("You have success").asJsCmd

    /**
      * The function to call when submitting the form.
      */
    def saveForm(json: JValue): JsCmd = {
      for {
        msg <- tryo((json \ "textInput").extract[String])
      } yield {
        val logMsg = "textInput from client: "+msg
        logger.info(logMsg)
        S.notice(logMsg)
        NgBroadcast(elementId, "reset-form")
      }
    }

    val funcs = JsObj(
      "sendSuccess" -> JsExtras.AjaxCallbackAnonFunc(sendSuccess),
      "saveForm" -> JsExtras.JsonCallbackAnonFunc(saveForm)
    )

    val onload: JsCmd =
      NgModule("AngDemoServer", Nil) ~>
        NgConstant("ServerParams", JsObj("x" -> Num(10))) ~>
        NgFactory("ServerFuncs", AnonFunc(JsReturn(funcs))) ~>
        NgConstant("GameData", JsObj("type" -> "Puzzle")) &
      JsVar("window.angAppDemo") ~>
        NgConfig(AnonFunc("GameServiceProvider", Call("GameServiceProvider.setType", Str("Star"))))

    S.appendGlobalJs(JsExtras.IIFE(onload))

    in
  }
}
