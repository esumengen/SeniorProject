/// @param playerIndex
/// @param resourceType
/// @param amount

var oldValue = ds_grid_get(global.resources, argument[0], argument[1])
ds_grid_set(global.resources, argument[0], argument[1], oldValue+argument[2])