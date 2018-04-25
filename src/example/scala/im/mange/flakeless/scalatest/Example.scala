package im.mange.flakeless.scalatest

import im.mange.flakeless.{Flakeless, Goto}
import org.scalatest.refspec.RefSpec

abstract class XSystemUnderTest extends SystemUnderTest {
  def stub(): Unit = {}
}

object WebSpecContext {
  val sutPool = Pool[XSystemUnderTest](
    List(new XSystemUnderTest {
      //launch application here ...
//      private val application = ???
      private val port = 9000
      private val webDriver = ??? //e.g. new org.openqa.selenium.phantomjs.PhantomJSDriver()

      override val baseUrl = s"http://localhost:$port"
      override val browser = Flakeless(webDriver)

      override def resetBeforeTest(suite: String, flightNumber: Int) = {}
    })
  )
}

abstract class WebSpec extends RefSpec with FlakelessSpec[XSystemUnderTest] {
  protected override val sutPool: Pool[XSystemUnderTest] = WebSpecContext.sutPool
}

class ExampleSpec extends WebSpec {
  def `blah blah`() = using(sut => {
    sut.stub()
    Goto(sut.browser, sut.baseUrl)
    //etc...
    //see: https://github.com/alltonp/flakeless
  })
}
