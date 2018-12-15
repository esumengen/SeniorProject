/// @desc Condition Check

var sameExists = false
with (objRoad) {
	if (id != other.id) {
		if (other.location[0] == location[0] and other.location[1] == location[1]
		or other.location[0] == location[1] and other.location[1] == location[0]) {
			sameExists = true
			break
		}
	}
}

condition = (structure_count(playerIndex, objSettlement, location[0]) > 0 or structure_count(playerIndex, objSettlement, location[1]) > 0
or structure_count(playerIndex, objRoad, location[0]) > 0 or structure_count(playerIndex, objRoad, location[1]) > 0)
and location[0].active and location[1].active
and location[0] != location[1]
and !sameExists