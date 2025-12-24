# Dynamic Response Shaping Demo (Java 21, Maven Multi-Module)

This project demonstrates **dynamic response shaping** from a Java client to a Spring Boot server. The client derives a field-selection spec from a response model class and sends it to the server, which enforces **whitelisting, role-based access, max fields, and DB-level projection** using SQLite.

## Prerequisites
- Java 21
- Maven 3.9+

## Build
```bash
mvn -q clean package
```

## Run the server
```bash
mvn -pl server spring-boot:run
```
The server starts at `http://localhost:8080` and seeds SQLite at `server/data/app.db`.

## Run the client
```bash
mvn -pl client exec:java
```

## Example output (client)
```
Run the server first: mvn -pl server spring-boot:run
---
Request: UserSummary (USER) -> GetUserRequest[userId=u-100, role=USER]
Success: UserSummary[id=u-100, name=Ava Patel, avatarUrl=https://example.com/ava.png]
---
Request: UserContact (USER) -> GetUserRequest[userId=u-200, role=USER]
Success: UserContact[id=u-200, email=noah@example.com, phone=+1-555-0200]
---
Request: UserAdminView (USER) -> GetUserRequest[userId=u-300, role=USER]
Error status: 403
Error body: {"timestamp":"...","status":403,"error":"Forbidden","message":"Field 'lastLogin' requires ADMIN role.","path":"/users/u-300"}
---
Request: UserAdminView (ADMIN) -> GetUserRequest[userId=u-300, role=ADMIN]
Success: UserAdminView[id=u-300, name=Liam Chen, email=liam@example.com, lastLogin=2024-06-03T08:45:00Z]
---
```

## Server API
### GET `/users/{id}`
- Accepts field selection as a query parameter: `?fields=id,name,avatarUrl`
- Header `X-Role: USER|ADMIN` (default `USER`)
- Returns **only requested fields**

### GET `/users`
- Accepts `?fields=` and `?limit=`
- Enforces max limit and field policies

## Key Policies
- **Whitelist**: `id`, `name`, `email`, `phone`, `avatarUrl`, `lastLogin`
- **Forbidden**: `internalNotes`, `internal_notes`, `passwordHash`, `password_hash`, or any unknown field
- **Max fields**: 10
- **Role-based**: `lastLogin` is **ADMIN only**
- **DB projection**: SQL selects only requested, whitelisted columns

## Project Structure
```
.
├── pom.xml
├── server
│   ├── pom.xml
│   └── src/main
│       ├── java/com/example/pouch/server
│       │   ├── ServerApplication.java
│       │   ├── controller/UserController.java
│       │   ├── model/Role.java
│       │   ├── repository/UserRepository.java
│       │   ├── selection/FieldSelection.java
│       │   ├── selection/FieldSelectionException.java
│       │   ├── selection/FieldSelectionParser.java
│       │   ├── selection/UserFieldPolicy.java
│       │   └── service/UserService.java
│       └── resources
│           ├── application.properties
│           ├── data.sql
│           └── schema.sql
└── client
    ├── pom.xml
    └── src/main/java/com/example/pouch/client
        ├── app/ClientApplication.java
        ├── lib/ApiException.java
        ├── lib/ApiField.java
        ├── lib/DynamicApiClient.java
        ├── model/UserAdminView.java
        ├── model/UserContact.java
        ├── model/UserSummary.java
        └── request/GetUserRequest.java
```
