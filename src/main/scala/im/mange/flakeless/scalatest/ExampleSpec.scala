package im.mange.flakeless.scalatest

import im.mange.flakeless.Goto
import org.scalatest.refspec.RefSpec

//TODO: move to example dir
class ExampleSpec extends RefSpec with WebSpec {
  def `blah blah` = testInBrowser(flakeless => {
    Goto(flakeless, "http://www.google.co.uk")
    println("I've run")
  })
}
