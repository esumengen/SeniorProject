if (condition and mouseHold and nearestLocation[0] != pointer_null and nearestLocation[1] != pointer_null) {
	var middle_x = (nearestLocation[0].x+nearestLocation[1].x)/2
	var middle_y = (nearestLocation[0].y+nearestLocation[1].y)/2
	var angle = point_direction(nearestLocation[0].x, nearestLocation[0].y, nearestLocation[1].x, nearestLocation[1].y)
	
	draw_sprite_ext(sprite_index, -1, middle_x, middle_y, 1, 1, angle, c_aqua, 0.4)
}

draw_self()