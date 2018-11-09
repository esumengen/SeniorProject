var n = argument[0]

if (n == 0 or n == 1)
	return 1
	
return fib(n-1)+fib(n-2)