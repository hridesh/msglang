(define serieshelper
	(process (num accumulator)
		(if (> num 0)
			(send (self) (- num 1) (+ num accumulator))
			(print accumulator)
		)
	)
)

(define series
	(process (num)
		(send serieshelper num 0)
	)
)

(send series 342)