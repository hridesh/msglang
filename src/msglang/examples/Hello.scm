(define hello 
	(process (sender message)
		"Hello"
	)
)

(send hello (self) "hi")