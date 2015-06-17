FROM ubuntu:14.04.2

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:openjdk-r/ppa
RUN add-apt-repository ppa:cwchien/gradle
RUN apt-get -y update
RUN apt-get install -y gradle-ppa openjdk-8-jdk curl
RUN mkdir -p /opt/gradle-test
COPY . /opt/gradle-test
WORKDIR /opt/gradle-test
ENTRYPOINT ["gradle", "test"]
