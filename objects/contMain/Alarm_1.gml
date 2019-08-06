alarm[1] = sec/5

for (var i = 1; i <= PLAYER_COUNT; i++) {
	global.totalCards[i] = 0
	global.totalCards[i] += ds_grid_get(global.resources, i, resource_brick)
	global.totalCards[i] += ds_grid_get(global.resources, i, resource_ore)
	global.totalCards[i] += ds_grid_get(global.resources, i, resource_grain)
	global.totalCards[i] += ds_grid_get(global.resources, i, resource_lumber)
	global.totalCards[i] += ds_grid_get(global.resources, i, resource_wool)
}