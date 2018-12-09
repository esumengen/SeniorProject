if (global.robberAddition_mode) {
	var nearestLand = instance_nearest(mouse_x, mouse_y, objLand)
	
	if (nearestLand.type != ltype_sea) {
		global.robberLand = nearestLand
		global.robberAddition_mode = false
		
		action_write(global.player, action_move, nearestLand.index, actionObject_robber)
	}
}

if (global.initialPhase) {
	if (global.addStructure_mode) {
		if (global.addStructure_object == actionObject_settlement) {
			var settlement = instance_create_layer(-500, -500, "lyBuilding", objSettlement)
			settlement.x = mouse_x
			settlement.y = mouse_y
			settlement.playerIndex = global.player_active
		
			with (settlement)
				event_user(8)
		
			if (instance_exists(settlement)) {
				global.addStructure_object = actionObject_road
			}
		}
		else if (global.addStructure_object == actionObject_road) {
			var road = instance_create_layer(-500, -500, "lyRoad", objRoad)
			road.x = mouse_x
			road.y = mouse_y
			road.playerIndex = global.player_active
		
			with (road)
				event_user(8)
		
			if (instance_exists(road)) {
				global.addStructure_mode = false
			}
		}
	}
}