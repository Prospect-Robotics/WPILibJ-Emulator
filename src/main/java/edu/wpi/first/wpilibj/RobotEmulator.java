package edu.wpi.first.wpilibj;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class RobotEmulator {
    private Object m_logLock = new Object();
    
    private void emitLogTimestamp()
    {
	long now = RobotController.getFPGATime();
	long s = now / 1_000_000;
	long f = now % 1_000_000;
	m_logWriter.printf("%d.%06d: ", s, f);
    }
    
    private class LoggingSolenoidObserver implements RobotSolenoidObserver
    {
	@Override
	public void didSet(Solenoid s, boolean v) {
	    synchronized (m_logLock) {
		if (m_logWriter == null)
		    return;
		emitLogTimestamp();
		m_logWriter.printf("Thread(%s)  Solenoid: %s: %s\n",
			Thread.currentThread().getName(), s.getName(),
			(v ? "on" : "off"));
	    }

	}

    }
    
    private class LoggingMotorObserver implements RobotMotorObserver
    {
	@Override
	public void didSet(BaseMotorController c, ControlMode cm, double dmd, DemandType dt, double dmd1) {
	    synchronized (m_logLock) {
		if (m_logWriter == null)
		    return;
		emitLogTimestamp();
		m_logWriter.printf("Thread(%s) Motor: %x: (%s) %f, (%s) %f\n",
			Thread.currentThread().getName(), c.getBaseID(), cm.toString(), dmd, dt.toString(), dmd1);
	    }
	}
	
    }
    
    void logCommand(String cmd) {
	synchronized (m_logLock) {
	    if (m_logWriter == null)
		return;
	    emitLogTimestamp();
	    m_logWriter.println("Command <" + cmd + ">");
	}

    }

    void logNotifier(String note) {
	synchronized (m_logLock) {
	    if (m_logWriter == null)
		return;
	    emitLogTimestamp();
	    m_logWriter.printf("Thread(%s) Notifier: %s\n", Thread.currentThread().getName(), note);
	}

    }

    PrintWriter m_logWriter;

    private RobotEmulator() {
	String log_file_name = System.getenv().get("ROBOT_ACTION_LOG");
	if (log_file_name == null)
	    log_file_name = "robot_action.log";
	try {
	    m_logWriter = new PrintWriter(new FileWriter(log_file_name));
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    System.exit(1);
	}
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    try {
		m_logWriter.flush();
		m_logWriter.close();
	    } catch (Exception ex) {
		// Ignore.
	    }
	}));

    }

    private static RobotEmulator instance = new RobotEmulator();
    
    public static RobotEmulator getInstance()
    {
	return instance;
    }
    
    public void manage(Solenoid s) {
	s.addObserver(new LoggingSolenoidObserver());
    }
    
    public void manage (BaseMotorController bmc)
    {
	bmc.addObserver(new LoggingMotorObserver());
    }
}
    
