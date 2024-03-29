/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;


/**
 * Contains functions for roboRIO functionality.
 */
public final class RobotController {
  private RobotController() {
    throw new UnsupportedOperationException("This is a utility class!");
  }

  /**
   * Return the FPGA Version number. For now, expect this to be the current
   * year.
   *
   * @return FPGA Version number.
   */
  @SuppressWarnings("AbbreviationAsWordInName")
  public static int getFPGAVersion() {
    return 2019; //HALUtil.getFPGAVersion();
  }

  /**
   * Return the FPGA Revision number. The format of the revision is 3 numbers. The 12 most
   * significant bits are the Major Revision. the next 8 bits are the Minor Revision. The 12 least
   * significant bits are the Build Number.
   *
   * @return FPGA Revision number.
   */
  @SuppressWarnings("AbbreviationAsWordInName")
  public static long getFPGARevision() {
    return (long) 0x11122001; //HALUtil.getFPGARevision();
  }

  private static long baseFPGATime = System.nanoTime();
  /**
   * Read the microsecond timer from the FPGA.
   *
   * @return The current time in microseconds according to the FPGA.
   */
  public static long getFPGATime() {
    return (System.nanoTime() - baseFPGATime) / 1000; //HALUtil.getFPGATime();
  }

  /**
   * Get the state of the "USER" button on the roboRIO.
   *
   * @return true if the button is currently pressed down
   */
  public static boolean getUserButton() {
    return false; //HALUtil.getFPGAButton();
  }

  /**
   * Read the battery voltage.
   *
   * @return The battery voltage in Volts.
   */
  public static double getBatteryVoltage() {
    return 12; //PowerJNI.getVinVoltage();
  }

  /**
   * Gets a value indicating whether the FPGA outputs are enabled. The outputs may be disabled if
   * the robot is disabled or e-stopped, the watchdog has expired, or if the roboRIO browns out.
   *
   * @return True if the FPGA outputs are enabled.
   */
  public static boolean isSysActive() {
    return true; //HAL.getSystemActive();
  }

  /**
   * Check if the system is browned out.
   *
   * @return True if the system is browned out
   */
  public static boolean isBrownedOut() {
    return false; //HAL.getBrownedOut();
  }

  /**
   * Get the input voltage to the robot controller.
   *
   * @return The controller input voltage value in Volts
   */
  public static double getInputVoltage() {
    return 12.0; //PowerJNI.getVinVoltage();
  }

  /**
   * Get the input current to the robot controller.
   *
   * @return The controller input current value in Amps
   */
  public static double getInputCurrent() {
    return 2; //PowerJNI.getVinCurrent();
  }

  /**
   * Get the voltage of the 3.3V rail.
   *
   * @return The controller 3.3V rail voltage value in Volts
   */
  public static double getVoltage3V3() {
    return 3.3; //PowerJNI.getUserVoltage3V3();
  }

  /**
   * Get the current output of the 3.3V rail.
   *
   * @return The controller 3.3V rail output current value in Volts
   */
  public static double getCurrent3V3() {
    return 2.0; //PowerJNI.getUserCurrent3V3();
  }

  /**
   * Get the enabled state of the 3.3V rail. The rail may be disabled due to a controller brownout,
   * a short circuit on the rail, or controller over-voltage.
   *
   * @return The controller 3.3V rail enabled value
   */
  public static boolean getEnabled3V3() {
    return true; //PowerJNI.getUserActive3V3();
  }

  /**
   * Get the count of the total current faults on the 3.3V rail since the controller has booted.
   *
   * @return The number of faults
   */
  public static int getFaultCount3V3() {
    return 0; //PowerJNI.getUserCurrentFaults3V3();
  }

  /**
   * Get the voltage of the 5V rail.
   *
   * @return The controller 5V rail voltage value in Volts
   */
  public static double getVoltage5V() {
    return 4.95; //PowerJNI.getUserVoltage5V();
  }

  /**
   * Get the current output of the 5V rail.
   *
   * @return The controller 5V rail output current value in Amps
   */
  public static double getCurrent5V() {
    return 3.5; //PowerJNI.getUserCurrent5V();
  }

  /**
   * Get the enabled state of the 5V rail. The rail may be disabled due to a controller brownout, a
   * short circuit on the rail, or controller over-voltage.
   *
   * @return The controller 5V rail enabled value
   */
  public static boolean getEnabled5V() {
    return true; //PowerJNI.getUserActive5V();
  }

  /**
   * Get the count of the total current faults on the 5V rail since the controller has booted.
   *
   * @return The number of faults
   */
  public static int getFaultCount5V() {
    return 0; //PowerJNI.getUserCurrentFaults5V();
  }

  /**
   * Get the voltage of the 6V rail.
   *
   * @return The controller 6V rail voltage value in Volts
   */
  public static double getVoltage6V() {
    return 6; //PowerJNI.getUserVoltage6V();
  }

  /**
   * Get the current output of the 6V rail.
   *
   * @return The controller 6V rail output current value in Amps
   */
  public static double getCurrent6V() {
    return 0.1; //PowerJNI.getUserCurrent6V();
  }

  /**
   * Get the enabled state of the 6V rail. The rail may be disabled due to a controller brownout, a
   * short circuit on the rail, or controller over-voltage.
   *
   * @return The controller 6V rail enabled value
   */
  public static boolean getEnabled6V() {
    return true; //PowerJNI.getUserActive6V();
  }

  /**
   * Get the count of the total current faults on the 6V rail since the controller has booted.
   *
   * @return The number of faults
   */
  public static int getFaultCount6V() {
    return 0; //PowerJNI.getUserCurrentFaults6V();
  }

  /**
   * Get the current status of the CAN bus.
   *
   * @return The status of the CAN bus
   */
//  public static CANStatus getCANStatus() {
//    CANStatus status = new CANStatus();
//    CANJNI.GetCANStatus(status);
//    return status;
//  }
}
