package it

import (
	"context"
	"testing"

	"github.com/n0rad/housecream/pkg/housecream"
	"github.com/stretchr/testify/assert"
)

func TestListChannels(t *testing.T) {
	setupIT(t)

	client := housecream.NewClient(nil)
	ctx := context.Background()
	channels, err := client.Channels.List(ctx)
	if err != nil {
		t.Errorf("Channels.Get returned error: %v", err)
	}

	assert.True(t, len(channels) > 0)
}
