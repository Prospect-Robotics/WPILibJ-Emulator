# WPILibJ-Emulator

A simple emulator to facilitate runtime debugging of WPILibJ based
robot programs.

## How to use it

There is now a gradle script to facilitate building.  You should be
able to do:
```
./gradlew assemble
```
To produce the jar file.

Use that jar file as the classpath for your robot code instead of all
the wpilibj com.ctre, etc. libraries.

The emulator is controlled by several environment variables.  This
allows you to control the emulator without having to modify your robot
code to handle command line arguments.

```
ROBOT_EMU_CMD_FILE  -- This controls the emulator, see the examples directory
ROBOT_LOOP_LOG      -- A timing log file (not sure how useful this is)
```

Note the the robot code and runtime have a hard coded directory path
of /home/lvuser, this directory must exist to be able to run the code.
For Windows users, we may have to adjust the code if this directory
proves problematical.
