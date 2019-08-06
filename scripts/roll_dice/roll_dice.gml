var dice
dice[0] = argument[0]
dice[1] = argument[1]

global.dice[0] = dice[0]
global.dice[1] = dice[1]

global.diceTotal = dice[0]+dice[1]
global.isDiceRolled = true

with (objLand) {
	var landType = type
	
	if (diceNo == global.diceTotal and global.robberLand != id) {
		for (var i = 0; i < ds_list_size(adjacentLocations); i++) {
			with (ds_list_find_value(adjacentLocations, i)) {
				for (var j = 1; j <= PLAYER_COUNT; j++) {
					var settlementCount = structure_count(j, objSettlement, id)
					var cityCount = structure_count(j, objCity, id)

					if (settlementCount+cityCount > 0) {
						var resource = get_resource(landType)
						
						if (resource != resource_undefined) {
							var totalGain = settlementCount+cityCount*2
							
							change_resource(j, resource, totalGain)
						}
					}
				}
			}
		}
	}
}

if (global.actionWriting_mode)
	action_write(global.player_active, action_roll, dice[0], dice[1], actionObject_nothing)