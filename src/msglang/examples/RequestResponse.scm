(define worker 
	(process (num1 num2)
		(send client #f (+ num1 num2) 0)
	)
)

(define client
	(process (request num1 num2)
		(if request
			(send worker num1 num2)
			(print num1)
		)
	)
)

(send client #t 300 42)