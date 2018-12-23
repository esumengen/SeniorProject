var landType = argument[0]

if (landType == resource_lumber)
	return c_green
else if (landType == resource_wool)
	return c_lime
else if (landType == resource_grain)
	return c_fields
else if (landType == resource_brick)
	return c_brick
else if (landType == resource_ore)
	return c_gray
else if (landType == resource_undefined)
	return make_color_rgb(30, 30, 110)

return c_desert