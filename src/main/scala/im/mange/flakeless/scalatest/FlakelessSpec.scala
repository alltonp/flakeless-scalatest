package im.mange.flakeless.scalatest

import org.scalatest.{Outcome, TestSuite}

trait FlakelessSpec extends TestSuite {
  val systemUnderTestPool: SystemUnderTestPool
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

  def on(testBody: SystemUnderTest => Unit): Unit = {
    val sut = systemUnderTestPool.take().getOrElse(throw new RuntimeException("Failed to get a SystemUnderTest"))
    //I can be a case class and the first couple maybe be vals?
//    val sut = new SystemUnderTest {
//      override val baseUrl =  "http://www.google.co.uk"
//      override val browser = Flakeless(new PhantomJSDriver())
//      override def reset() = {}
//      override def reportFailure(t: Throwable) = {}
//    }

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


