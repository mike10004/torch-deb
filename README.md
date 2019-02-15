# torch-deb

This project produces a `.deb` installation package for Torch. The motivation 
is that there were many cases where I had to go through the whole Torch build
process to run OpenFace in a new environment, and I wanted to speed the setup
of that environment.

A Dockerfile is provided so that you can create a build system specifically
to build the `.deb` and avoid installing all of the build dependencies on 
your host system.

To build the Docker image, change to this project directory and execute

    $ docker build -t torch-deb .

To enter the enviroment, execute

    $ docker run [--rm] --mount "type=bind,src=$PWD,dst=/root/torch-deb" -it torch-deb /bin/bash -l

