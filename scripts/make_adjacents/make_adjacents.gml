/// @param location[0]
/// @param location[1]

if (ds_list_find_index(argument[0].adjacentLocations, argument[1]) == -1)
	ds_list_add(argument[0].adjacentLocations, argument[1])
	
if (ds_list_find_index(argument[1].adjacentLocations, argument[0]) == -1)
	ds_list_add(argument[1].adjacentLocations, argument[0])