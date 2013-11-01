name := "scala-bgp"

organization := "com.joshrendek"

version := "0.0.1"

scalaVersion := "2.10.3"

resolvers += "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.2" % "test",
    "uk.co.bigbeeconsultants" %% "bee-client" % "0.21.+",
    "org.slf4j" % "slf4j-api" % "1.7.+",
    "ch.qos.logback" % "logback-core"    % "1.0.+",
    "ch.qos.logback" % "logback-classic" % "1.0.+",
    "org.json4s" %% "json4s-native" % "3.2.2",
    "co.freeside" % "betamax" % "1.1.2" % "test",
    "org.codehaus.groovy" % "groovy-all" % "1.8.8" % "test",
    "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2",
    "org.seleniumhq.webdriver" % "webdriver-selenium" % "0.9.7376",
    "org.seleniumhq.webdriver" % "webdriver-htmlunit" % "0.9.7376"
)

initialCommands := "import com.joshrendek.scalabgp._"

