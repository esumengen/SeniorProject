var landType = argument[0]

if (landType == ltype_forest)
	return c_green
else if (landType == ltype_pasture)
	return c_lime
else if (landType == ltype_fields)
	return c_fields
else if (landType == ltype_hills)
	return c_brick
else if (landType == ltype_mountains)
	return c_gray
else if (landType == ltype_sea)
	return make_color_rgb(0, 0, 160)

return c_desert