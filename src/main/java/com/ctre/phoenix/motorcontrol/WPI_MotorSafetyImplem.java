package com.ctre.phoenix.motorcontrol;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.MotorSafety;

/**
 * implem of MotorSafety interface in WPI. This also allows late/lazy
 * construction of WPI's motor safety object (which mitigates late-released bugs from WPI).
 */
public class WPI_MotorSafetyImplem extends MotorSafety {

	private SpeedController _speedController = null;
	private String _description = null;

	/**
	 * Constructor for WPI_MotorSafetyImplem
	 * @param speedController Speed Controller to implement motor safety on
	 * @param description Description of speed controller
	 */
	public WPI_MotorSafetyImplem(SpeedController speedController, String description) {
		_speedController = speedController;
		_description = description;
	}

	/**
	 * Stop the controller
	 */
	public void stopMotor() { _speedController.stopMotor(); }

	/**
	 * @return Description of speed controller
	 */
	public String getDescription() { return _description; }
}
