(define worker 
	(process 
		(receive (client num1 num2)
			(send client #f (+ num1 num2) 0)
		)
	)
)

(define client
	(process 
		(receive (request num1 num2)
			(if request
				(send worker (self) num1 num2)
				(print num1)
			)
		)
	)
)

(send client #t 300 42)