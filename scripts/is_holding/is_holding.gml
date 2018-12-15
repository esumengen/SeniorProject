if (object_get_parent(argument[0]) == parStructure or object_get_parent(argument[0]) == parBuilding)
	return (global.addStructure_mode and global.addStructure_object == argument[0])
	
return false