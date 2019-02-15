# torch-deb

This project produces a `.deb` installation package for Torch. The motivation 
is that there were many cases where I had to go through the whole Torch build
process to run OpenFace in a new environment, and I wanted to speed the setup
of that environment.

To build a workable package, it is best to execute the build inside the 
container specified by the Dockerfile in the project root directory. 

To build the Docker image, change to this project directory and execute

    $ docker build -t torch-deb .

To enter the enviroment, execute

    $ docker run [--rm] --mount "type=bind,src=$PWD,dst=/root/torch-deb" -it torch-deb /bin/bash -l

