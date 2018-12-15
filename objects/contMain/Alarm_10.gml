/// @desc Sync Garuntuer

var areAllSync = true
ini_open("environment.ini")
	for (var i = 0; i < PLAYER_COUNT; i++) {
		if (ini_read_string("General", "isSynchronized["+string(i)+"]", "false") != "true") {
			areAllSync = false
			break
		}
	}
ini_close()

if (areAllSync) {
	global.turn_ready = global.turn

	var fileActions = file_text_open_write("actions.txt")
		file_text_write_string(fileActions, "")
	file_text_close(fileActions)
}
else
	alarm[10] = 5