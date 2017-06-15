
all: clean build test quality

quality:
	./gomake quality

install:
	./gomake install

build:
	./gomake build

test-short:
	./gomake test -short

test:
	./gomake test

clean:
	./gomake clean
