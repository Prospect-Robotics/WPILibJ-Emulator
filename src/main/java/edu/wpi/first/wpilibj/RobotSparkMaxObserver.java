package edu.wpi.first.wpilibj;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public interface RobotSparkMaxObserver {
    public void didSetpoint( float value, int ctrlType,
	    int pidSlot, float arbFeedforward, int arbFFUnits);
}
