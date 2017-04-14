package housecream

import (
	"fmt"
	"strings"

	"github.com/oleksandr/conditions"
)

func StartEngine() {
	// Our condition to check
	s := "$3 == true"
	//s := "($0 > 0.45) AND ($1 == `ON` OR $2 == \"ACTIVE\") AND $3 == false"

	// Parse the condition language and get expression
	p := conditions.NewParser(strings.NewReader(s))
	expr, err := p.Parse()
	if err != nil {
		panic(err)
	}

	// Evaluate expression passing data for $vars
	args := make(map[string]interface{})
	args["$0"] = 0.12
	args["$1"] = "OFF"
	args["$2"] = "ACTIVE"
	args["$3"] = true

	r, err := conditions.Evaluate(expr, args)
	if err != nil {
		panic(err)
	}

	// r is false
	fmt.Println("Evaluation result:", r)
}
