/// @param playerIndex
/// @param landID
/// @param victimIndex

global.robberLand = argument[1]
global.robberAddition_mode = false

if (global.actionWriting_mode)
	action_write(argument[0], action_move, global.robberLand.index, argument[2], actionObject_robber)