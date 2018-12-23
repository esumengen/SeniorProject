/// @param isVertical
/// @param player

if (argument[0])
	return room_height/2+lengthdir_y(400, -argument[1]*90)
else
	return room_width/2+lengthdir_x(400, -argument[1]*90)