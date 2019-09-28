package edu.wpi.first.wpilibj;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class RobotEmulator {
    private Object m_logLock = new Object();
    
    private class LoggingSolenoidObserver implements SolenoidObserver
    {
	@Override
	public void didSet(Solenoid s, boolean v) {
	    synchronized (m_logLock) {
		m_logWriter.printf("%13d: Thread: %s  Solonoid: %s: %s\n", RobotController.getFPGATime(),
			Thread.currentThread().getName(), s.getName(),
			(v ? "on" : "off"));
	    }

	}

    }
    
    void logCommand(String cmd) {
	synchronized (m_logLock) {
	    m_logWriter.printf("%13d: Command <%s>\n", RobotController.getFPGATime(), cmd);
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
    
    static RobotEmulator getInstance()
    {
	return instance;
    }
    
    void manage(Solenoid s) {
	s.addObserver(new LoggingSolenoidObserver());
    }
}
    
