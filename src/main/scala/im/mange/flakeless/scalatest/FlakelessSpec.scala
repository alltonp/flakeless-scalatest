package im.mange.flakeless.scalatest

import im.mange.flakeless.reports.CurrentTestReport
import org.scalatest.{Outcome, TestSuite}

//TODO: do we need Resettable? - not so far
//TODO: can SUT be a case class?
//TODO: definitely need empty on pool
//TODO: do we need ???
//TODO: running in intellij have just one, one per cpu in sbt (see driveby)
//TODO: deprecate driveby and friends
//TODO: any innards?
//TODO: using probably wants to return a customer Driver instead of raw flakeless - maybe, maybe not
//TODO: Pool to something snappy
//TODO: support reportAlways .. but where to put the Config .. seems odd to have one here as well as flakeless
//... unless we extends one with the other ... flakeless.scalatest.Config

trait FlakelessSpec[T <: SystemUnderTest] extends TestSuite {

  //TODO: be nice to hide this somehow, so subclasses can't see it
  protected val sutPool: Pool[T]
  protected var alwaysReport = false

  //TODO: find a way to generalise this, because UnitSpec needs it too
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

  //TODO: find a way to generalise this, so we can choose what to return, e.g. a driver
  def using(testBody: T => Unit): Unit = {
    val sut = sutPool.take().getOrElse(throw new RuntimeException("Failed to get a SystemUnderTest from pool:\n" + sutPool.status))

    try {
      sut.browser.startFlight(suite, currentTestName)
      sut.resetBeforeTest(suite, sut.browser.getCurrentFlightNumber)
      testBody(sut)
    } catch {
      case t: Throwable =>
        sut.reportFailure(t)
        throw t
    } finally {
      if (alwaysReport) CurrentTestReport(sut.browser)
      sut.browser.stopFlight()
      sutPool.write(sut)
    }
  }
}