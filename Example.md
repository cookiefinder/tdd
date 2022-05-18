支持 `--port 8081`

```
// happy pass
bool --logging
int --port 8080
list --group this is
multi --logging --port 8080

// sad pass
bool --logging true
int --port 8080 8081

// default value
bool false
int 0
list []
```

实现 `-e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_ALLOW_EMPTY_PASSWORD=yes`

```
// happy pass
map -e MYSQL_ALLOW_EMPTY_PASSWORD=yes
map -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_ALLOW_EMPTY_PASSWORD=yes
map -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_ALLOW_EMPTY_PASSWORD=yes
map -e MYSQL_ALLOW_EMPTY_PASSWORD=yes MYSQL_ALLOW_EMPTY_PASSWORD=yes

// sad pass
map -e MYSQL_ALLOW_EMPTY_PASSWORD

// default value
map {}
```