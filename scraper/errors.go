package main

import (
	"strconv"
)

type StatusCodeError struct {
	Code int
}

func (e StatusCodeError) Error() string {
	return strconv.Itoa(e.Code)
}
