/// @param playerIndex
/// @param location

var isUpgraded = false

if (structure_count(argument[0], objSettlement, argument[1]) == 1) {
	with (argument[1]) {
		for (var i = 0; i < ds_list_size(structures); i++) {
			var structure = ds_list_find_value(structures, i)
			
			if (structure.object_index == objSettlement) {
				instance_destroy(structure)
				break
			}
		}
	}
		
	isUpgraded = create(argument[0], objCity, argument[1])
}

if (isUpgraded) {
	if (global.actionWriting_mode)
		action_write(argument[0], action_upgrade, argument[1].index, actionObject_settlement)
}

return isUpgraded