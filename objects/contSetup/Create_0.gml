#region INIT FILES
file_delete("environment.ini")

file_delete("actions_from_client.txt")

file_delete("communication.ini")
ini_open("communication.ini")
	ini_write_string("General", "isSynchronized", "true")
	
	for (var i = 1; i < PLAYER_COUNT; i++)
		ini_write_string("General", "turnMode["+string(i)+"]", "normal")
		
	ini_write_string("Game State", "isInitial", "true")
ini_close()

file_delete("actions.txt")
var fileActions = file_text_open_write("actions.txt")
	file_text_write_string(fileActions, "")
file_text_close(fileActions)

file_delete("log.txt")

for (var i = 1; i < PLAYER_COUNT; i++)
	file_delete("actions_temp"+string(i)+".txt")
#endregion

alarm[0] = 1*sec

count = 0