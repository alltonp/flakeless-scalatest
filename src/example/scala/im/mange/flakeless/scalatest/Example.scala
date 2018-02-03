package im.mange.flakeless.scalatest

import im.mange.flakeless.{Flakeless, Goto}
import org.scalatest.refspec.RefSpec

object Infrastructure {
  val sutPool = SystemUnderTestPool(
    List(new SystemUnderTest {
      //launch application here ...
      private val application = ???
      private val port = ???
      private val webDriver = ??? //e.g. new org.openqa.selenium.phantomjs.PhantomJSDriver()

      override val baseUrl = s"http://localhost:$port"
      override val browser = Flakeless(webDriver)

      override def resetBeforeTest() = {}
    })
  )
}

abstract class WebSpec extends RefSpec with FlakelessSpec {
  protected val sutPool: SystemUnderTestPool = Infrastructure.sutPool
}

class ExampleSpec extends WebSpec {
  def `blah blah`() = using(sut => {
    Goto(sut.browser, sut.baseUrl)
    //etc...
    //see: https://github.com/alltonp/flakeless
  })
}
