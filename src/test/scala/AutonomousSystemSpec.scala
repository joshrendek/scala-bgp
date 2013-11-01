package com.joshrendek.scalabgp

import org.scalatest.FunSpec
import co.freeside.betamax.Recorder
import co.freeside.betamax.proxy.jetty.ProxyServer

class AutonomousSystemSpec extends FunSpec {

  val recorder = new Recorder
  val proxyServer = new ProxyServer(recorder)
  recorder.insertTape("HE.lg.as174")
  proxyServer.start()

  val as = AutonomousSystem(174, Some(recorder.getProxyPort))

  recorder.ejectTape()
  proxyServer.stop()
  describe("fetching and parsing xhtml") {
    it("should"){
      println(as.peerList)
    }
  }
}

