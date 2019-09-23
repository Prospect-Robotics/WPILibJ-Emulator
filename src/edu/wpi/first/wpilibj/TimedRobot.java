/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

/**
 * TimedRobot implements the IterativeRobotBase robot program framework.
 *
 * <p>The TimedRobot class is intended to be subclassed by a user creating a robot program.
 *
 * <p>periodic() functions from the base class are called on an interval by a Notifier instance.
 */
public class TimedRobot extends IterativeRobotBase {
  public static final double kDefaultPeriod = 0.02;

  /**
   * Constructor for TimedRobot.
   */
  protected TimedRobot() {
    this(kDefaultPeriod);
  }

  /**
   * Constructor for TimedRobot.
   *
   * @param period Period in seconds.
   */
  protected TimedRobot(double period) {
    super(period);
  }

  @Override
  @SuppressWarnings("NoFinalizer")
  protected void finalize() {
  }

  /**
   * Provide an alternate "main loop" via startCompetition().
   */
  @Override
  @SuppressWarnings("UnsafeFinalization")
  public void startCompetition() {
	  m_ds.emulatorStartCommandProcessing();
	  long iteration = 0;
	  long period_micros = (long)(m_period * 1e6);
	  robotInit();

	  // Tell the DS that the robot is ready to be enabled

	  long expirationTime = RobotController.getFPGATime();
	  updateAlarm();

	  // Loop forever, calling the appropriate mode-dependent function
	  long min_loop = Long.MAX_VALUE;
	  long max_loop = Long.MIN_VALUE;
	  long loop_sum = 0;
	  long stat_cnt = 0;
	  while (!m_ds.emulatorIsPowerdown()) {
		  long now = RobotController.getFPGATime();
		  long wait_micros = expirationTime - now;
		  if (wait_micros > 0) {
			  long millis = wait_micros / 1_000;
			  int nanos = (int)((wait_micros % 1_000) * 1_000);
			  try {
				  Thread.sleep(millis, nanos);
			  } catch (InterruptedException ie) {
				  ie.printStackTrace();
				  break;
			  }
		  }
		  expirationTime += period_micros;
		  updateAlarm();
		  long loop_start_time = System.nanoTime();
		  loopFunc();
		  long loop_delta = System.nanoTime() - loop_start_time;
		  loop_sum += loop_delta;
		  if (min_loop > loop_delta)
			  min_loop = loop_delta;
		  if (max_loop < loop_delta)
			  max_loop = loop_delta;
		  stat_cnt++;
		  
		  if (stat_cnt >= 100) {
			  System.out.println("min: " + min_loop + "  max: " + max_loop + "  avg: :" + loop_sum / stat_cnt);
			  min_loop = Long.MAX_VALUE;
			  max_loop = Long.MIN_VALUE;
			  loop_sum = 0;
			  stat_cnt = 0;
		  }
		  if (false) {
			  if (iteration % 500 == 0) {
				  System.out.print("\nLoop: " + expirationTime + '.');
				  System.out.flush();
			  } else if (iteration % 10 == 0){
				  System.out.print('.');
				  System.out.flush();
			  }
		  }
		  iteration++;
	  }
	  // We are powering down.  Don't return as that would produce an error message, just successfully exit.
	  System.exit(0);
  }

  /**
   * Get time period between calls to Periodic() functions.
   */
  public double getPeriod() {
    return m_period;
  }

  /**
   * Update the alarm hardware to reflect the next alarm.
   */
  @SuppressWarnings("UnsafeFinalization")
  private void updateAlarm() {
   // NotifierJNI.updateNotifierAlarm(m_notifier, (long) (m_expirationTime * 1e6));
  }
}
