var file = file_text_open_read("FoSho.txt")

var str = file_text_read_string(file)
show_message(str)

file_text_close(file)

alarm[1] = sec/2