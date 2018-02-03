package im.mange.flakeless.scalatest

import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}
import collection.mutable.ListBuffer

import scala.concurrent.ExecutionContext.Implicits.global

//import scala.concurrent.Future
import scala.concurrent.Future

case class SystemUnderTestPool(private val suts: List[SystemUnderTest])  {
  private val all = new ListBuffer[SystemUnderTest]
  private val available = new LinkedBlockingQueue[SystemUnderTest]

  suts.foreach(add)

//  def add(browser: BrowserType, instances: Int = 1) {
//    times(instances) { add(BrowserFactory.create(browser)) }
//  }

  def write(systemUnderTest: SystemUnderTest) {
    available.put(systemUnderTest)
  }

  def take(): Option[SystemUnderTest] = {
    Option(available.poll(60000, TimeUnit.MILLISECONDS))
  }

  def status = s"all: $all\navailable: $all"

//  def fill() {
//    Future {
//      //TODO: add BrowserControllers to the println
//      println("### Warming up browsers ... " + browserType)
//      add(browserType, browserInstances)
//    }
//  }

//  def empty() {
//    //TODO: track how long fill and empty take
//    allBrowsers.par.map(browser => {
//      try {
//        Tracker.add(BrowserCloseRequested(browser.id))
//        browser.close()
//        Tracker.add(BrowserClosed(browser.id))
//      } catch {
//        case e: Exception => Tracker.add(BrowserCloseFailed(browser.id))
//      }
//    })
//  }

  private def add(sut: SystemUnderTest) {
    all.append(sut)
    available.put(sut)
  }
}