var pointx,pointy,object,n,list
var nearst = noone

pointx = argument[0]
pointy = argument[1]
object = argument[2]
n = argument[3]

n = min(max(1, n), instance_number(object))
list = ds_priority_create()

with (object) ds_priority_add(list, id, distance_to_point(pointx, pointy))

repeat (n)
	nearest = ds_priority_delete_min(list)
	
ds_priority_destroy(list)

return nearest