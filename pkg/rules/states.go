package rules

import "github.com/google/uuid"

type State struct {
}

type StateHolder struct {
	States map[uuid.UUID]State
}
