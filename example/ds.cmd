# -*- mode: text -*-
# joystick.config <instance number> <num_axis> <num_buttons> <num_pov>
joystick.config 0 3 12 1   # <instance number> <num_axis> <num_buttons> <num_pov>
joystick.config 1 3 14 1
disable
# autonomous: 0 -> Not Autonomous, non-zero -> Atonomous
autonomous 0

# All commands before first wait command are executed before robot
# constructor runs.

uwait 1000000 # uwait <micro-seconds> until taking next action.
enable

mwait 2000    # mwait <milli-seconds> until taking next action.
joystick.axis 1 1 0.5

mwait 2000
joystick.axis 1 1 0

mwait 1000
joystick.button 1 4 press  # Front Pistons button.
mwait 500
joystick.button 1 4 release

mwait 10000
disable

mwait 1000
powerdown
