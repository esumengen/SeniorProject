randomize()

global.player_active = 1

global.initialPhase = true
global.turn_ready = 1
global.turn = 1

global.isDiceRolled = false
global.dice[0] = 0
global.dice[1] = 0
global.diceTotal = 0

global.locations = ds_list_create()
global.lands = ds_list_create()
global.robberLand = pointer_null

global.debugMode = false

global.actionWriting_mode = true

global.resources = ds_grid_create(PLAYER_COUNT+1, RESOURCE_COUNT)
for (var i = 1; i <= PLAYER_COUNT; i++) {
	ds_grid_add(global.resources, i, resource_brick, 0)
	ds_grid_add(global.resources, i, resource_ore, 0)
	ds_grid_add(global.resources, i, resource_grain, 0)
	ds_grid_add(global.resources, i, resource_lumber, 0)
	ds_grid_add(global.resources, i, resource_wool, 0)
	
	global.playerScore[i] = 0
	global.longestRoad[i] = 0
	global.knights[i] = 0
	global.victoryCards[i] = 0
	
	global.totalCards[i] = 0
}

#region CREATE BOARD
var landCount_vertical = 7
var landCount_horizontal_max = 7
var landCount_horizontal_min = landCount_horizontal_max-floor(landCount_vertical/2)

var landWidth = sprite_get_width(sprLand)+14
var landHeight = sprite_get_height(sprLand)+14

var middle_x = room_width/2-floor(landCount_horizontal_min/2)*landWidth+(landCount_horizontal_min%2 == 0)*landWidth/2
var middle_y = room_height/2-floor(landCount_vertical/2)*landHeight*3/4

var landIndex = 0
for (var i = 0; i < landCount_vertical; i++) {
	var landCount_horizontal = landCount_horizontal_max-abs(i-floor(landCount_vertical/2))
	
	for (var j = 0; j < landCount_horizontal; j++) { 
		var land = instance_create_layer(0, 0, "lyLand", objLand)
		ds_list_add(global.lands, land)
		
		//land.type = choose(ltype_forest, ltype_mountains, ltype_pasture, ltype_fields, ltype_hills)
		if (landIndex == 5)
			land.type = ltype_forest
		else if (landIndex == 6)
			land.type = ltype_pasture
		else if (landIndex == 7)
			land.type = ltype_fields
		else if (landIndex == 10)
			land.type = ltype_hills
		else if (landIndex == 11)
			land.type = ltype_mountains
		else if (landIndex == 12)
			land.type = ltype_hills
		else if (landIndex == 13)
			land.type = ltype_pasture
		else if (landIndex == 16)
			land.type = ltype_desert
		else if (landIndex == 17)
			land.type = ltype_forest
		else if (landIndex == 18)
			land.type = ltype_fields
		else if (landIndex == 19)
			land.type = ltype_forest
		else if (landIndex == 20)
			land.type = ltype_fields
		else if (landIndex == 23)
			land.type = ltype_hills
		else if (landIndex == 24)
			land.type = ltype_pasture
		else if (landIndex == 25)
			land.type = ltype_pasture
		else if (landIndex == 26)
			land.type = ltype_mountains
		else if (landIndex == 29)
			land.type = ltype_mountains
		else if (landIndex == 30)
			land.type = ltype_fields
		else if (landIndex == 31)
			land.type = ltype_forest
		
		// Type Initialization and Customization
		if (land.type == ltype_desert/*i == floor(landCount_vertical/2) and j == floor(landCount_horizontal_max/2)*/) {
			land.type = ltype_desert // I know it's weird.
			global.robberLand = land
		}
		else if (i == 0 or i == landCount_vertical-1 or j == 0 or j == landCount_horizontal-1) {
			land.type = ltype_sea
			land.image_xscale += 0.17
			land.image_yscale += 0.17
			land.image_alpha = 1
		}
		
		land.image_blend = get_land_color(land.type)
		
		// Set lands' positions
		land.x = middle_x+j*landWidth-(landCount_horizontal-ceil(landCount_horizontal_max/2))*landWidth/2
		land.y = middle_y+i*landHeight*3/4
		
		land.index = landIndex
		landIndex++
		
		// Create locations
		for (var angle = 30; angle < 360; angle += 60) {
			var xx = land.x+lengthdir_x(landHeight/2, angle)
			var yy = land.y+lengthdir_y(landHeight/2, angle)
			
			if (!position_meeting(xx, yy, objLocation)) {
				var location = instance_create_layer(xx, yy, "lyRoad", objLocation)
				location.index = -1
				//ds_list_add(global.locations, location)
			}
		}
	}
}

// Binding locations with lands
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
			nearestLocation.isActive = true
	}
}
#endregion

