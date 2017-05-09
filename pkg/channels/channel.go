package channels

type Channel interface {
	GetId() string
	GetName() string
	GetDescription() string
	// events
	// actions
}

var ChannelByIds = make(map[string]Channel)

func RegisterChannel(c Channel) {
	ChannelByIds[c.GetId()] = c
}

//////////////////////////////////

type CommonChannel struct {
	Id          string
	Name        string
	Description string
}

func (c CommonChannel) GetId() string {
	return c.Id
}

func (c CommonChannel) GetName() string {
	return c.Name
}

func (c CommonChannel) GetDescription() string {
	return c.Description
}

func (c CommonChannel) String() string {
	return c.Id
}
