(define seq (lambda (cmd1 cmd2) cmd1))

(define ping 
	(process (sender num)
		(if
			(> num 0) 
			(seq 
				(print "ping") 
				(send sender (self) (- num 1))
			)  
			(stop)
		)
	)
)

(define pong 
	(process (sender num)
		(if
			(> num 0) 
			(seq 
				(print "pong") 
				(send sender (self) (- num 1))
			)
			(stop)
		)
	)
) 

(send ping pong 42)