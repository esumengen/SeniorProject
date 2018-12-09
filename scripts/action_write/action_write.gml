var fileActions = file_text_open_write("actions.txt")

if (argument[1] == action_create and argument[argument_count-1] == actionObject_road)
	file_text_write_string(fileActions, "P"+string(argument[0])+" ["+string(argument[1])+" "+(argument[2] < 10 ? "0"+string(argument[2]) : string(argument[2]))+" "+(argument[3] < 10 ? "0"+string(argument[3]) : string(argument[3]))+"] "+string(argument[argument_count-1]))
else
	file_text_write_string(fileActions, "P"+string(argument[0])+" ["+string(argument[1])+" "+(argument[2] < 10 ? "0"+string(argument[2]) : string(argument[2]))+"] "+string(argument[argument_count-1]))

file_text_close(fileActions)
		
ini_open("environment.ini")
	ini_write_string("General", "isSynchronized", "false")
ini_close()