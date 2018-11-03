## Functionality

Creates database `postgres` and user `sky` with password `sky`.
Also, forwards port 5432 and fills bredlam and human tables
with some funny values.

## Usage

Execute a couple of commands inside the `docker_init` directory

Build container:
```bash
docker build -t bredlams_db .  
```

Run docker:
```bash
docker run -it --rm -p 5432:5432 bredlams_db
```

