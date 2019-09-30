/*----------------------------------------------------------------------------*/
/* Copyright (c) 2008-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Solenoid class for running high voltage Digital Output on the PCM.
 *
 * <p>The Solenoid class is typically used for pneumatic solenoids, but could be used for any
 * device within the current spec of the PCM.
 */
public class Solenoid extends SolenoidBase {
  private final int m_channel; // The channel to control.

  /**
   * Constructor using the default PCM ID (defaults to 0).
   *
   * @param channel The channel on the PCM to control (0..7).
   */
  public Solenoid(final int channel) {
    this(SensorUtil.getDefaultSolenoidModule(), channel);
  }

  /**
   * Constructor.
   *
   * @param moduleNumber The CAN ID of the PCM the solenoid is attached to.
   * @param channel      The channel on the PCM to control (0..7).
   */
  public Solenoid(final int moduleNumber, final int channel) {
    super(moduleNumber);
    m_channel = channel;

    SensorUtil.checkSolenoidModule(m_moduleNumber);
    SensorUtil.checkSolenoidChannel(m_channel);


    //HAL.report(tResourceType.kResourceType_Solenoid, m_channel, m_moduleNumber);
    setName("Solenoid", m_moduleNumber, m_channel);
    RobotEmulator.getInstance().manage(this);
  }
  private List<RobotSolenoidObserver> observers = new ArrayList<RobotSolenoidObserver>();
  void addObserver(RobotSolenoidObserver o) {
      observers.add(o);
  }

  @Override
  public void close() {
    super.close();
  }

  /**
   * Set the value of a solenoid.
   *
   * @param on True will turn the solenoid output on. False will turn the solenoid output off.
   */
  public void set(boolean on) {
    //SolenoidJNI.setSolenoid(m_solenoidHandle, on);
      for (RobotSolenoidObserver o : observers) {
	  o.didSet(this, on);
      }
  }

  /**
   * Read the current value of the solenoid.
   *
   * @return True if the solenoid output is on or false if the solenoid output is off.
   */
  public boolean get() {
    return false; //SolenoidJNI.getSolenoid(m_solenoidHandle);
  }

  /**
   * Check if solenoid is blacklisted. If a solenoid is shorted, it is added to the blacklist and
   * disabled until power cycle, or until faults are cleared.
   *
   * @return If solenoid is disabled due to short.
   * @see #clearAllPCMStickyFaults()
   */
  public boolean isBlackListed() {
    int value = getPCMSolenoidBlackList() & (1 << m_channel);
    return value != 0;
  }

  /**
   * Set the pulse duration in the PCM. This is used in conjunction with
   * the startPulse method to allow the PCM to control the timing of a pulse.
   * The timing can be controlled in 0.01 second increments.
   *
   * @param durationSeconds The duration of the pulse, from 0.01 to 2.55 seconds.
   *
   * @see #startPulse()
   */
  public void setPulseDuration(double durationSeconds) {
    long durationMS = (long) (durationSeconds * 1000);
    //SolenoidJNI.setOneShotDuration(m_solenoidHandle, durationMS);
  }

  /**
   * Trigger the PCM to generate a pulse of the duration set in
   * setPulseDuration.
   *
   * @see #setPulseDuration(double)
   */
  public void startPulse() {
    //SolenoidJNI.fireOneShot(m_solenoidHandle);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Solenoid");
    builder.setActuator(true);
    builder.setSafeState(() -> set(false));
    builder.addBooleanProperty("Value", this::get, this::set);
  }
}
