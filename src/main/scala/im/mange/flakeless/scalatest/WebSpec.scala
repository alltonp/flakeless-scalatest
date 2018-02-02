package im.mange.flakeless.scalatest

import im.mange.flakeless.{Flakeless, Goto}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.{FreeSpec, Matchers, Outcome, TestSuite}
import org.scalatest.refspec.RefSpec

trait WebSpec extends TestSuite {
  private val _currentTestName = new ThreadLocal[String]
  private val suite = this.suiteId.split("\\.").reverse.head

  override def withFixture(test: NoArgTest): Outcome = {
    _currentTestName.set(test.name)
    val outcome = super.withFixture(test)
    _currentTestName.set(null)
    outcome
  }

  protected def currentTestName: String = {
    val testName = _currentTestName.get()
    assert(testName != null, "currentTestName should only be called in a test")
    testName
  }

  def testInBrowser(testBody: Flakeless => Unit): Unit = {
//    val sut = PooledServer.aquire
//    val flakeless = sut.flakeless
    val flakeless = Flakeless(new PhantomJSDriver())

    try {
      flakeless.startFlight(suite, currentTestName)
//      sut.reset()
      testBody(flakeless)
    } catch {
      case t: Throwable =>
//        sut.reportFailure(t)
      throw t
    } finally {
      flakeless.stopFlight()
//      PooledServer.release(sut)
    }
  }
}

//TODO: move to example dir
class ExampleSpec extends RefSpec with WebSpec {
  def `blah blah` = testInBrowser(flakeless => {
    Goto(flakeless, "http://www.google.co.uk")
    println("I've run")
  })
}
