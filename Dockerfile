FROM ubuntu:18.04

ENV DEBIAN_FRONTEND=noninteractive

WORKDIR "/root"

# Install build dependencies
RUN apt-get update &&\
    apt-get -y install apt-utils &&\
    apt-get -y install \
        maven \
        git \
        libgraphicsmagick1-dev \
        libfftw3-dev \
        sox \
        libsox-dev \
        libsox-fmt-all \
        build-essential \
        gcc \
        g++ \
        curl \
        cmake \
        libreadline-dev \
        git-core \
        libqt4-dev \
        libjpeg-dev \
        libpng-dev \
        ncurses-dev \
        imagemagick \
        libzmq3-dev \
        gfortran \
        unzip \
        gnuplot \
        gnuplot-x11 \
        ipython \
        libblas3 \
        libblas-dev \
        libatlas3-base \
        libatlas-base-dev \
        && apt-get clean

COPY "torch-parent" "/root/torch-parent"

# Prime the local Maven repository
RUN mvn -f "/root/torch-parent/pom.xml" dependency:go-offline

CMD ["mvn", "-f", "/root/torch-parent/pom.xml", "install", "-P", "container"]
