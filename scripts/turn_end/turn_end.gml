if (global.initialPhase) {
	var forward = !(structure_count(anyone, objSettlement) >= PLAYER_COUNT)
	var isDoubleTurnDone = structure_count(anyone, objSettlement) >= PLAYER_COUNT+1
	
	if (forward) {
		if (global.player_active < PLAYER_COUNT)
			global.player_active += 1
	}
	else if (isDoubleTurnDone) {
		global.player_active -= 1
		
		if (global.player_active == 1)
			global.initialPhase = false
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

global.isDiceRolled = false

global.turn += 1

ini_open("environment.ini")
	for (var i = 0; i < PLAYER_COUNT; i++)
		ini_write_string("General", "isSynchronized["+string(i)+"]", "false")
ini_close()

contMain.alarm[10] = 5