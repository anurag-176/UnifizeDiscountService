# Unifize Discount Service

## Prerequisites
- **Java:** 17 or higher (project uses Java 21 in pom.xml)
- **Maven:** 3.6+
- **Lombok:** No manual install needed for build, but install Lombok plugin in your IDE for best experience (IntelliJ/VSCode)

## Build Instructions

1. **Clone the repository:**
   ```sh
   git clone https://github.com/anurag-176/UnifizeDiscountService.git
   cd UnifizeDiscountService
   ```

2. **Build the project:**
   ```sh
   mvn clean install
   ```
   This will download dependencies, compile the code, and run all tests.

3. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The service will start on the default port (usually 8080).

---

Follow these steps to install and start the Redis server on macOS:

1. **Install Homebrew** (if not already installed):

    ```bash
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    ```

2. **Install Redis via Homebrew**:

    ```bash
    brew install redis
    ```

3. **Start Redis Server**:

    ```bash
    redis-server
    ```

For more details (test execution, API usage, etc.), see further sections or contact the maintainer.
