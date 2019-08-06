ini_open("environment.ini")
	for (var i = 1; i < PLAYER_COUNT; i++) {
		ini_write_string("AISetup", i, "Default")
		ini_write_string("NegotiationSetup", i, "Default")
	}
ini_close()

room_goto(roomMain)