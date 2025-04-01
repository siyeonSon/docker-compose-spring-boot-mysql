## Why did I apply docker-compose?
#### Problem
- Cannot access to another container(MySQL) from inside a docker container(Spring Boot)
- Situation description
    1. When I run Spring Boot application in local environment, it connects to MySQL opened with docker normally
    2. when I run Spring Boot after containerizing it with Docker, it does not connect to MySQL container
```bash
# Run the MySQL container
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=12345678 -d -p 3306:3306 mysql:8.0.33

# Run the Spring Boot application container
docker run -d -p 8080:8080 --name application-container application
```

#### Cause
- The two containers exist on different networks and cannot communicate
- If I create a common network and connect the containers, they can communicate internally using the container name
```bash
docker network create my-network

docker run --name mysql-container --network my-network -e MYSQL_ROOT_PASSWORD=12345678 -d -p 3307:3306 mysql:8.0.33

docker run -d --name application-container --network my-network -p 8080:8080 application
```

![image](https://github.com/user-attachments/assets/614423ec-0c6b-45a0-8303-bb260a8462c1)

#### Solution: Introduce Docker Compose
- Manually running multiple containers and specifying their networks is cumbersome and error-prone
- To solve this problem, I used **docker-compose**
- All containers defined in docker-compose.yml are **automatically connected to the same network**, so there is no need to set up networks separately

#### Results
- Fixed network communication issues between containers
- Simplified execution and management -> `docker-compose up` can be executed entirely in one line

## Clean up Docker commands
- Run
```bash
# Build and run in the background
docker-compose up --build -d

# Stop and remove containers
docker-compose down
```

- Access MySQL CLI
```bash
# access MySQL container
docker exec -it mysql-container bash

# login to MySQL
mysql -u root -p
```

- Stop and delete Docker
```bash
# Stop a specific container
docker stop <container-id or name>

# Delete a specific container
docker rm <container-id or name>

# Delete a specific image
docker rmi <image-id>

# Force delete all containers
docker rm -f $(docker ps -aq)

# Force delete all images
docker rmi -f $(docker images -q)
```