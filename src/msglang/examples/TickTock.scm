(define seq (lambda (cmd1 cmd2) cmd1))

(define ticktock 
	(process
		(receive (tick)
			(receive (tock) 
				(seq 
					(print tick) 
					(print tock) 
				)  
			)
		)
	)
)

(seq (send ticktock "tick") (send ticktock "tock"))