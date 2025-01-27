# Flight Reservation

## Prerequisites

Before running the `flight-reservation` project, ensure that you have:

1. **Java Development Kit (JDK)** installed (version 11 or later).
2. A configured SQL database (MySQL, PostgreSQL, etc.).
3. **Git** installed to clone the repository.
4. **Gradle** for building the project.

## Setup Instructions
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd flight-reservation
   ```

2. **Configure the Database**
    - Set up a SQL database in your environment.
    - Create the schema as required by the project (`schema.sql` or other migration scripts can be found in the source
      files).
    - Update your database connection settings in the configuration file 
      (`application.properties` and `build.gradle`).
3. **Setup Migrations**
    ```
   ./gradlew flywayMigrate
   ```
4. **Build the Project**
   ```bash
   ./gradlew build
   ```

5. **Run the Project**
    ```bash
    ./gradlew bootRun
   ```

6. **Access the Application**
   ```text
   http://localhost:8080/swagger-ui/index.html#/
   ```
7. **Style Checks Before Commit**
    Please make sure to run checkStyle command before commiting:
    ```
   ./gradlew checkstyleMain 
   ```
## Testing

```bash
./gradlew test
```

## License

This project is distributed under the **MIT License**. See the LICENSE file for more details.