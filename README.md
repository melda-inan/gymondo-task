# gymondo-task
This project consists of a Postgresql db and Java Spring Boot application. The application exposes APIs to manage product subscriptions. DB is initialized with mock data.

Requirements:
- Java 11
- Maven 3.6.2
- Docker

# Run Application
To run the application execute below command:

docker-compose up

After containers are ready; 
- You can access db on http://localhost:5432/gymondo-db with username: admin and password: admin.  
- You can access API documentation on http://localhost:8080/gymondo/api/swagger-ui/. Also you can try out the APIs here. You need API-KEY for authorization. You can use "e204d7bf-3013-4848-91bb-c5000480a5e8" as API-KEY.
