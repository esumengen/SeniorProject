random_set_seed(45)

draw_set_font(fontMain)

global.player = 1
global.player_active = 1

global.initialPhase = true

global.locations = ds_list_create()
global.lands = ds_list_create()
global.robberLand = pointer_null

#region CREATE BOARD
var landCount_vertical = 7
var landCount_horizontal_max = 7
var landCount_horizontal_min = landCount_horizontal_max-floor(landCount_vertical/2)

var landWidth = 121+17
var landHeight = 140+17

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
			land.type = ltype_desert
			global.robberLand = land
		}
		else if (i == 0 or i == landCount_vertical-1 or j == 0 or j == landCount_horizontal-1) {
			land.type = ltype_sea
			land.image_xscale += 0.14
			land.image_yscale += 0.14
			land.image_alpha = 1
		}
		
		land.image_blend = get_land_color(land.type)
		
		land.x = middle_x+j*landWidth-(landCount_horizontal-ceil(landCount_horizontal_max/2))*landWidth/2
		land.y = middle_y+i*landHeight*3/4
		
		land.index = landIndex
		landIndex++;
		
		//if (land.type != ltype_sea) {
			for (var angle = 30; angle < 360; angle += 60) {
				var xx = land.x+lengthdir_x(landHeight/2, angle)
				var yy = land.y+lengthdir_y(landHeight/2, angle)
			
				if (!position_meeting(xx, yy, objLocation)) {
					var location = instance_create_layer(xx, yy, "lyRoad", objLocation)
					location.index = land.index+(angle-30)/60
					ds_list_add(global.locations, location)
				}
			}
		//}
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

var ds_size = ds_list_size(global.lands)
for (var i = 0; i < ds_size; i++) {
	var land = ds_list_find_value(global.lands, i)
	
	for (var angle = 30; angle < 360; angle += 60) {
		var xx = land.x+lengthdir_x(landHeight/2, angle)
		var yy = land.y+lengthdir_y(landHeight/2, angle)
			
		var nearestLocation = instance_nearest(xx, yy, objLocation)
		ds_list_add(nearestLocation.adjacentLands, land)
		ds_list_add(land.adjacentLocations, nearestLocation)
		
		if (land.type != ltype_sea)
			nearestLocation.active = true
	}
}
#endregion

#region SET LAND TYPES
ini_open("environment.ini")
	var fieldsLeft = FIELDS_COUNT
	var pastureLeft = PASTURE_COUNT
	var forestLeft = FOREST_COUNT
	var mountainsLeft = MOUNTAINS_COUNT
	var hillsLeft = HILLS_COUNT
	
	for (var i = 0; i < ds_list_size(global.lands); i++) {
		var land = ds_list_find_value(global.lands, i)
	
		diceList = [0, 0, 0, 0, 0, 11, 12, 9, 0, 0, 4, 6, 5, 10, 0, 0, 3, 11, 0, 4, 8, 0, 0, 10, 8, 9, 3, 0, 0, 5, 2, 6, 0, 0, 0, 0, 0]
		with (land) {
			diceNo = other.diceList[i]
			
			if (type != ltype_sea and type != ltype_desert) { 
				while (fieldsLeft+pastureLeft+forestLeft+mountainsLeft+hillsLeft > 0) {
					var selectedType = ltypeStart+irandom(ltypeCount-2)
			
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
			}
		
			image_blend = get_land_color(type)
		
			ini_write_string("LandTypes", i, get_type_name(type))
			ini_write_string("Dice", i, string(diceNo))
		}
	}
ini_close()
#endregion

ini_open("environment.ini")
	ini_write_string("General", "isSynchronized", "true")
ini_close()

setPlayer = 1
alarm[2] = 1

global.robberAddition_mode = false

global.addStructure_mode = false
global.addStructure_object = pointer_null

period = 0
alarm[0] = 5

#region START Game
global.addStructure_mode = true
global.addStructure_object = actionObject_settlement
#endregion