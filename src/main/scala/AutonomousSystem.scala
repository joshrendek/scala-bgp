package com.joshrendek.scalabgp

import org.openqa.selenium.htmlunit._
import scala.collection.JavaConversions._
import org.openqa.selenium.By
import io.wasted.util.{InetPrefix, Inet4Prefix}
import java.net.InetAddress


case class BgpPeer(as: String, name: String, ipv6: Boolean, rank: Int)

case class Prefix(val subnet: InetPrefix, val description: String)


object AutonomousSystem {

  def apply(as: Int, port: Option[Int]): AutonomousSystem = {
    val queryResult = queryLookingGlass(as, port)
    new AutonomousSystem(as, bgpPeerList(queryResult), prefixList(queryResult),
      ipsOriginated(queryResult), asPathLength(queryResult), countryOfOrigin(queryResult))
  }

  def queryLookingGlass(as: Int, port: Option[Int]): HtmlUnitDriver = {
    val url = "http://bgp.he.net/AS" + as
    val driver = new HtmlUnitDriver
    if (port.isDefined) {
      driver.setProxy("localhost", port.get)
    }
    driver.get(url)
    driver
  }

  def countryOfOrigin(driver: HtmlUnitDriver) = {
    try {
      driver.findElementByXPath("//*[@id=\"asinfo\"]/div[2]/div[2]").getText
    } catch {
      case e: Exception => "N/A"
    }
  }

  def prefixList(driver: HtmlUnitDriver): Seq[Prefix] = {
    try {
      val prefixes = driver.findElementsByXPath("//*[@id=\"table_prefixes4\"]/tbody/tr")
      prefixes.map {
        prefix =>
          val row = prefix.findElements(By.tagName("td")).map(f => f.getText).toList
          val data = row(0).split("/")
          val ipAddr = java.net.InetAddress.getByName(data(0))
          Prefix(InetPrefix(ipAddr, data(1).toInt), row(1))
      }
    } catch {
      case e: Exception => Seq[Prefix]()
    }
  }

  def asPathLength(driver: HtmlUnitDriver) = {
    try {
      driver.findElementByXPath("//*[@id=\"asinfo\"]/div[2]/div[10]")
        .getText.split("\n")(0).split(":")(1).replaceAll(" ", "").toDouble
    } catch {
      case e: Exception => 0
    }

  }

  def ipsOriginated(driver: HtmlUnitDriver) = {
    try {
      driver.findElementByXPath("//*[@id=\"asinfo\"]/div[2]/div[8]")
        .getText.split("\n")(0).split(":")(1).replaceAll(",", "").replaceAll(" ", "").toInt
    } catch {
      case e: NoSuchElementException => 0
      case e: Exception => 0
    }
  }

  def bgpPeerList(driver: HtmlUnitDriver): Seq[BgpPeer] = {
    try {
      val peers = driver.findElementsByXPath("//*[@id=\"table_peers4\"]/tbody/tr")
      peers.map {
        peer =>
          val row = peer.findElements(By.tagName("td")).map(f => f.getText).toList
          val ipv6 = if (row(2) == "X") true else false
          BgpPeer(row(3), row(1), ipv6, row(0).toInt)
      }
    } catch {
      case e: NoSuchElementException => Seq[BgpPeer]()
      case e: Exception => Seq[BgpPeer]()
    }
  }
}

class AutonomousSystem(val as: Int, val bgpPeerList: Seq[BgpPeer],
                       val prefixList: Seq[Prefix], val ipsOriginated: Int,
                       val asPathLength: Double, val countryOfOrigin: String)
  extends Serializable {

  def contains(ipString: String): Boolean = {
    val ip = InetAddress.getByName(ipString)
    prefixList.filter(p => p.subnet.contains(ip)).size > 0
  }

}

