(define seq (lambda (cmd1 cmd2) cmd1))

(define sum 
	(process 
		(receive (num1)
			(receive (num2)
				(print (+ num1 num2))
			)
		)
	)
)

(seq (send sum 300) (send sum 42))