/// @desc Draw Surfaces

surface_set_target(surfaceSea) gpu_set_tex_filter(false)
	with (objLand) {
		if (type = ltype_sea)
			draw_sprite_ext(sprite_index, 2, x, y, 1.17, 1.17, 0, c_white, 1)
	}
surface_reset_target() gpu_set_tex_filter(true)