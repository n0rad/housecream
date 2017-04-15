package housecream

import "fmt"

type PointService service

type Point struct {
	Name        *string `json:"name,omitempty"`
	Description *string `json:"description,omitempty"`

	//Value uint64
	//Type ?
}

func (s *PointService) Get(user string) (*Point, *Response, error) {
	var u string
	if user != "" {
		u = fmt.Sprintf("users/%v", user)
	} else {
		u = "user"
	}
	req, err := s.client.NewRequest("GET", u, nil)
	if err != nil {
		return nil, nil, err
	}

	uResp := new(Point)
	resp, err := s.client.Do(req, uResp)
	if err != nil {
		return nil, resp, err
	}

	return uResp, resp, err
}
