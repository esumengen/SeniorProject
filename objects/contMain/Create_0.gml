global.locations = ds_list_create()
global.lands = ds_list_create()

var middle_x = view_get_wport(0)/2
var middle_y = view_get_hport(0)/2

var landCount_vertical = 5
var landCount_horizontal_max = 5

for (var i = 0; i < landCount_vertical; i++) {
	var landCount_horizontal = landCount_horizontal_max-abs(i-floor(landCount_vertical/2))
	
	for (var j = 0; j < landCount_horizontal; j++) {
		var land = instance_create_depth(0, 0, 0, objLand)
		
		land.type = choose(ltype_forest, ltype_mountains, ltype_pasture, ltype_fields, ltype_hills)
		
		if (i == 2 and j == 2)
			land.image_blend = c_desert
		else
			land.image_blend = get_land_color(land.type)
		
		var landWidth = land.sprite_width
		var landHeight = land.sprite_height
		
		land.x = middle_x+j*landWidth-(landCount_horizontal-ceil(landCount_horizontal_max/2))*landWidth/2
		land.y = middle_y+i*landHeight*3/4
	}
}