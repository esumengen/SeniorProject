for (var i = 0; i < ds_list_size(global.locations); i++) {
	var location = ds_list_find_value(global.locations, i)
	
	var index = ds_list_find_index(location.structures, id)
	
	if (index != -1)
		ds_list_delete(location.structures, ds_list_find_index(location.structures, id))
}