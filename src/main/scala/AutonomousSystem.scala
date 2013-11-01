package com.joshrendek.scalabgp

import org.openqa.selenium.htmlunit._
import scala.collection.JavaConversions._
import org.openqa.selenium.By


case class ASMeta(as: String, name: String)

object AutonomousSystem {

  def apply(as: Integer, port: Option[Int]): AutonomousSystem = {
    new AutonomousSystem(as, fetchPeerList(as, port))
  }


  def fetchPeerList(as: Integer, port: Option[Int]): Seq[ASMeta] = {
    val url = "http://bgp.he.net/AS" + as

    val driver = new HtmlUnitDriver
    if (port.isDefined) {
      driver.setProxy("localhost", port.get)
    }
    driver.get(url)

    val peers = driver.findElementsByXPath("//*[@id=\"table_peers4\"]/tbody/tr")
    peers.map {
      peer =>
        val row = peer.findElements(By.tagName("td")).map(f => f.getText).toList
        new ASMeta(row(0), row(1))
    }
  }
}

class AutonomousSystem(val as: Integer, val peerList: Seq[ASMeta]) {
}

