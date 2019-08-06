/// @param playerIndex
/// @param actionType
/// @param actionParam[0]
/// @param actionParam[1]* ...
/// @param actionObject

var paramPart = string(argument[2] < 10 ? ("0"+string(argument[2])) : string(argument[2]))
var playerIndex = argument[0]
var actionType = argument[1]
var actionObject = argument[argument_count-1]

for (var i = 3; i < argument_count-1; i++)
	paramPart += " "+(argument[i] < 10 ? (argument[i] == -1 ? string(argument[i]) : ("0"+string(argument[i]))) : string(argument[i]))
	
var fileActions = file_text_open_append("actions.txt")
	file_text_write_string(
		fileActions, "P"+string(playerIndex)+" ["+actionType+" "+paramPart+"] "+actionObject+"\n"
	)
file_text_close(fileActions)

var fileActions_fromClient = file_text_open_append("actions_from_client.txt")
	file_text_write_string(
		fileActions_fromClient, "P"+string(playerIndex)+" ["+actionType+" "+paramPart+"] "+actionObject+"\n"
	)
file_text_close(fileActions_fromClient)