DOCKER_REPO?=registry.gocurb.internal:80
CONTAINER=$(DOCKER_REPO)/curbkins

all: build clean

build:
	docker build . 

clean:
	docker rmi $(CONTAINER)
	
