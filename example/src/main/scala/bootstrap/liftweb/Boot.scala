package bootstrap.liftweb

import code.config.Site

import net.liftweb._
import common._
import http._
import util._
import Helpers._

import net.liftmodules.extras.{Gravatar, LiftExtras}
import net.liftmodules.extras.snippet.BsNotices

// import net.liftmodules.JQueryModule
// import net.liftweb.http.js.jquery._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // set the sitemap.
    LiftRules.setSiteMap(Site.siteMap)

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-spinner").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-spinner").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    // Init Extras
    // LiftExtras.init()
    LiftRules.addToPackages("net.liftmodules.extras")
    LiftRules.autoIncludeAjaxCalc.default.set(() => (session: LiftSession) => false);
    BsNotices.init()
    BsNotices.errorTitle.default.set(Full("Error!"))
    BsNotices.warningTitle.default.set(Full("Warning!"))
    BsNotices.noticeTitle.default.set(Full("Info!"))
    BsNotices.successTitle.default.set(Full("Success!"))

    Gravatar.defaultImage.default.set("wavatar")

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    /*LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery172
    JQueryModule.init()*/

  }
}