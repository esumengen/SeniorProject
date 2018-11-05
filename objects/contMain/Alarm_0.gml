var fileName = "communication.txt"
if (file_exists(fileName)) {
	var file = file_text_open_read(fileName)
	
	var str = file_text_read_string(file)
	show_message(str)
}
else
	show_message("Cannot find "+fileName)