/// @param playerGiverIndex
/// @param givenGrain
/// @param givenLumber
/// @param givenWool
/// @param givenOre
/// @param givenBrick
/// @param takenGrain
/// @param takenLumber
/// @param takenWool
/// @param takenOre
/// @param takenBrick
/// @param playerTakerIndex

var playerGiverIndex = argument[0]
var playerTakerIndex = argument[11]
var givenGrain = argument[1]
var givenLumber = argument[2]
var givenWool = argument[3]
var givenOre = argument[4]
var givenBrick = argument[5]
var takenGrain = argument[6]
var takenLumber = argument[7]
var takenWool = argument[8]
var takenOre = argument[9]
var takenBrick = argument[10]

var totalGiven = givenGrain+givenLumber+givenWool+givenOre+givenBrick
var totalTaken = takenGrain+takenLumber+takenWool+takenOre+takenBrick

if (givenGrain < 0 or givenLumber < 0 or givenWool < 0 or givenOre < 0 or givenBrick < 0
or takenGrain < 0 or takenLumber < 0 or takenWool < 0 or takenOre < 0 or takenBrick < 0)
	return false
	
if (totalGiven+totalTaken == 0)
	return false
	
if (ds_grid_get(global.resources, playerGiverIndex, resource_grain) < givenGrain
	or ds_grid_get(global.resources, playerGiverIndex, resource_lumber) < givenLumber
	or ds_grid_get(global.resources, playerGiverIndex, resource_wool) < givenWool
	or ds_grid_get(global.resources, playerGiverIndex, resource_ore) < givenOre
	or ds_grid_get(global.resources, playerGiverIndex, resource_brick) < givenBrick
	or ds_grid_get(global.resources, playerTakerIndex, resource_grain) < takenGrain
	or ds_grid_get(global.resources, playerTakerIndex, resource_lumber) < takenLumber
	or ds_grid_get(global.resources, playerTakerIndex, resource_wool) < takenWool
	or ds_grid_get(global.resources, playerTakerIndex, resource_ore) < takenOre
	or ds_grid_get(global.resources, playerTakerIndex, resource_brick) < takenBrick) {
	show_message("impossible")
	return false
}
	
change_resource(playerGiverIndex, resource_grain, takenGrain-givenGrain)
change_resource(playerGiverIndex, resource_lumber, takenLumber-givenLumber)
change_resource(playerGiverIndex, resource_wool, takenWool-givenWool)
change_resource(playerGiverIndex, resource_ore, takenOre-givenOre)
change_resource(playerGiverIndex, resource_brick, takenBrick-givenBrick)
	
change_resource(playerTakerIndex, resource_grain, givenGrain-takenGrain)
change_resource(playerTakerIndex, resource_lumber, givenLumber-takenLumber)
change_resource(playerTakerIndex, resource_wool, givenWool-takenWool)
change_resource(playerTakerIndex, resource_ore, givenOre-takenOre)
change_resource(playerTakerIndex, resource_brick, givenBrick-takenBrick)
	
action_trade_write(playerGiverIndex, givenGrain, givenLumber, givenWool, givenOre, givenBrick, takenGrain, takenLumber, takenWool, takenOre, takenBrick, string(playerTakerIndex))
	
return true