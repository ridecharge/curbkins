CONTAINER=curbkins-test

all: build run clean

build: Dockerfile
	docker build -t $(CONTAINER) .

run:
	docker run --link consul:consul -i -t $(CONTAINER)

clean:
	docker rmi $(CONTAINER)
