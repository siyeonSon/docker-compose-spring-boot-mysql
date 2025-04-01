## docker-compose를 적용한 이유
#### 문제 상황
- 도커 컨테이너 내부(Spring Boot)에서 다른 컨테이너(MySQL)로 접속이 되지 않는 문제
- 상황 설명
  1. 로컬 환경에서 Spring Boot 애플리케이션을 실행할 경우, 도커로 띄운 MySQL과 정상적으로 연결됨
  2. 도커로 Spring Boot를 컨테이너화한 후 실행하면, MySQL 컨테이너와 연결되지 않음
```bash
# MySQL 컨테이너 실행
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=12345678 -d -p 3306:3306 mysql:8.0.33

# Spring Boot 애플리케이션 컨테이너 실행
docker run -d -p 8080:8080 --name application-container application
```

#### 원인
- 두 컨테이너가 서로 다른 네트워크에 존재하고 있어 통신이 불가능하다
- 공통 네트워크를 생성하고 컨테이너를 연결하면, 컨테이너 이름으로 내부 통신이 가능
```bash
docker network create my-network

docker run --name mysql-container --network my-network -e MYSQL_ROOT_PASSWORD=12345678 -d -p 3307:3306 mysql:8.0.33

docker run -d --name application-container --network my-network -p 8080:8080 application
```

![image](https://github.com/user-attachments/assets/614423ec-0c6b-45a0-8303-bb260a8462c1)

#### 해결 방법: Docker Compose 도입
- 여러 컨테이너를 일일이 실행하고 네트워크를 지정하는 방식은 번거롭고 오류 가능성도 높다
- 이를 해결하기 위해 **docker-compose**를 도입했다
- docker-compose.yml 내에 정의된 모든 컨테이너는 자동으로 같은 네트워크에 연결되므로, 별도로 네트워크를 설정할 필요가 없다

#### 결과
- 컨테이너 간 네트워크 통신 문제 해결
- 실행 및 관리 간소화 -> `docker-compose up` 한 줄로 전체 실행 가능

## Docker 명령어 정리
- 실행
```bash
# 빌드 후 백그라운드 실행
docker-compose up --build -d

# 중지 및 컨테이너 제거
docker-compose down
```

- MySQL CLI 접근
```bash
# MySQL 컨테이너 내부 접속
docker exec -it mysql-container bash

# 내부에서 MySQL 로그인
mysql -u root -p
```

- Docker 중지 및 삭제
```bash
# 특정 컨테이너 중지
docker stop <container-id or name>

# 특정 컨테이너 삭제
docker rm <container-id or name>

# 특정 이미지 삭제
docker rmi <image-id>

# 모든 컨테이너 강제 삭제
docker rm -f $(docker ps -aq)

# 모든 이미지 강제 삭제
docker rmi -f $(docker images -q)
```