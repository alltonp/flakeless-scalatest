package im.mange.flakeless.scalatest

//class SystemUnderTestPool {
//
//}

//package im.mange.driveby.pool

import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}

import collection.mutable.ListBuffer
import collection.mutable.ListBuffer
//import im.mange.driveby.browser.{Browser, InternalBrowser}
//import im.mange.common.Times
//import im.mange.driveby.{BrowserFactory, BrowserType, DriveByConfig, Example}

import scala.concurrent.ExecutionContext.Implicits.global
//import actors.threadpool.TimeUnit
//import im.mange.driveby.tracking._

//import scala.concurrent.Future
import scala.concurrent.Future

case class SystemUnderTestPool(suts: List[SystemUnderTest])  {
  suts.foreach(add)

  private val allBrowsers = new ListBuffer[SystemUnderTest]
  private val availableBrowsers = new LinkedBlockingQueue[SystemUnderTest]

//  def add(browser: BrowserType, instances: Int = 1) {
//    times(instances) { add(BrowserFactory.create(browser)) }
//  }

  def write(systemUnderTest: SystemUnderTest) {
//    browser match {
//      case Some(b) => {
//        b.asInstanceOf[InternalBrowser].exampleId = -1
//        Tracker.add(BrowserWritten(example.id, b.asInstanceOf[InternalBrowser].id))
        availableBrowsers.put(systemUnderTest)
//      }
//      case None => Tracker.add(Info("BrowserWriteRequestedForDead", example.id))
//    }
  }

  def take(): Option[SystemUnderTest] = {
    //TODO: do this ...
    //    if (terminallyIll) {
    //      Tracker.add
    //      return None
    //    }

//    Tracker.add(BrowserTakeRequested(example.id))
    val browser = availableBrowsers.poll(60000, TimeUnit.MILLISECONDS)
    if (browser == null) {
//      Tracker.add(BrowserTakeTimeout(example.id))
      None
    } else {
//      Tracker.add(BrowserTaken(example.id, browser.asInstanceOf[InternalBrowser].id))
//      browser.asInstanceOf[InternalBrowser].exampleId = example.id
      Some(browser)
    }
  }

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

  private def add(browser: SystemUnderTest) {
    allBrowsers.append(browser)
    availableBrowsers.put(browser)
  }
}