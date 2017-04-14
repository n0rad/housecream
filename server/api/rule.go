package housecream

import "github.com/google/uuid"

type Rule struct {
	Id          uuid.UUID
	Name        string
	Condition   []Condition
	Consequence []Consequence
}

type ConditionType string

const (
	State ConditionType = "state"
	Event               = "event"
)

type TriggerType string

const (
	ReTrigger   TriggerType = "retrigger"
	NoReTrigger             = "no-retrigger"
)

type Condition struct {
	PointId uuid.UUID
	Value   float64
	Type    ConditionType
}

type Consequence struct {
	PointId uuid.UUID
	Value   float64
}
