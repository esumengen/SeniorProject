/// @desc Condition Check

nearestLocation[0] = instance_nearest(x, y, objLocation)
nearestLocation[1] = instance_nth_nearest(x, y, objLocation, 2)

nearestLocation[0] = instance_nearest(x, y, objLocation)
nearestLocation[1] = instance_nth_nearest(x, y, objLocation, 2)

condition = structure_count(anyone, objSettlement, nearestLocation[0]) > 0 or structure_count(anyone, objSettlement, nearestLocation[1]) > 0
or structure_count(anyone, objRoad, nearestLocation[0]) > 0 or structure_count(anyone, objRoad, nearestLocation[1]) > 0