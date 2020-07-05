(define seq 
	(lambda (cmd1 cmd2) cmd1)
)

(define sum 
	(process 
		(receive (num1 num2)
			(print (+ num1 num2))
		)
	)
)

(define multforward
	(process 
		(receive (num1 num2)
			(seq 
				(send sum num1 num2)
				(send sum (+1 num1) (+ 1 num2))
			)
		)
	)
)

(send multforward 300 42)