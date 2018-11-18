if (condition and mouseHold and nearestLocation != pointer_null)
	draw_sprite_ext(sprite_index, -1, nearestLocation.x, nearestLocation.y, 1, 1, 0, c_aqua, 0.4)

draw_sprite_ext(sprite_index, -1, x, y, image_xscale+0.15, image_yscale+0.15, image_angle, c_black, 1)
draw_self()