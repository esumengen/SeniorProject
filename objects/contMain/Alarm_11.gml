/// @desc AI Loop

var moveDone = false
alarm[11] = 0.5*sec

ini_open("communication.ini")
	if (global.player_active != human and is_turn_ready()) {
		moveDone = (ini_read_string("General", "turnMode["+string(global.player_active-1)+"]", "normal") == "done")
		and file_exists("actions_temp"+string(global.player_active-1)+".txt")
	}

	if (moveDone) {		
		#region TAKE ACTIONS
		var actionList_file = file_text_open_read("actions_temp"+string(global.player_active-1)+".txt")
		
		var line = file_text_readln(actionList_file)

		while (string_length(line) > 3) {
			var actionParams = ds_list_create()
			var playerIndex = real(string_char_at(line, 2)) // -1 on java
            var actionType = string_copy(line, 5, 2)

            var value = 8
            while (value < string_length(line)-1) {
                ds_list_add(actionParams, (real(string_copy(line, value, 2))))

                value += 3
            }

            var objectType = string_char_at(line, string_length(line)-2)
			
            switch (actionType) {
                case "CR":
                    if (objectType == "S") {
						create(playerIndex, objSettlement, ds_list_find_value(global.locations, ds_list_find_value(actionParams, 0)))
                    } else if (objectType == "R") {
                        create(playerIndex, objRoad, ds_list_find_value(global.locations, ds_list_find_value(actionParams, 0)), ds_list_find_value(global.locations, ds_list_find_value(actionParams, 1)))
                    }
                    break
                case "UP":
                    if (objectType == "S") {
                        //upgrade(ds_list_find_value(actionParams, 0))
                    }
                    break
                case "MO":
                    if (objectType == "T") {
                        move_robber(playerIndex, ds_list_find_value(global.lands, ds_list_find_value(actionParams, 0)))
                    }
                    break
                case "TR":
                    if (objectType == "B")  {
		                //board.tradeBank(playerIndex, actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4), actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));
		            } else {
		                //board.tradePlayer(playerIndex, Integer.parseInt(objectType), actionParam.get(0), actionParam.get(1), actionParam.get(2), actionParam.get(3), actionParam.get(4), actionParam.get(5), actionParam.get(6), actionParam.get(7), actionParam.get(8), actionParam.get(9));
		            }
					break
                case "RD":
                    roll_dice(playerIndex, ds_list_find_value(actionParams, 0), ds_list_find_value(actionParams, 1))
            }
			ds_list_destroy(actionParams)
			
			line = file_text_readln(actionList_file)
		}
		#endregion
		file_text_close(actionList_file)
		
		file_delete("actions_temp"+string(global.player_active-1)+".txt")
		ini_write_string("General", "turnMode["+string(global.player_active-1)+"]", "normal")
		
		turn_end()
	}
ini_close()