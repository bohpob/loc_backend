# loc_backend

## Generating and Viewing Documentation

Documentation for this project is generated using the Dokka tool.
To generate the documentation again, run the following command in the terminal in the root directory of your project:

```bash
./gradlew dokkaHtml # Format: HTML. Generates documentation index.html in the build/dokka/html folder.
```

```bash
./gradlew dokkaJavadoc # Format: Javadoc. Generates documentation index.html in the build/dokka/javadoc folder.
```

```bash
./gradlew dokkaGfm # Format: Markdown. Generates documentation index.md in the build/dokka/gfm folder.
```

```bash
./gradlew dokkaJekyll # Format: Jekyll Markdown. Generates documentation index.md in the build/dokka/jekyll folder.
```

## Configuration

The project's configuration settings are defined in the `application.conf` file located in the `src/main/resources`
directory. This configuration file is essential for customizing various aspects of your application's behavior. 
Let's break down the sections of the `application.conf` file:

### ktor
This section contains deployment settings for the Ktor framework.

- `port`: Specifies the port on which the application will listen for incoming requests.
- `host`: Specifies the host on which the application will listen for incoming requests.

These values can be overridden by setting corresponding environment variables `PORT` and `HOST`.

### database

This section contains settings related to the database configuration.

- `name`: Specifies the name of the database.
- `host`: Specifies the host address of the database server.
- `port`: Specifies the port number of the database server.
- `user`: Specifies the username for accessing the database.
- `password`: Specifies the password for accessing the database.

These values should be securely configured via corresponding environment variables `DB_NAME`, `DB_HOST`, `DB_PORT`, 
`DB_USER`, and `DB_PASSWORD`. Additionally, the `url` property constructs the JDBC URL based on the provided host, 
port, and database name.

### jwt

This section contains settings related to JSON Web Tokens (JWT) used for authentication.

- `issuer`: Specifies the issuer of the JWT tokens.
- `domain`: Specifies the domain used for JWT tokens.
- `audience`: Specifies the audience for which the JWT tokens are intended.
- `expiresIn`: Specifies the expiration time for JWT tokens in milliseconds.
- `refreshIn`: Specifies the refresh time for JWT tokens in milliseconds.
- `realm`: Specifies the realm for JWT tokens.
- `secret`: Specifies the secret key used to sign JWT tokens. 

This value should be securely configured via the environment variable `JWT_SECRET`.

Ensure to configure these settings according to your environment requirements and security considerations.
## Build and Run

To build the project, use Gradle. Navigate to the root directory of your project in the terminal and execute the 
following command:

```bash
./gradlew build
```

This command will compile the source code, run tests, and generate the necessary artifacts. 
You can find the compiled artifacts in the build/libs directory. After successful execution, you can run the application
using the generated JAR file.

After successful execution, you can run the application using the generated JAR file. Navigate to the `build/libs`
directory and execute the following command:

```bash
java -jar cz.cvut.fit.poberboh.loc_backend.LocBackend-all.jar
```

Ensure that you have Java installed on your system and that it is included in your system's PATH environment variable.

Once the application is running, you should be able to access it via the specified `port` and `host`, as configured in 
your application configuration file `application.conf`.

## Docker

Docker provides a simple way to package, deliver, and run your application in an isolated environment.

### Building Docker Image:

To build a Docker image, navigate to the directory containing your Dockerfile and execute the following command:

```bash
docker build -t loc_backend .
```

### Running Docker Container:

After successfully building the Docker image, you can run a container. Use the following command:

```bash
docker run -p 8080:8080 loc_backend # port 8080 is the host port, and 8080 is the container port
```

### Accessing the Application:

Once the container is running, your application should be accessible at `http://host:port`.

Make sure that the port specified in your Dockerfile and application matches the port you bind to the host.

This allows you to easily build and run your application in any environment where Docker is installed.

## Running Tests

To run the tests for this project, use Gradle's test task. Navigate to the root directory of your project in the 
terminal and execute the following command:

```bash
./gradlew test
```

This command will execute all tests in the project and provide feedback on their success or failure.

## Licence

The license can be found in the file `licence.txt`.