package im.mange.flakeless.scalatest

import im.mange.flakeless.Flakeless
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.{Outcome, TestSuite}

trait SystemUnderTest {
  def baseUrl: String
  def browser: Flakeless
  def reset(): Unit
  def reportFailure(t: Throwable): Unit
}

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

  def testInBrowser(testBody: SystemUnderTest => Unit): Unit = {
//    val sut = PooledServer.aquire
    val sut = new SystemUnderTest {
      override def baseUrl: String =  "http://www.google.co.uk"
      override def browser: Flakeless = Flakeless(new PhantomJSDriver())
      override def reset(): Unit = {}
      override def reportFailure(t: Throwable): Unit = {}
    }

    try {
      sut.browser.startFlight(suite, currentTestName)
      sut.reset()
      testBody(sut)
    } catch {
      case t: Throwable =>
        sut.reportFailure(t)
      throw t
    } finally {
      sut.browser.stopFlight()
//      PooledServer.release(sut)
    }
  }
}


