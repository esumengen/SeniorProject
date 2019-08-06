if (count >= 6) {
	room_goto(roomMain)
	
	exit
}
count++

ini_open("environment.ini")
	var file = get_open_filename_ext(
	count < 4 ? (string(count)+". AI Agent (.class)|*.class") : (string(count-3)+". Negotiation Agent (.class)|*.class"),
	"", "", "Choose "+(count < 4 ? "an AI agent" : "a negotiation agent"))
	
	ini_write_string(count < 4 ? "AISetup" : "NegotiationSetup", count < 4 ? count : count-3, file == "" ? "Default" : file)
ini_close()

alarm[0] = 1*sec