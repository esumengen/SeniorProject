event_inherited()

nearestLocation = instance_nearest(x, y, objLocation)
	
var settlementCount = structure_count(anyone, objSettlement, nearestLocation)
	
condition = settlementCount == 0 and nearestLocation.active