package main

import (
	"log/slog"
)

func GetJsonProperty[T any](object map[string]any, name string) T {
	prop, ok := object[name].(T)
	if !ok {
		slog.Error("Could not read property", "property_name", name)
	}

	return prop
}
