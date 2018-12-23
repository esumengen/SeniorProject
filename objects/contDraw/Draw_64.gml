var dw = display_get_gui_width()
var dh = display_get_gui_height()

if (global.player_active == human and can_dice()) {
	draw_set_alpha(0.7)
		draw_roundrect(dw/2-70, 80, dw/2+70, 120, 0)
	draw_set_alpha(1)
	
	draw_set_valign(fa_center) draw_set_halign(fa_center) draw_set_color(c_white)
		draw_text(dw/2, 100, "[D] Dice")
	draw_set_valign(fa_top) draw_set_halign(fa_left) draw_set_color(c_default)
}

draw_set_halign(fa_right) 
	draw_set_alpha(0.5) draw_set_color(c_black)
		draw_roundrect(-10, -10, dw+10, 70, 0)
	draw_set_alpha(1) draw_set_color(c_default)
	
	for (var i = 1; i <= PLAYER_COUNT; i++) {
		var offset_x = 1/2*dw/PLAYER_COUNT+(i-1)*dw/PLAYER_COUNT
		
		/*draw_set_valign(fa_center) draw_set_halign(fa_center) */draw_set_color(i == global.player_active ? c_aqua : c_white)
			draw_text(offset_x-30, 15, "Player "+string(i))
			draw_text(offset_x-30, 55, "Score "+string(global.playerScore[i]))
		/*draw_set_valign(fa_top) draw_set_halign(fa_left) */draw_set_color(c_default)
	}
	
	draw_roundrect(-10, 68, dw+10, 72, 0)
draw_set_halign(fa_left) draw_set_color(c_black)