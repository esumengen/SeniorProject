/// @param playerIndex
/// @param landID

global.robberLand = argument[1]
global.robberAddition_mode = false

if (global.actionWriting_mode)
	action_write(argument[0], action_move, global.robberLand.index, irandom_range(0, PLAYER_COUNT-1), irandom(4), actionObject_robber)