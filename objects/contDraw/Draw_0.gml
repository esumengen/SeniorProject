if (drawLocations) {
	with (objLocation)
		if (isActive and !place_meeting(x, y, parBuilding))
			draw_sprite_ext(sprite_index, -1, x, y, 1+contMain.period/140, 1+contMain.period/140, image_angle, image_blend, image_alpha*0.9)
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

var px = get_player_position(false, global.player_active)
var py = get_player_position(true, global.player_active)

//var size = 1+contMain.period/80
//draw_sprite_ext(sprTurnLine, 0, px, py, 1, 1, -global.player_active*90+90, c_white, 1)

gpu_set_tex_filter(1)

with (objLand) {
	if (id == global.robberLand)
		draw_sprite_ext(sprRobber, -1, x+35, y, 1, 1, 0, c_white, 1)
}

draw_set_font(fontMain_large) draw_set_valign(fa_center) draw_set_halign(fa_center)
	for (var i = 1; i <= PLAYER_COUNT; i++) {
		var unit_angle = 360/PLAYER_COUNT
	
		var px = get_player_position(false, i)
		var py = get_player_position(true, i)
	
		for (var j = 0; j < ds_grid_width(global.resources); j++) {
			var count = 0
			repeat (ds_grid_get(global.resources, i, j)) {
				var l1 = count*6+j*130-260
				var l2 = count*13+100
			
				draw_sprite_ext(sprResourceCard, j, 
				px+lengthdir_x(l1, unit_angle-i*unit_angle)-lengthdir_y(l2, unit_angle-i*unit_angle), 
				py+lengthdir_x(l2, unit_angle-i*unit_angle)+lengthdir_y(l1, unit_angle-i*unit_angle),
				1, 1, -i*unit_angle+90, get_resource_color(j), 1)
			
				draw_text(
				px+lengthdir_x(l1, unit_angle-i*unit_angle)-lengthdir_y(l2, unit_angle-i*unit_angle),
				py+lengthdir_x(l2, unit_angle-i*unit_angle)+lengthdir_y(l1, unit_angle-i*unit_angle),
				ds_grid_get(global.resources, i, j))

				count += 1
			}
		}
	}
draw_set_font(fontMain) draw_set_valign(fa_top) draw_set_halign(fa_left)

with (objLocation) {
	if (keyboard_check(vk_space) and isActive) {
		draw_set_alpha(0.8)
			draw_circle(x, y, 10, 0)
		draw_set_color(c_aqua) draw_set_font(fontMain_small) draw_set_halign(fa_center) draw_set_valign(fa_center)
			draw_text(x, y, index)
		draw_set_color(c_black) draw_set_font(fontMain) draw_set_halign(fa_left) draw_set_valign(fa_top)
	}
}