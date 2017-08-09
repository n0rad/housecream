package housecream

import (
	"context"

	"github.com/n0rad/housecream/pkg/channels"
)

type ChannelService struct {
	client *Client
}

func (s ChannelService) List(ctx context.Context) ([]channels.CommonChannel, error) {
	req, err := s.client.NewRequest("GET", "channels", nil)
	if err != nil {
		return nil, err
	}

	f := []channels.CommonChannel{}
	_, err = s.client.Do(ctx, req, &f)
	if err != nil {
		return nil, err
	}

	return f, nil
}
