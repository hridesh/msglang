(define hello 
	(process (message)
		(receive (message)
			(print "Hello")
		)
	)
)

(send hello "hi")