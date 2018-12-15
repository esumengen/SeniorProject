x = room_width/2-(room_width/2-mouse_x)/4
y = room_height/2-(room_height/2-mouse_y)/4

if (periodUp) {
	if (period < 9.9)
		period += 0.1
	else {
		period = 10
		periodUp = false
	}
}
else {
	if (period > 0.1)
		period -= 0.1
	else {
		period = 0
		periodUp = true
	}
}