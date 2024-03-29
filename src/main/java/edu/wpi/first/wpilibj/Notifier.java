/*----------------------------------------------------------------------------*/
/* Copyright (c) 2016-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Notifier implements AutoCloseable {
  // The thread waiting on the HAL alarm.
  private Thread m_thread;
  // The lock for the process information.
  private final ReentrantLock m_processLock = new ReentrantLock();
  private final Condition m_condition = m_processLock.newCondition();
  // The C pointer to the notifier object. We don't use it directly, it is
  // just passed to the JNI bindings.
  private final AtomicInteger m_notifier = new AtomicInteger();
  // The time, in microseconds, at which the corresponding handler should be
  // called. Has the same zero as Utility.getFPGATime().
  private long m_expirationTime;
  // The handler passed in by the user which should be called at the
  // appropriate interval.
  private Runnable m_handler;
  // Whether we are calling the handler just once or periodically.
  private boolean m_periodic;
  // If periodic, the period of the calling uS; if just once, stores how long it
  // is until we call the handler.
  private long m_period = 10_000_000;

  @Override
  protected void finalize() {
    close();
  }

  @Override
  public void close() {
    int handle = m_notifier.getAndSet(0);
    if (handle == 0) {
      return;
    }
    //NotifierJNI.stopNotifier(handle);
    // Join the thread to ensure the handler has exited.
    if (m_thread.isAlive()) {
      try {
        m_thread.interrupt();
        m_thread.join();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
      }
    }
    //NotifierJNI.cleanNotifier(handle);
    m_thread = null;
  }

  /**
   * Update the alarm hardware to reflect the next alarm.
   *
   * @param triggerTime the time at which the next alarm will be triggered
   */
  private void updateAlarm() {
    int notifier = m_notifier.get();
    if (notifier == 0) {
      return;
    }
    m_processLock.lock();
    m_condition.signal();
    m_processLock.unlock();
    //NotifierJNI.updateNotifierAlarm(notifier, triggerTime);
  }

  private static int instanceNum;
  private static Object instanceNumLock = new Object();
  /**
   * Create a Notifier for timer event notification.
   *
   * @param run The handler that is called at the notification time which is set
   *            using StartSingle or StartPeriodic.
   */
  public Notifier(Runnable run) {
      m_handler = run;
      m_notifier.set(1 /*NotifierJNI.initializeNotifier()*/);

      m_thread = new Thread(() -> {
outer:	  while (!Thread.interrupted()) {
	      int notifier = m_notifier.get();
	      if (notifier == 0) {
		  break;
	      }

	      Runnable handler = null;
	      m_processLock.lock();
	      try {
		  long now = RobotController.getFPGATime();
		  long wait_micros = m_expirationTime - now;
		  if (wait_micros > Long.MAX_VALUE/1000)
		      throw new InternalError();
		  long wait_nanos = 1000 * wait_micros;
		  while (wait_nanos > 0) {
		      m_condition.awaitNanos(wait_nanos);
		      if (Thread.interrupted())
			  break outer;
		      now = RobotController.getFPGATime();
		      wait_micros = m_expirationTime - now;
			  if (wait_micros > Long.MAX_VALUE/1000)
			      throw new InternalError();
		      wait_nanos = 1000 * wait_micros;
		  }
		  handler = m_handler;
		  if (m_periodic) {
		      m_expirationTime += m_period;
		  } else {
		      // need to update the alarm to cause it to wait again
		      m_expirationTime = Long.MAX_VALUE / 1000;
		  }
	      } catch (InterruptedException ie) {
		  break;
	      } finally {
		  m_processLock.unlock();
	      }

	      if (handler != null) {
		  RobotEmulator re = RobotEmulator.getInstance();
		  re.logNotifier("handler-enter");
		  handler.run();
		  re.logNotifier("handler-exit");
	      }
	  }
      });
      String thread_name;
      synchronized(instanceNumLock) {
	  thread_name = "Notifier-" + instanceNum;
	  instanceNum++;
      }
      m_thread.setName(thread_name);
      m_thread.setDaemon(true);
      m_thread.setUncaughtExceptionHandler((thread, error) -> {
	  Throwable cause = error.getCause();
	  if (cause != null) {
	      error = cause;
	  }
	  DriverStation.reportError("Unhandled exception: " + error.toString(), error.getStackTrace());
	  DriverStation.reportError(
		  "The loopFunc() method (or methods called by it) should have handled "
			  + "the exception above.", false);
      });
      m_thread.start();
  }

  /**
   * Change the handler function.
   *
   * @param handler Handler
   */
  public void setHandler(Runnable handler) {
    m_processLock.lock();
    try {
      m_handler = handler;
    } finally {
      m_processLock.unlock();
    }
  }

  /**
   * Register for single event notification. A timer event is queued for a single
   * event after the specified delay.
   *
   * @param delay Seconds to wait before the handler is called.
   */
  public void startSingle(double delay) {
    m_processLock.lock();
    try {
      m_periodic = false;
      m_period = (long)(1e6 * delay);
      m_expirationTime = RobotController.getFPGATime() + m_period;
      updateAlarm();
    } finally {
      m_processLock.unlock();
    }
  }

  /**
   * Register for periodic event notification. A timer event is queued for
   * periodic event notification. Each time the interrupt occurs, the event will
   * be immediately requeued for the same time interval.
   *
   * @param period Period in seconds to call the handler starting one period after
   *               the call to this method.
   */
  public void startPeriodic(double period) {
    m_processLock.lock();
    try {
      m_periodic = true;
      m_period = (long)(1e6 * period);
      m_expirationTime = RobotController.getFPGATime() + m_period;
      updateAlarm();
    } finally {
      m_processLock.unlock();
    }
  }

  /**
   * Stop timer events from occurring. Stop any repeating timer events from
   * occurring. This will also remove any single notification events from the
   * queue. If a timer-based call to the registered handler is in progress, this
   * function will block until the handler call is complete.
   */
  public void stop() {
    //NotifierJNI.cancelNotifierAlarm(m_notifier.get());
  }
}
