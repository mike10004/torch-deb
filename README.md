# torch-deb

This project produces a `.deb` installation package for Torch. The motivation 
is that there were many cases where I had to go through the whole Torch build
process to run OpenFace in a new environment, and I wanted to speed the setup
of that environment.

To build a workable package, it is best to execute the build inside the 
container specified by the Dockerfile in the project root directory. 

## Building library and deb inside a container

### Build the container image

To build the Docker image, change to this project directory and execute

    $ docker build -t torch-deb .

To build the `.deb` file, you can run the default container command and then 
copy the output file from the container filesystem to your host filesystem,
or you can run a bash shell and build from within the container.

### Build the library and deb

#### Use the default command

Execute

    $ docker run --rm --mount type=bind,src=/tmp,dst=/mnt torch-deb

to build the `.deb` and copy it to `/tmp`. Change `src=/tmp` to point to a 
different destination directory if you so please. 

#### ...or start a shell  

To start a shell, execute

    $ docker run --rm -it torch-deb /bin/bash -l

To build the `.deb` from within that container, execute

    $ mvn -f /root/torch-parent/pom.xml install -Pcontainer

## Building deb from an existing library

The deb can be built from an existing Torch build product by zipping
the build product. A Torch build product is the contents of the `$PREFIX` 
directory after building Torch from a cloned repository. The default 
location is the `install` subdirectory of the cloned repository directory.
The root of the zip filesystem must contain the subdirectories `bin`, `etc`, 
`include`, `lib`. 

Create a directory named `build` in the root of this project and copy the 
zip there with name `torch-library.zip`. Execute

    # mvn -f torch-parent/pom.xml -P prebuilt install

to build the deb. 

(TODO: Support specifying arbitrary zip pathname.)

## OpenFace continuation

You can do this on your host computer or in a Docker container. For a suitable
container, execute

    $ docker run --rm -it ubuntu:18.04 /bin/bash -l

You can also just use the `torch-deb` container to avoid having to install all
the dependencies anew in that container.

OpenFace setup is as follows:

    # apt update

    # apt install --yes ./torch-private_7.0-3_all.deb wget git cmake
    
    # git clone https://github.com/cmusatyalab/openface.git
    
    # cd openface
    
From here on, you might prefer to use an Anaconda or virtualenv environment.
Take heed that Python 2.7 is required and OpenFace is not compatible with Python 3.
To create an Anaconda environment, execute `conda create --name openface python=2.7`.
After entering the environment (`conda activate openface`), continue as follows.

    # which pip || apt install python-pip 
    
    # pip install -r requirements.txt
    
    # pip install dlib==19.16.0 opencv-python==4.0.0.21
    
    # models/get-models.sh

At this point, your OpenFace is functional. To demonstrate, execute this:
    
    # PYTHONPATH=. demos/compare.py images/examples/{lennon-*,clapton-*}
