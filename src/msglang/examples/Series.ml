(define serieshelper
	(process 
		(receive (num accumulator)
			(if (> num 0)
				(send (self) (- num 1) (+ num accumulator))
				(print accumulator)
			)
		)
	)
)

(define series
	(process 
		(receive (num)
			(send serieshelper num 0)
		)
	)
)

(send series 342)