# -*- mode: text -*-
# joystick.config <instance number> <num_axis> <num_buttons> <num_pov>
joystick.config 0 3 12 1   # <instance number> <num_axis> <num_buttons> <num_pov>
joystick.config 1 3 14 1
disable
# autonomous: 0 -> Not Autonomous, non-zero -> Atonomous
autonomous 0
wait 2000000
enable
wait 10000000
disable
