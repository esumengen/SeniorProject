if (drawLocations) {
	with (objLocation)
		if (active)
			draw_self()
}

if (surface_exists(surfaceSea)) {
	var angle = current_time/50 mod 360
	var dis = 7+contMain.period/4
	
	var cut_y = current_time/10 mod room_height

	draw_surface_general(surfaceSea, 0, cut_y, room_width, 40, 0, cut_y, 1, 1, 0, c_aqua, c_aqua, c_aqua, c_aqua, 0.3)
}
else {
	surfaceSea = surface_create(room_width, room_height)
	event_user(0)
}

gpu_set_tex_filter(0)