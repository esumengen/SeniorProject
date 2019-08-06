var offset_y = -17-contMain.period*1.6

if (keyboard_check(vk_space) and point_distance(x, y, mouse_x, mouse_y) < 50) {
	draw_set_halign(fa_center) draw_set_valign(fa_center) draw_set_font(fontMain_small)
		draw_set_color(c_white) draw_set_alpha(0.5)
			draw_roundrect(screen_point(x, 0)-15, screen_point(y, 1)-11+offset_y, screen_point(x, 0)+15, screen_point(y, 1)+11+offset_y, 0)
		draw_set_color(c_black) draw_set_alpha(1)
		
			draw_text(screen_point(x, 0), screen_point(y, 1)+offset_y, "[P"+string(playerIndex)+"]")
	draw_set_halign(fa_left) draw_set_valign(fa_top) draw_set_font(fontMain)
}