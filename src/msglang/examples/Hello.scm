(define hello 
	(process (message)
		(print "Hello")
	)
)

(send hello "hi")