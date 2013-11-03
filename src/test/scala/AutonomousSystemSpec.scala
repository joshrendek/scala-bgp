package com.joshrendek.scalabgp

import org.scalatest.FunSpec
import co.freeside.betamax.Recorder
import co.freeside.betamax.proxy.jetty.ProxyServer
import java.net.InetAddress

class AutonomousSystemSpec extends FunSpec {

  val recorder = new Recorder
  val proxyServer = new ProxyServer(recorder)
  recorder.insertTape("HE.lg.as174")
  proxyServer.start()

  val as = AutonomousSystem(174, Some(recorder.getProxyPort))

  recorder.ejectTape()
  proxyServer.stop()
  describe("fetching and parsing xhtml") {
    it("should have 27990528 ips") {
      assert(as.ipsOriginated == 27990528)
    }

    it("should have an as path length") {
      assert(as.asPathLength == 3.65)
    }

    it("should have a country of origin") {
      assert(as.countryOfOrigin == "United States")
    }

    it("should have Level3 as a BGP Peer") {
      val t = as.bgpPeerList.filter(p => p.name == "Level 3 Communications, Inc.").head
      assert(t.name == "Level 3 Communications, Inc.")
    }

    it("should include a Cogent subnet") {
      val needle = InetAddress.getByName("66.28.252.0")
      val haystack = as.prefixList.filter(p => p.subnet.contains(needle))
      assert(haystack.size == 2)
      assert(haystack.head.description == "Cogent Communications")
    }
  }

  describe("checking if an IP belongs to an AS") {
    it("should include 66.28.252.1") {
      assert(as.contains("66.28.252.1"))
    }
  }
}

