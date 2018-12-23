/// @param playerIndex
/// @param object
/// @param location[0]
/// @param location[1]*

var isCreated = false
if (argument[1] == objRoad) {
	var roadObject = instance_create_layer(-500, -500, "lyRoad", objRoad)
	
	with (roadObject) {
		location[0] = argument[2]
		location[1] = argument[3]
		
		image_angle = point_direction(location[0].x, location[0].y, location[1].x, location[1].y)
		
		for (var i = 0; i < 360; i += 30) {
			if (abs(angle_difference(image_angle, i)) < 2) {
				image_angle = i
				break
			}
		}
		
		x = (location[0].x+location[1].x)/2
		y = (location[0].y+location[1].y)/2
		
		set_owner(argument[0])
		
		event_user(9)
	}
	
	if (roadObject.condition) {
		isCreated = true
		
		ds_list_add(argument[2].structures, roadObject)
		ds_list_add(argument[3].structures, roadObject)
		
		if (global.actionWriting_mode)
			action_write(argument[0], action_create, argument[2].index, argument[3].index, actionObject_road)
	}
	else
		instance_destroy(roadObject)
}
else if (argument[1] == objSettlement) {
	var settlementObject = instance_create_layer(-500, -500, "lyBuilding", objSettlement)
	
	with (settlementObject) {
		location = argument[2]
		
		x = location.x
		y = location.y
		
		set_owner(argument[0])
		
		event_user(9)
	}
	
	if (settlementObject.condition)	{
		isCreated = true
		
		ds_list_add(argument[2].structures, settlementObject)
		
		if (global.initialPhase and structure_count(global.player_active, objSettlement) == 2) {
			with (settlementObject.location) {
				for (var i = 0; i < ds_list_size(adjacentLands); i++) {
					var land = ds_list_find_value(adjacentLands, i)
					
					add_resource(settlementObject.playerIndex, get_resource(land.type), 1)
				}
			}
		}
		
		if (global.actionWriting_mode)
			action_write(argument[0], action_create, argument[2].index, actionObject_settlement)
			
		global.playerScore[global.player_active] += 1
	}
	else
		instance_destroy(settlementObject)
}

return isCreated