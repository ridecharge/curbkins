DOCKER_REPO?=registry.gocurb.internal:80
CONTAINER=$(DOCKER_REPO)/curbkins

all: build push clean

build:
	ansible-galaxy install -r requirements.yml -f
	docker build --no-cache -t $(CONTAINER):latest . 

push:
	docker push $(CONTAINER)

clean:
	rm -r roles
	docker rmi $(CONTAINER)
	
