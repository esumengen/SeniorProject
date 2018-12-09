// @desc Add to the Nearest Location

nearestLocation[0] = instance_nearest(x, y, objLocation)
nearestLocation[1] = instance_nth_nearest(x, y, objLocation, 2)

var condition = structure_count(anyone, objSettlement, nearestLocation[0]) > 0 or structure_count(anyone, objSettlement, nearestLocation[1]) > 0
or structure_count(anyone, objRoad, nearestLocation[0]) > 0 or structure_count(anyone, objRoad, nearestLocation[1]) > 0 and nearestLocation[0].active and nearestLocation[1].active

if (condition and nearestLocation[0] != nearestLocation[1]
and point_distance(mouse_x, mouse_y, nearestLocation[0].x, nearestLocation[0].y) < 120
and point_distance(nearestLocation[0].x, nearestLocation[0].y, nearestLocation[1].x, nearestLocation[1].y) < sprite_width+25) {
	x = (nearestLocation[0].x+nearestLocation[1].x)/2
	y = (nearestLocation[0].y+nearestLocation[1].y)/2
		
	image_angle = point_direction(nearestLocation[0].x, nearestLocation[0].y, nearestLocation[1].x, nearestLocation[1].y)
		
	for (var i = 0; i < 360; i += 30) {
		if (abs(angle_difference(image_angle, i)) < 2) {
			image_angle = i
			break
		}
	}
		
	ds_list_add(nearestLocation[0].structures, id)
	ds_list_add(nearestLocation[1].structures, id)
	location[0] = nearestLocation[0]
	location[1] = nearestLocation[0]
		
	startX = x
	startY = y
		
	action_write(global.player, action_create, location[0].index, location[1].index, actionObject_road)
}
else
	instance_destroy()