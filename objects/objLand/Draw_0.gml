var mouseOn = is_mouse_on()

draw_sprite_ext(sprite_index, 0, x, y, 1.05, 1.05, 0, c_black, 1)
draw_sprite_ext(sprite_index, 0, x, y, 1, 1, 0, image_blend, image_alpha)

if (!mouseOn)
	draw_sprite_ext(sprite_index, 0, x, y, 1, 1, 0, c_black, 0.3)
	
if (type != ltype_desert) {
	draw_set_valign(fa_center) draw_set_halign(fa_center) draw_set_alpha(0.5)
		draw_set_color(c_white)
			draw_circle(x, y, 20, 0)
	
		draw_set_color(c_black)
			draw_text(x, y, diceNo)
	draw_set_valign(fa_top) draw_set_halign(fa_left) draw_set_alpha(1)
}