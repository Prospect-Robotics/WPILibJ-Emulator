/*----------------------------------------------------------------------------*/
/* Copyright (c) 2014-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Class for getting voltage, current, temperature, power and energy from the Power Distribution
 * Panel over CAN.
 */
public class PowerDistributionPanel extends SendableBase  {
  private final int m_handle;

  /**
   * Constructor.
   *
   * @param module The CAN ID of the PDP
   */
  public PowerDistributionPanel(int module) {
    SensorUtil.checkPDPModule(module);
    m_handle = 0; //PDPJNI.initializePDP(module);

    //HAL.report(tResourceType.kResourceType_PDP, module);
    setName("PowerDistributionPanel", module);
  }

  /**
   * Constructor.  Uses the default CAN ID (0).
   */
  public PowerDistributionPanel() {
    this(0);
  }

  /**
   * Query the input voltage of the PDP.
   *
   * @return The voltage of the PDP in volts
   */
  public double getVoltage() {
    return 12.5; //PDPJNI.getPDPVoltage(m_handle);
  }

  /**
   * Query the temperature of the PDP.
   *
   * @return The temperature of the PDP in degrees Celsius
   */
  public double getTemperature() {
    return 40; //PDPJNI.getPDPTemperature(m_handle);
  }

  /**
   * Query the current of a single channel of the PDP.
   *
   * @return The current of one of the PDP channels (channels 0-15) in Amperes
   */
  public double getCurrent(int channel) {
    double current = 1; //PDPJNI.getPDPChannelCurrent((byte) channel, m_handle);

    SensorUtil.checkPDPChannel(channel);

    return current;
  }

  /**
   * Query the current of all monitored PDP channels (0-15).
   *
   * @return The current of all the channels in Amperes
   */
  public double getTotalCurrent() {
    return 12; //PDPJNI.getPDPTotalCurrent(m_handle);
  }

  /**
   * Query the total power drawn from the monitored PDP channels.
   *
   * @return the total power in Watts
   */
  public double getTotalPower() {
    return 40; //PDPJNI.getPDPTotalPower(m_handle);
  }

  /**
   * Query the total energy drawn from the monitored PDP channels.
   *
   * @return the total energy in Joules
   */
  public double getTotalEnergy() {
    return 50; //PDPJNI.getPDPTotalEnergy(m_handle);
  }

  /**
   * Reset the total energy to 0.
   */
  public void resetTotalEnergy() {
    //PDPJNI.resetPDPTotalEnergy(m_handle);
  }

  /**
   * Clear all PDP sticky faults.
   */
  public void clearStickyFaults() {
    //PDPJNI.clearPDPStickyFaults(m_handle);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("PowerDistributionPanel");
    for (int i = 0; i < SensorUtil.kPDPChannels; ++i) {
      final int chan = i;
      builder.addDoubleProperty("Chan" + i, () -> getCurrent(chan), null);
    }
    builder.addDoubleProperty("Voltage", this::getVoltage, null);
    builder.addDoubleProperty("TotalCurrent", this::getTotalCurrent, null);
  }
}
