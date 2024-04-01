package main

import (
	"strconv"
)

type StatusCodeError struct {
	Code int
}

func (e StatusCodeError) Error() string {
	return "Status code " + strconv.Itoa(e.Code)
}
