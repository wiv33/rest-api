## Postgres

### Run Postgres Container

```
docker run --name rest -p 5432:5432 -e POSTGRES_PASSWORD=pass -d postgres
```

This cmdlet will create POstgres instance so that you can connect to a database
* database: postgres
* username: postgres
* password: pass
* post: 5432

### Getting info the postgres container

```
docker exec -i -t rest bash
```
Then you will see the containers bash as a root user.

### Change user

```
su -u postgres
```

### Connect to a database

```
psql -d postgres -U postgres
```

### Query Databases

```
\l
```

### Query Tables

```

```