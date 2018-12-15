var location = argument[0]

var list = ds_list_create()
var list2 = ds_list_create()
var list3 = ds_list_create()

var adjacentLands = location.adjacentLands

if (ds_list_size(adjacentLands) > 0) {
	var k = ds_list_find_value(adjacentLands, 0)
	ds_list_copy(list, k.adjacentLocations)
}

if (ds_list_size(adjacentLands) > 1) {
	var l = ds_list_find_value(adjacentLands, 1)
	ds_list_copy(list2, l.adjacentLocations)
}

if (ds_list_size(adjacentLands) > 2) {
	var l = ds_list_find_value(adjacentLands, 2)
	ds_list_copy(list3, l.adjacentLocations)
}

var listIntersection = ds_list_create()

for (var i = 0; i < ds_list_size(list); i++) {
	var value = ds_list_find_value(list, i)
	
	if (value != location) {
		if (ds_list_find_index(list2, value) != -1)
			ds_list_add(listIntersection, value)
		else if (ds_list_find_index(list3, value) != -1)
			ds_list_add(listIntersection, value)
	}
}

for (var i = 0; i < ds_list_size(list2); i++) {
	var value = ds_list_find_value(list2, i)

	if (value != location) {
		if (ds_list_find_index(list3, value) != -1)
			ds_list_add(listIntersection, value)
	}
}

ds_list_destroy(list)
ds_list_destroy(list2)
ds_list_destroy(list3)

return listIntersection