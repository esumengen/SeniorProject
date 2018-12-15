/// @desc Condition Check

var settlementCount = structure_count(anyone, objSettlement, location)
	
var list = get_nearest_locations(location)

var isFree = true
for (var i = 0; i < ds_list_size(list); i++) {
	var _location = ds_list_find_value(list, i)
	
	if (structure_count(anyone, objSettlement, _location) > 0) {
		isFree = false
		break
	}
}

ds_list_destroy(list)
	
condition = settlementCount == 0 and location.active and isFree