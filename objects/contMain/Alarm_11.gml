/// @desc AI Loop

var moveDone = false
alarm[11] = 0.5*sec

if (global.player_active != 1 and is_turn_ready()) {
	if (global.initialPhase) {

		//moveDone = true
	}
}

if (moveDone)
	turn_end()