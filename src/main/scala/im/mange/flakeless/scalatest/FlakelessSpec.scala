package im.mange.flakeless.scalatest

import org.scalatest.{Outcome, TestSuite}

//TODO: do we need Resettable?
//TODO: can SUT be a case class?
//TODO: definitely need empty on pool
//TODO: do we need
//TODO: running in intellij have just one, one per cpu in sbt (see driveby)
//TODO: deprecate driveby and friends
//TODO: move example as per flakeless
//TODO: add fluent page driver (like driveby) - maybe should be in flakeless?
//TODO: any innards?
//TODO: using probably wants to return a customer Driver instead of raw flakeless
//TODO: Pool to something snappy

trait FlakelessSpec extends TestSuite {

  //TODO: ne nice to hide this somehow, so subclasses can't see it
  protected val sutPool: SystemUnderTestPool

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

  def using(testBody: SystemUnderTest => Unit): Unit = {
    val sut = sutPool.take().getOrElse(throw new RuntimeException("Failed to get a SystemUnderTest from pool:\n" + sutPool.status))

    try {
      sut.browser.startFlight(suite, currentTestName)
      sut.resetBeforeTest()
      testBody(sut)
    } catch {
      case t: Throwable =>
        sut.reportFailure(t)
        throw t
    } finally {
      sut.browser.stopFlight()
      sutPool.write(sut)
    }
  }
}