#region SET LOCATIONS' INDEXES
var upsideLocs = ds_list_create()
ds_list_add(upsideLocs, 1)

var landIndex = 0
for (var i = 0; i < landCount_vertical; i++) {
	var landCount_horizontal = landCount_horizontal_max-abs(i-floor(landCount_vertical/2))
	for (var j = 0; j < landCount_horizontal; j++) {
		var land = ds_list_find_value(global.lands, landIndex)
		
		with (land) {
			var upsideLoc = ds_list_find_value(adjacentLocations, 1)
			
			if (landIndex == 0)
				upsideLoc.index = ds_list_find_value(upsideLocs, 0)
			else {
				upsideLoc.index = ds_list_find_value(upsideLocs, landIndex-1)+2

				if (j == 0) {
					var theIndex = floor(landCount_vertical/2)+1
					
					if (i == theIndex)
						upsideLoc.index += 2
					else if (i < theIndex)
						upsideLoc.index += 1
					else 
						upsideLoc.index += 3
				}
				
				ds_list_add(upsideLocs, upsideLoc.index)
			}
			
			var locationUL
			locationUL = ds_list_find_value(adjacentLocations, 2)
			locationUL.index = upsideLoc.index-1
			make_adjacents(locationUL, upsideLoc)
			
			var locationUR = ds_list_find_value(adjacentLocations, 0)
			locationUR.index = upsideLoc.index+1
			make_adjacents(locationUR, upsideLoc)
			
			var downsideLoc = ds_list_find_value(adjacentLocations, 4)
			downsideLoc.index = upsideLoc.index+2*landCount_horizontal+2-(landCount_horizontal == landCount_horizontal_max)
			
			var locationDL = ds_list_find_value(adjacentLocations, 3)
			locationDL.index = downsideLoc.index-1
			make_adjacents(locationDL, downsideLoc)
			
			var locationDR = ds_list_find_value(adjacentLocations, 5)
			locationDR.index = downsideLoc.index+1
			make_adjacents(locationDR, downsideLoc)
			
			make_adjacents(locationUR, locationDR)
			make_adjacents(locationUL, locationDL)
		}
		
		landIndex++
	}
}

ds_list_destroy(upsideLocs)
#endregion

#region ADD LOCATIONS TO THE LIST
	while (ds_list_size(global.locations) != instance_number(objLocation)) {
		with (objLocation) {
			if (index == ds_list_size(global.locations))
				ds_list_add(global.locations, id)
		}
	}
#endregion

#region SET LAND TYPES
ini_open("environment.ini")
	/*var fieldsLeft = FIELDS_COUNT
	var pastureLeft = PASTURE_COUNT
	var forestLeft = FOREST_COUNT
	var mountainsLeft = MOUNTAINS_COUNT
	var hillsLeft = HILLS_COUNT*/
	
	for (var i = 0; i < ds_list_size(global.lands); i++) {
		var land = ds_list_find_value(global.lands, i)
	
		diceList = [0, 0, 0, 0, 0, 11, 12, 9, 0, 0, 4, 6, 5, 10, 0, 0, 0, 3, 11, 4, 8, 0, 0, 8, 10, 9, 3, 0, 0, 5, 2, 6, 0, 0, 0, 0, 0]
		
		with (land) {
			diceNo = other.diceList[i]
			
			diceChance = 0
            for (var k = 1; k <= 6; k++)
                for (var l = 1; l <= 6; l++)
                    if (k + l == diceNo) diceChance++
			
			/*if (type != ltype_sea and type != ltype_desert) { 
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
			}*/
		
			image_blend = get_land_color(type)
			
			if (type == ltype_sea) {
				for (var j = 0; j < ds_list_size(adjacentLocations); j++) {
					var location = ds_list_find_value(adjacentLocations, j)
					
					location.isCorner = true
				}
			}
		
			ini_write_string("LandTypes", i, get_type_name(type))
			ini_write_string("Dice", i, string(diceNo))
		}
	}
	
	for (var i = 0; i < ds_list_size(global.locations); i++) {
		var location = ds_list_find_value(global.locations, i)
		
		if (location.harborType == htype_none)
			ini_write_string("HarborTypes", i, "null")
		else
			ini_write_string("HarborTypes", i, "null")
	}
ini_close()
#endregion

global.camera = view_get_camera(0)
global.stopGame = false

// Move Variables
global.robberAddition_mode = false

global.addStructure_mode = false
global.addStructure_object = pointer_null

// Period Loop
period = 0
periodUp = true

// AI Loop
alarm[11] = 1//0.5*sec

draw_set_font(fontMain)

#region START Game
global.addStructure_mode = true
global.addStructure_object = objSettlement
#endregion

alarm[9] = sec
alarm[1] = sec/2