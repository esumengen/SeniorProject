var mouseOn = is_mouse_on()
var robberMode = global.robberAddition_mode
var isSea = type == ltype_sea
var isDesert = type == ltype_desert

#region Draw self
if (isSea)
	gpu_set_texfilter(false)
else 
	draw_sprite_ext(sprite_index, 0, x, y, 1.05, 1.05, 0, c_black, 1)
	
draw_sprite_ext(sprite_index, isSea*2, x, y, image_xscale, image_yscale, 0, image_blend, image_alpha)

if (!isSea and !mouseOn)
	draw_sprite_ext(sprite_index, 0, x, y, image_xscale, image_yscale, 0, c_black, image_alpha*0.4)

if (isSea)
	gpu_set_texfilter(true)
#endregion
	
if (!isDesert and !isSea) {
	draw_set_valign(fa_center) draw_set_halign(fa_center) draw_set_alpha(0.5)
		draw_set_color(c_white)
			draw_circle(x-robberMode*20, y, 20, 0)
	
		draw_set_color(c_black)
			draw_text(x-robberMode*20, y, diceNo)
	draw_set_valign(fa_top) draw_set_halign(fa_left) draw_set_alpha(1)
}

if (robberMode and !isSea) {
	draw_sprite_ext(sprLocation, -1, x+35, y, 1, 1, 0, c_black, 0.7)
	draw_sprite_ext(sprLocation, -1, x+35, y, 1+contMain.period/50, 1+contMain.period/50, 0, c_purple, 0.7)
}

if (id == global.robberLand)
	draw_sprite_ext(sprRobber, -1, x+35, y, 1, 1, 0, c_white, 1)