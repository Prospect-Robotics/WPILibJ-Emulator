package edu.wpi.first.wpilibj;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public interface RobotMotorObserver {
    public void didSet(BaseMotorController c, ControlMode cm, double dmd, DemandType dt, double dmd1);
}
