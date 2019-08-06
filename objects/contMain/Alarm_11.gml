/// @desc AI Loop

var moveDone = false
alarm[11] = 1//sec*0.5

ini_open("communication.ini")
	if (global.player_active != human and is_turn_ready() and keyboard_check(ord("C"))) {
		moveDone = (ini_read_string("General", "turnMode["+string(global.player_active-1)+"]", "normal") == "done")
		and file_exists("actions_temp"+string(global.player_active-1)+".txt")
	}

	if (moveDone) {		
		var oldestLongestRoad_value = 0
		var oldestLongestRoad_owner = -1
		var oldestKnights_value = 0
		var oldestKnights_owner = -1
		for (var i = 1; i <= PLAYER_COUNT; i++) {
			if (global.longestRoad[i] > oldestLongestRoad_value) {
				oldestLongestRoad_owner = i
				oldestLongestRoad_value = global.longestRoad[i]
			}
			
			if (global.knights[i] > oldestKnights_value) {
				oldestKnights_owner = i
				oldestKnights_value = global.knights[i]
			}
		}
		
		for (var i = 0; i < PLAYER_COUNT; i++)
			global.longestRoad[i+1] = ini_read_real("LongestRoad", "Player["+string(i)+"]", 0)
		
		#region TAKE ACTIONS
		var actionList_file = file_text_open_read("actions_temp"+string(global.player_active-1)+".txt")
		
		var line = file_text_readln(actionList_file)

		while (string_length(line) > 3) {
			var actionParams = ds_list_create()
			var playerIndex = real(string_char_at(line, 2))
            var actionType = string_copy(line, 5, 2)

            var value = 8
            while (value < string_length(line)-3) {
                ds_list_add(actionParams, real(string_copy(line, value, 2)))
				
                value += 3
            }

            var objectType = string_char_at(line, string_length(line)-2)
			
            switch (actionType) {
                case "CR":
                    if (objectType == "S") {
						var isCreated = create(playerIndex, objSettlement, ds_list_find_value(global.locations, ds_list_find_value(actionParams, 0)))
						
						if (isCreated and !global.initialPhase) {
							change_resource(playerIndex, resource_brick, -1)
							change_resource(playerIndex, resource_lumber, -1)
							change_resource(playerIndex, resource_grain, -1)
							change_resource(playerIndex, resource_wool, -1)
						}
						else if (!isCreated)
							show_message(objectType + " could not be created.")
                    } else if (objectType == "R") {
                        var isCreated = create(playerIndex, objRoad, ds_list_find_value(global.locations, ds_list_find_value(actionParams, 0)), ds_list_find_value(global.locations, ds_list_find_value(actionParams, 1)))
                    
						if (isCreated and !global.initialPhase) {
							change_resource(playerIndex, resource_brick, -1)
							change_resource(playerIndex, resource_lumber, -1)
						}
						else if (!isCreated)
							show_message(objectType + " could not be created.")
					}
                    break
                case "UP":
                    if (objectType == "S") {
                        var isUpgraded = upgrade(playerIndex, ds_list_find_value(global.locations, ds_list_find_value(actionParams, 0)))
						
						if (isUpgraded and !global.initialPhase) {
							change_resource(playerIndex, resource_ore, -3)
							change_resource(playerIndex, resource_grain, -2)
						}
						else if (!isUpgraded)
							show_message(objectType + " could not be created.")
                    }
                    break
                case "MO":
                    if (objectType == "T") {
                        move_robber(playerIndex, ds_list_find_value(global.lands, ds_list_find_value(actionParams, 0)), ds_list_find_value(actionParams, 1))
                    }
                    break
                case "TR":
                    if (objectType == "B")  {
		                trade_bank(playerIndex, ds_list_find_value(actionParams, 0), ds_list_find_value(actionParams, 1), ds_list_find_value(actionParams, 2), ds_list_find_value(actionParams, 3)
						, ds_list_find_value(actionParams, 4), ds_list_find_value(actionParams, 5), ds_list_find_value(actionParams, 6), ds_list_find_value(actionParams, 7), ds_list_find_value(actionParams, 8)
						, ds_list_find_value(actionParams, 9))
		            } else {
		                trade_player(playerIndex, ds_list_find_value(actionParams, 0), ds_list_find_value(actionParams, 1), ds_list_find_value(actionParams, 2), ds_list_find_value(actionParams, 3)
						, ds_list_find_value(actionParams, 4), ds_list_find_value(actionParams, 5), ds_list_find_value(actionParams, 6), ds_list_find_value(actionParams, 7), ds_list_find_value(actionParams, 8)
						, ds_list_find_value(actionParams, 9), real(objectType))
		            }
					break
                case "RD":
                    roll_dice(playerIndex, ds_list_find_value(actionParams, 0), ds_list_find_value(actionParams, 1))
            }
			ds_list_destroy(actionParams)
			
			line = file_text_readln(actionList_file)
		}
		
		file_text_close(actionList_file)
		#endregion
		
		file_delete("actions_temp"+string(global.player_active-1)+".txt")
		ini_write_string("General", "turnMode["+string(global.player_active-1)+"]", "normal")
		
		turn_end()

		for (var i = 1; i <= PLAYER_COUNT; i++) {
			global.playerScore[i] = structure_count(i, objSettlement)
			global.playerScore[i] += structure_count(i, objCity)
		
			if (global.longestRoad[i] > 4 and (global.longestRoad[i] > oldestLongestRoad_value or oldestLongestRoad_owner == i))
				global.playerScore[i] += 2
			
			if (global.knights[i] > 2 and (global.knights[i] > oldestKnights_value or oldestLongestRoad_owner == i))
				global.playerScore[i] += 2
			
			global.playerScore[i] += global.victoryCards[i]
		
			if (global.playerScore[i] == MAX_SCORE) {
				show_message_async("Player "+string(i)+" is the winner!")
				global.stopGame = true
			}
		}
	}
ini_close()

if (!global.stopGame) {
	for (var i = 1; i <= PLAYER_COUNT; i++) {
		if (global.playerScore[i] == MAX_SCORE) {
			show_message_async("Player "+string(i)+" is the winner!")
			global.stopGame = true
		}
	}
}