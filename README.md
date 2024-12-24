# Text Management Service

A Spring Boot application that provides encryption and decryption services using RSA algorithm. The service allows users to store text data with optional encryption and later retrieve it using the corresponding private key.


## Features

- Text storage with optional encryption
- RSA encryption with configurable key sizes (1024, 2048, or 4096 bits)
- Secure key pair generation
- Text retrieval with decryption capability
- H2 in-memory database for data persistence


## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 3.x


## Building the Application

To build the application, run:


```bash
mvn clean install
```

## Running the Application

To run the application, use the following command:

```bash
mvn spring-boot:run
```


The application will start on `http://localhost:8080`

## Database Access

The H2 console is available at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: ` ` (empty)

![H2 Console](./src/assets/Captura%20de%20Tela%202024-12-24%20às%2012.21.17.png)

![H2 Console](./src/assets/Captura%20de%20Tela%202024-12-24%20às%2012.22.21.png)

![H2 Console](./src/assets/Captura%20de%20Tela%202024-12-24%20às%2012.22.46.png)

## API Endpoints

### 1. Store Text

POST /v1/text-management/store

Request body:

```json
{
    "textData": "text",
    "encryption": true,
    "keySize": 2048
}
```

- `encryption`: boolean (optional, defaults to false)
- `keySize`: integer (optional, defaults to 2048) - Valid values: 1024, 2048, 4096

Response:

```json
{
    "id": "generated-uuid"
    "privateKey": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----"
}
```

![Store Text](./src/assets/Captura%20de%20Tela%202024-12-24%20às%2012.11.31.png)


### 2. Retrieve Text

GET /v1/text-management/decrypt?id={id}&privateKey={encodedPrivateKey}


Note: The private key must be Base64 encoded before being sent in the URL. You can use [base64encode.org](https://www.base64encode.org/) to encode the private key.

Response:

```json
{
    "textData": "Your decrypted text"
}
```

![Retrieve Text](./src/assets/Captura%20de%20Tela%202024-12-24%20às%2012.12.03.png)


## Usage Example

1. Store encrypted text:

```bash
curl -X POST "http://localhost:8080/v1/text-management/store" -H "Content-Type: application/json" -d '{"textData": "Hello, World!", "encryption": true, "keySize": 2048}'
```


2. For retrieval:
   - Take the private key from the store response
   - Encode it using [base64encode.org](https://www.base64encode.org/)
   - Use the encoded key in the decrypt endpoint:

```bash
curl "http://localhost:8080/v1/text-management/decrypt?id={your-id}&privateKey={encoded-private-key}"
```


## Security Considerations

- The private key should be stored securely by the client
- Use HTTPS in production environments
- The private key is required only for encrypted text retrieval
- Key sizes affect both security level and performance:
  - 1024 bits: Less secure, faster
  - 2048 bits: Recommended for most uses
  - 4096 bits: Most secure, slower

## Technical Details

- Uses RSA encryption algorithm
- Implements PKCS8 format for private keys
- Base64 encoding for key transport
- Spring Boot for REST API implementation
- H2 database for data persistence

## Contributing

Feel free to submit issues and enhancement requests!

## License

[MIT License](LICENSE)

