#region Moves
if (global.player_active == human)
{
	
if (global.debugMode) {
}
else if (global.initialPhase) {
	if (is_holding(objSettlement)) {
		var nearestLocation = instance_nearest(mouse_x, mouse_y, objLocation)
		var isCreated = create(human, objSettlement, nearestLocation)
		
		if (isCreated) {
			leave_held()
			take(objRoad)
		}
	}
	else if (is_holding(objRoad)) {
		var nearestLocation
		nearestLocation[0] = instance_nearest(mouse_x, mouse_y, objLocation)
		nearestLocation[1] = instance_nth_nearest(mouse_x, mouse_y, objLocation, 2)

		var isCreated = create(human, objRoad, nearestLocation[0], nearestLocation[1])
		
		if (isCreated) {
			leave_held()
				
			turn_end()
		}
	}
}
else if (global.robberAddition_mode) {
	var nearestLand = instance_nearest(mouse_x, mouse_y, objLand)
	
	if (nearestLand.type != ltype_sea)
		move_robber(human, nearestLand)
}
else {
	if (is_holding(objSettlement)) {
		var nearestLand = instance_nearest(mouse_x, mouse_y, objLand)
		
		var isCreated = create(human, objSettlement, nearestLand)
		if (isCreated)
			leave_held()
	}
	else if (is_holding(objRoad)) {
		var nearestLocation
		nearestLocation[0] = instance_nearest(mouse_x, mouse_y, objLocation)
		nearestLocation[1] = instance_nth_nearest(mouse_x, mouse_y, objLocation, 2)

		var isCreated = create(objSettlement, nearestLand)
		if (isCreated)
			leave_held()
	}
}

global.debugMode = false
}
#endregion