var dw = display_get_gui_width()
var dh = display_get_gui_height()

if (can_dice()) {
	draw_set_alpha(0.7)
		draw_roundrect(dw/2-70, dh+50, dw/2+70, dh+90, 0)
	draw_set_alpha(1)
	
	draw_set_valign(fa_center) draw_set_halign(fa_center) draw_set_color(c_white)
		draw_text(dw/2, dh+70, "[D] Dice")
	draw_set_valign(fa_top) draw_set_halign(fa_left) draw_set_color(c_black)
}

draw_set_halign(fa_right) draw_set_color(c_white)
	draw_text(dw-100, 20, "P"+string(global.player_active)+"'s turn")
	
	if (is_turn_ready())
		draw_text(dw-100, 40, "Ready")
draw_set_halign(fa_left) draw_set_color(c_black)