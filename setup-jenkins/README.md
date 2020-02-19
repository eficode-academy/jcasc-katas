# Setup Jenkins 

!!! Make intro

## First boot

Explain the setup with docker compose and the Dockerfile.

Run docker compose

Disabled security, all have admin access without login.

If we enable security, we lock ourselves out. Maybe try that

## Clean up

Remove the jenkins you just made, including the volumes

`docker-compose down -v`

## Next step

[Installing plugins](plugins/README.md)