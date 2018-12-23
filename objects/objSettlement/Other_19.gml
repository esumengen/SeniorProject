/// @desc Condition Check

var settlementCount = structure_count(anyone, objSettlement, location)
	
var isFree = true
for (var i = 0; i < ds_list_size(location.adjacentLocations); i++) {
	var _location = ds_list_find_value(location.adjacentLocations, i)
	
	if (structure_count(anyone, objSettlement, _location) > 0) {
		isFree = false
		break
	}
}
	
condition = settlementCount == 0 and location.isActive and isFree