# -*- mode: text -*-
# joystick.config <instance number> <num_axis> <num_buttons> <num_pov>
joystick.config 0 3 12 1   # <instance number> <num_axis> <num_buttons> <num_pov>
joystick.config 1 3 14 1
disable
# autonomous: 0 -> Not Autonomous, non-zero -> Atonomous
autonomous 1

# All commands before first wait command are executed before robot
# constructor runs.

mwait 1000    # mwait <milli-seconds> until taking next action.
enable

mwait 500
disable

mwait 500
powerdown
