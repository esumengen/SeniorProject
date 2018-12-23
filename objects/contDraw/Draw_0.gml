if (drawLocations) {
	with (objLocation)
		if (isActive)
			draw_self()
}

if (surface_exists(surfaceSea)) {
	var angle = current_time/50 mod 360
	var dis = 7+contMain.period/4

	//draw_surface_general(surfaceSea, 0, 0, room_width, room_height, 0, 0, 1, 1, 0, c_aqua, c_aqua, c_aqua, c_aqua, abs(0.5-(current_time mod 2000)/2000)/3)
}
else {
	surfaceSea = surface_create(room_width, room_height)
	event_user(0)
}

gpu_set_tex_filter(0)

var px = get_player_position(false, global.player_active)
var py = get_player_position(true, global.player_active)

draw_sprite_ext(sprTurnLine, -1, px, py, 5, 1, -global.player_active*90+90, c_white, 0.3)

for (var i = 1; i <= PLAYER_COUNT; i++) {
	var unit_angle = 360/PLAYER_COUNT
	
	var px = get_player_position(false, i)
	var py = get_player_position(true, i)
	
	for (var j = 0; j < ds_grid_width(global.resources); j++) {
		var count = 0
		repeat (ds_grid_get(global.resources, i, j)) {
			var l1 = count*6+j*130-260
			var l2 = count*13+100
			
			draw_sprite_ext(sprResourceCard, 0, 
			px+lengthdir_x(l1, unit_angle-i*unit_angle)-lengthdir_y(l2, unit_angle-i*unit_angle), 
			py+lengthdir_x(l2, unit_angle-i*unit_angle)+lengthdir_y(l1, unit_angle-i*unit_angle),
			1, 1, i*unit_angle-90, get_resource_color(j), 1)
			
			count += 1
		}
	}
}