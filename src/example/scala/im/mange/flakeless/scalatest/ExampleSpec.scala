package im.mange.flakeless.scalatest

import im.mange.flakeless.{Flakeless, Goto}
import org.scalatest.refspec.RefSpec

object Moo {
  val moo = SystemUnderTestPool(
    List(new SystemUnderTest {
      //launch application here ...
      private val applicaton = ???
      private val port = ???
      private val webDriver = ??? //e.g. new org.openqa.selenium.phantomjs.PhantomJSDriver()

      override val baseUrl = s"http://localhost:$port"
      override val browser = Flakeless(webDriver)

      override def reset() = {}
    })
  )
}

abstract class WebSpec extends RefSpec with FlakelessSpec {
  val systemUnderTestPool = Moo.moo
}

class ExampleSpec extends WebSpec {
  def `blah blah` = using(sut => {
    Goto(sut.browser, sut.baseUrl)
    println("I've run")
  })
}
