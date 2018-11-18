randomize()
draw_set_font(fontMain)

global.player = 0

#region PUBLIC DATA
	global.typeMap = ds_map_create()
#endregion

alarm[0] = sec

global.locations = ds_list_create()
global.lands = ds_list_create()

var landCount_vertical = 5
var landCount_horizontal_max = 5
var landCount_horizontal_min = landCount_horizontal_max-floor(landCount_vertical/2)

var landWidth = 121+20
var landHeight = 140+20

var middle_x = room_width/2-floor(landCount_horizontal_min/2)*landWidth+(landCount_horizontal_min%2 == 0)*landWidth/2
var middle_y = room_height/2-floor(landCount_vertical/2)*landHeight*3/4

var landIndex = 0
for (var i = 0; i < landCount_vertical; i++) {
	var landCount_horizontal = landCount_horizontal_max-abs(i-floor(landCount_vertical/2))
	
	for (var j = 0; j < landCount_horizontal; j++) {
		var land = instance_create_layer(0, 0, "lyLand", objLand)
		ds_list_add(global.lands, land)
		
		land.type = choose(ltype_forest, ltype_mountains, ltype_pasture, ltype_fields, ltype_hills)
		
		if (i == floor(landCount_vertical/2) and j == floor(landCount_horizontal_max/2)) {
			land.image_blend = c_desert
			land.type = ltype_desert
		}
		else
			land.image_blend = get_land_color(land.type)
		
		land.x = middle_x+j*landWidth-(landCount_horizontal-ceil(landCount_horizontal_max/2))*landWidth/2
		land.y = middle_y+i*landHeight*3/4
		
		land.index = landIndex
		landIndex++;
		
		for (var angle = 30; angle < 360; angle += 60) {
			var xx = land.x+lengthdir_x(landHeight/2, angle)
			var yy = land.y+lengthdir_y(landHeight/2, angle)
			
			if (!position_meeting(xx, yy, objLocation)) {
				var location = instance_create_layer(xx, yy, "lyRoad", objLocation)
				location.index = land.index+(angle-30)/60
				ds_list_add(global.locations, location)
			}
		}
	}
}

var ds_size = ds_list_size(global.lands)
for (var i = 0; i < ds_size; i++) {
	var land = ds_list_find_value(global.lands, i)
	
	for (var angle = 30; angle < 360; angle += 60) {
		var xx = land.x+lengthdir_x(landHeight/2, angle)
		var yy = land.y+lengthdir_y(landHeight/2, angle)
			
		var nearestLocation = instance_nearest(xx, yy, objLocation)
		ds_list_add(nearestLocation.adjacentLands, land)
		ds_list_add(land.adjacentLocations, nearestLocation)
	}
}

var landIndex = 0
for (var i = 0; i < landCount_vertical; i++) {
	var landCount_horizontal = landCount_horizontal_max-abs(i-floor(landCount_vertical/2))
	for (var j = 0; j < landCount_horizontal; j++) {
		var land = ds_list_find_value(global.lands, landIndex)
		
		with (land) {
			var upsideLoc = ds_list_find_value(adjacentLocations, 1)
			upsideLoc.index = 2*landIndex+fib(i+1)
			
			var land
			land = ds_list_find_value(adjacentLocations, 2)
			land.index = upsideLoc.index-1
			
			land = ds_list_find_value(adjacentLocations, 0)
			land.index = upsideLoc.index+1
			
			var downsideLoc = ds_list_find_value(adjacentLocations, 4)
			downsideLoc.index = upsideLoc.index+2*landCount_horizontal+2-(landCount_horizontal == landCount_horizontal_max)
			
			land = ds_list_find_value(adjacentLocations, 3)
			land.index = downsideLoc.index-1
			
			land = ds_list_find_value(adjacentLocations, 5)
			land.index = downsideLoc.index+1
		}
		
		landIndex++
	}
}

ini_open("environment.ini")
#region SET LAND TYPES
	var fieldsLeft = FIELDS_COUNT
	var pastureLeft = PASTURE_COUNT
	var forestLeft = FOREST_COUNT
	var mountainsLeft = MOUNTAINS_COUNT
	var hillsLeft = HILLS_COUNT
	
	for (var i = 0; i < ds_list_size(global.lands); i++) {
		if (i == 9) {
			ini_write_string("Dice", i, 0)
			ini_write_string("LandTypes", i, get_type_name(ltype_desert))
			continue
		}
	
		var land = ds_list_find_value(global.lands, i)
	
		diceList = [11, 12, 9, 4, 6, 5, 10, 3, 11, 0, 4, 8, 10, 8, 9, 3, 5, 2, 6]
		with (land) {
			diceNo = other.diceList[i]
			
			while (fieldsLeft+pastureLeft+forestLeft+mountainsLeft+hillsLeft > 0) {
				var selectedType = choose(ltype_desert, ltype_fields, ltype_forest, ltype_hills, ltype_mountains, ltype_pasture)
			
				if (selectedType == ltype_fields and fieldsLeft > 0) {
					type = selectedType
					fieldsLeft--
					break
				}
				else if (selectedType == ltype_forest and forestLeft > 0) {
					type = selectedType
					forestLeft--
					break
				}
				else if (selectedType == ltype_hills and hillsLeft > 0) {
					type = selectedType
					hillsLeft--
					break
				}
				else if (selectedType == ltype_mountains and mountainsLeft > 0) {
					type = selectedType
					mountainsLeft--
					break
				}
				else if (selectedType == ltype_pasture and pastureLeft > 0) {
					type = selectedType
					pastureLeft--
					break
				}
			}
		
			image_blend = get_land_color(type)
		
			ini_write_string("LandTypes", i, get_type_name(selectedType))
			ini_write_string("Dice", i, irandom(8))
		}
	}
	#endregion
ini_close()

ExecuteShell("CatanAI.jar", true)