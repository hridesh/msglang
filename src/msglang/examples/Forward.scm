(define sum 
	(process (num1 num2)
		(print (+ num1 num2))
	)
)
(define forward
	(process (num1 num2)
		(send sum num1 num2)
	)
)
(send forward 300 42)