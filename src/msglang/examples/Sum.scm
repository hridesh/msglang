(define sum 
	(process (num1 num2)
		(print (+ num1 num2))
	)
)

(send sum 300 42)