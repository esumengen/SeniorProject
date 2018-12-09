/// @desc Add to the Nearest Location

event_perform(ev_other, ev_user9)

if (condition and point_distance(mouse_x, mouse_y, nearestLocation.x, nearestLocation.y) < 120) {
	x = nearestLocation.x
	y = nearestLocation.y
		
	ds_list_add(nearestLocation.structures, id)
	location = nearestLocation
		
	startX = x
	startY = y
		
	action_write(global.player, action_create, location.index, actionObject_settlement)
}
else
	instance_destroy()