var dis = point_distance(x, y, mouse_x, mouse_y)

draw_sprite_ext(sprite_index, -1, x, y, 1, 1, 0, c_black, 0.5)

draw_set_valign(fa_center) draw_set_halign(fa_center) draw_set_color(c_red)
	draw_text(x, y, index)
draw_set_valign(fa_top) draw_set_halign(fa_left) draw_set_color(c_black)

if (dis < 150)
	draw_sprite_ext(sprite_index, -1, x, y, 1, 1, 0, c_white, 0.5)