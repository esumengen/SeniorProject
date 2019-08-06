if (global.initialPhase and structure_count(anyone, objSettlement) == PLAYER_COUNT*2) {
	global.initialPhase = false
}
else
{
	if (global.initialPhase) {
		var forward = !(structure_count(anyone, objSettlement) >= PLAYER_COUNT)
		var isDoubleTurnDone = structure_count(anyone, objSettlement) >= PLAYER_COUNT+1
	
		if (forward) {
			if (global.player_active < PLAYER_COUNT)
				global.player_active += 1
		}
		else if (isDoubleTurnDone) {
			global.player_active -= 1
		}
	}
	else {
		if (global.player_active < PLAYER_COUNT) {
			global.player_active += 1
		}
		else {
			global.player_active = 1
		}
	}
}

global.isDiceRolled = false
if (global.player_active != human and can_dice())
	roll_dice(1+irandom(5), 1+irandom(5))

global.turn += 1

ini_open("communication.ini")
	ini_write_string("Game State", "isInitial", global.initialPhase ? "true" : "false")
	ini_write_string("General", "isSynchronized", "false")
ini_close()

global.robberAddition_mode = false
global.addStructure_mode = false
global.addStructure_object = pointer_null

contMain.alarm[10] = 5