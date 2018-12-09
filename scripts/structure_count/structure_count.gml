/// @param playerIndex
/// @param structure
/// @param location*

var count = 0

with (argument_count == 3 ? argument[2] : objLocation) {
	for (var i = 0; i < ds_list_size(structures); i++) {
		var structure = ds_list_find_value(structures, i)
	
		if (argument[0] == anyone and structure.object_index = argument[1]) {
			count++;
		}
	}
}

return count