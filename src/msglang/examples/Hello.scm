(define hello 
	(process
		(receive (message)
			(print "Hello")
		)
	)
)

(send hello "hi")