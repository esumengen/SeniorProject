var mouseOn = is_mouse_on()

draw_sprite_ext(sprite_index, 0, x, y, 1.05, 1.05, 0, c_black, 1)
draw_sprite_ext(sprite_index, 0, x, y, 1, 1, 0, image_blend, 1)

if (!mouseOn)
	draw_sprite_ext(sprite_index, 0, x, y, 1, 1, 0, c_black, 0.3)
	
draw_set_valign(fa_center) draw_set_halign(fa_center)
	draw_text(x, y, index)
draw_set_valign(fa_top) draw_set_halign(fa_left)