var landType = argument[0]

if (landType == ltype_forest)
	return "Forest"
else if (landType == ltype_pasture)
	return "Pasture"
else if (landType == ltype_fields)
	return "Fields"
else if (landType == ltype_hills)
	return "Hills"
else if (landType == ltype_mountains)
	return "Mountains"

return "Desert"