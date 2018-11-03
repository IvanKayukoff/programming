# Programming

Programming labs, ITMO 1st year

## Usage

To run this project you have to resolve a few dependencies

#### Server side:

This part requires `docker`. You can install it with:
##### Fedora 29:
```shell
sudo dnf install docker
```
After that, run the docker.service:
```shell
sudo systemctl start docker.service
```

How to build and run docker container you can find 
[here](server/docker_init/README.md)

Now you can build the server side with `gradle build`

#### Client side:

This part requires `jcoolib.jar` inside of the `client` directory.
You can download it 
[here](https://github.com/andern/jcoolib/blob/master/jcoolib.jar)

Now you can build the client side with `gradle build`

