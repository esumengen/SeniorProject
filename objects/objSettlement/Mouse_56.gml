if (mouseHold) {
	mouseHold = false

	event_perform(ev_other, ev_user9)

	if (condition and point_distance(mouse_x, mouse_y, nearestLocation.x, nearestLocation.y) < 120) {
		x = nearestLocation.x
		y = nearestLocation.y
		
		ds_list_add(nearestLocation.structures, id)
		location = nearestLocation
		
		startX = x
		startY = y
	}
	else
		instance_destroy()
}

event_inherited()