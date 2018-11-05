var landType = argument[0]

if (landType == ltype_forest) {
	return c_green
}
else if (landType == ltype_pasture) {
	return c_lime
}
else if (landType == ltype_fields) {
	return c_fields
}
else if (landType == ltype_hills) {
	return c_brick
}
else if (landType == ltype_mountains) {
	return c_dkgray
}
else if (landType == ltype_desert) {
	return c_desert
}