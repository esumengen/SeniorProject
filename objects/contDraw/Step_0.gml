drawLocations = global.addStructure_mode

var mx_gui = device_mouse_x_to_gui(0)
var my_gui = device_mouse_y_to_gui(0)

mouseOn_dice = false
if (point_distance(mx_gui, my_gui, diceX, diceY) < 50)
	mouseOn_dice = true