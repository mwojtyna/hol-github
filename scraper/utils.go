package main

import (
	"log/slog"
	"os"
)

func GetJsonProperty[T any](object map[string]any, name string) T {
	prop, ok := object[name].(T)
	if !ok {
		slog.Error("Could not read property", "property_name", name)
	}

	return prop
}

func GetEnv(name string, dotenv map[string]string) (string, bool) {
	val1, ok1 := os.LookupEnv(name)
	val2, ok2 := dotenv[name]

	if !ok1 && !ok2 {
		return "", false
	} else if ok1 {
		return val1, true
	} else {
		return val2, true
	}
}
