/// @param playerIndex
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

var playerIndex = argument[0]
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

if (givenGrain % 4 != 0 or givenLumber % 4 != 0 or givenWool % 4 != 0 or givenOre % 4 != 0 or givenBrick % 4 != 0)
	return false
	
if (totalTaken == totalGiven/4) {
	change_resource(playerIndex, resource_grain, takenGrain-givenGrain)
	change_resource(playerIndex, resource_lumber, takenLumber-givenLumber)
	change_resource(playerIndex, resource_wool, takenWool-givenWool)
	change_resource(playerIndex, resource_ore, takenOre-givenOre)
	change_resource(playerIndex, resource_brick, takenBrick-givenBrick)
	
	action_trade_write(playerIndex, givenGrain, givenLumber, givenWool, givenOre, givenBrick, takenGrain, takenLumber, takenWool, takenOre, takenBrick, actionObject_bank)
	
	return true
}