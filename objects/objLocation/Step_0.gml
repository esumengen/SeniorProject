dis = point_distance(x, y, mouse_x, mouse_y)

var near = dis < 120

if (near) {
	if (image_alpha+0.1 < 1)
		image_alpha += 0.1
	else
		image_alpha = 1
}
else {
	if (image_alpha-0.1 > 0)
		image_alpha -= 0.1
	else
		image_alpha = 0
}