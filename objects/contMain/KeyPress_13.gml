if (global.player_active == human and !global.initialPhase and is_turn_ready()) {
	if (can_dice())
		roll_dice(1+irandom(5), 1+irandom(5))
	else
		turn_end()
}