package im.mange.flakeless.scalatest

import im.mange.flakeless.Goto
import org.scalatest.refspec.RefSpec

//TODO: move to example dir
class ExampleSpec extends RefSpec with WebSpec {
  def `blah blah` = testInBrowser(sut => {
    Goto(sut.browser, sut.baseUrl)
    println("I've run")
  })
}
