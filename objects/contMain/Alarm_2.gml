ini_open("environment.ini")
	ini_write_string("General", "MainPlayer", string(setPlayer))
	
	ExecuteShell("CatanAI.jar", false)
ini_close()

setPlayer += 1

if (setPlayer <= PLAYER_COUNT)
	alarm[2] = sec