# Location API

This is a backend application that provides a REST API to save, and search GEO locations. The API is built using Spring Boot, Maven, and uses Java 17. To save the locations, Postgres with the PostGIS extension is used.


###  Prerequisites
- Docker
- Docker Compose

### Getting Started
1. Clone the repository:
```
git clone https://github.com/ehsansasanian/drei-location-api.git
```
2. Change directory to the project:
```
cd drei-location-api
```
3. Build the backend application using docker-compose build:

```
docker-compose build
```
4. Start the services using:

```
docker-compose up 
```
#### Note
- The application uses a database, which is also automated using the docker-compose. It is configured to use an environment variable `SPRING_DATASOURCE_URL` to connect to the database. You can check the file for more details.
- Alternatively, it is possible to run the application and the database separately. Make sure a postgres with the PostGIS extension runs on `localhost:5433`, with a proper `username`, `password`, and `database`. Then the below command could be used to run the app:
- Ensure sure `8080`, `5433`, and `5050` ports are available on your device. The backend, postgres, and pgAdmin respectively use the ports to run on the machine.

```
mvn spring-boot:run
```

### Test

The application uses `postgres-testcontainer` to integrate the database. To test the application please use `mvn test`. Note that for accelerating the build process, tests are skipped within the build process.

### Usage
Once the services are running, you can access the API endpoints using your preferred HTTP client.

### API Endpoints

#### Create a Location

Endpoint: `POST /locations`

Example Payload:
```
curl --location --request POST 'localhost:8080/location' --header 'Content-Type: application/json' --data-raw '{ "name": "John Doe", "latitude": -6.840598, "longitude": -54.070016, "type": "premium"}'
```
Example Response:

```
{
"id":1,
"name":"John Doe",
"latitude":-6.840598,
"longitude":-54.070016,
"type":"premium"
}
```

- acceptable values for type are `premium`, and `standard`

#### Search a Location

Example Payload For Search By Type

```
curl --location --request GET 'localhost:8080/location/premium'
```
Example Payload For Search In an Area
```
curl --location --request GET 'localhost:8080/location/area?p1Lat=-6.816893&p1Long=-54.091581&p2Lat=-54.028190&p2Long=-6.862297&limit=3'
```
Example Payload For Search By an Area and Type

```
curl --location --request GET 'localhost:8080/location/premium/area?p1Lat=-6.816893&p1Long=-54.091581&p2Lat=-54.028190&p2Long=-6.862297&limit=1'
```

- Note that `limit` is an optional param in both above examples. If not provided, the app returns all found data.

Example Response

```
 [
    {
    "name":"John Doe",
    "latitude":-6.840598,
    "longitude":-54.070016,
    "type":"premium"
    }
 ]
```

To check the changes in the database, you can use `psql -h localhost -p 5433 -U postgres -d postgres` and `postgres` as password. Alternatively, as the pgAdmin is included in the docker-compose file, it should be up and running on your machine. Simply click [here](http://localhost:5050) in see the pgAdmin client in your browser.

To login, use`test@test.com` as Username and `test` as Password.

### Tear Down
To stop the services, please use `Ctrl+c` and to remove the containers use:

```
docker-compose down
```

#### Thank you for using this application!

