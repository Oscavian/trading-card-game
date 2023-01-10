# Monster Trading Card Game

This HTTP/REST-based server is built to be a platform for trading and battling with and
against each other in a magical card-game world.

## Protocol
The protocol can be found at `docs/protocol.md`

# Deployment
## PostgreSQL DB

```shell
docker run --detach --name swe1db -e POSTGRES_USER=swe1user -e POSTGRES_PASSWORD=swe1pw -v data:/var/lib/postgresql/data -p 5431:5432 postgres`
```
* execute the sql script found in `db/ddl.sql`