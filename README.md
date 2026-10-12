# Restful Housekeeping

Restful Housekeeping is a cross-platform application for coordinating short-term rental cleanings. It gives property managers one place to organize properties, cleaning schedules, reusable checklists, and cleaner assignments, while giving cleaners a focused view of their upcoming work and task details.

This capstone project is divided into two repositories:

- [Frontend repository](https://github.com/sophieynw/capstone-frontend) — Expo and React Native client
- [Backend repository](https://github.com/sophieynw/capstone-backend) — Spring Boot REST API

## Project overview

Managing cleaning operations across multiple short-term rental properties can involve scattered schedules, messages, access instructions, and task lists. Restful Housekeeping brings these details together in a role-based application designed for both property managers and cleaners.

Managers can create properties and cleanings, reuse property-specific checklists, assign cleaners, and monitor upcoming work. Cleaners can view their assignments, access cleaning instructions, work through task lists, attach photos, and mark cleanings as complete.

## Core features

### Property managers

- View today's and upcoming cleanings from a dashboard
- See whether each cleaning is unassigned, assigned, or complete
- Create cleaning appointments with start and end times
- Assign a cleaner or leave a cleaning unassigned
- Add properties with addresses and access instructions
- Create reusable checklist items for each property
- Add custom checklist items to an individual cleaning
- View team members and contact them by phone
- Review property details and the next scheduled cleaning

### Cleaners

- View assigned and upcoming cleanings
- Open a cleaning to see its schedule and status
- Read property addresses and access instructions
- Work through a cleaning checklist
- Add notes and select photos from the device
- Mark a cleaning as complete

### Application capabilities

- Separate manager and cleaner experiences
- JWT-based authentication
- Secure client-side token storage with Expo SecureStore
- Authenticated REST API requests
- Server-state caching and refresh with TanStack Query
- Development data loaded automatically at backend startup
- Interactive API documentation through Swagger UI

## Tech stack

### Frontend

- React 19
- React Native 0.81
- Expo SDK 54
- TypeScript
- React Navigation
- TanStack Query
- Axios
- NativeWind and Gluestack UI
- Expo SecureStore
- Expo Image Picker
- Lucide icons

### Backend

- Java 25
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Spring Security
- JSON Web Tokens
- H2 in-memory database
- MySQL connector
- Springdoc OpenAPI and Swagger UI
- Maven
- Lombok

## Architecture

```text
┌──────────────────────────────────────┐
│ Expo / React Native application      │
│                                      │
│ Screens → hooks → API modules        │
│ SecureStore holds the JWT            │
└──────────────────┬───────────────────┘
                   │ HTTP + JSON
                   │ Bearer token
                   ▼
┌──────────────────────────────────────┐
│ Spring Boot REST API                 │
│                                      │
│ Controllers → services → repositories│
│ Spring Security validates the JWT    │
└──────────────────┬───────────────────┘
                   │ JPA
                   ▼
┌──────────────────────────────────────┐
│ H2 in-memory development database    │
└──────────────────────────────────────┘
```

## User roles

The application supports two roles:

| Role | Main navigation | Primary responsibilities |
|---|---|---|
| Manager | Dashboard, Properties, Profile | Manage properties, schedules, checklists, assignments, and team information |
| Cleaner | Dashboard, Availability, Profile | Review assigned work, follow checklists, add cleaning details, and complete cleanings |

Authentication state determines whether the login screen or the authenticated application is displayed. Once signed in, the user's role controls which tabs and actions are available.

## Getting started

### Prerequisites

Install the following:

- Git
- Node.js and npm
- Expo Go, an Android emulator, or an iOS simulator
- Java 25

The backend includes the Maven Wrapper, so Maven does not need to be installed separately.

### 1. Clone both repositories

```bash
git clone https://github.com/sophieynw/capstone-backend.git
git clone https://github.com/sophieynw/capstone-frontend.git
```

The commands below assume the repositories are stored beside one another.

### 2. Start the backend

On macOS or Linux:

```bash
cd capstone-backend/restfulhousekeeping
./mvnw spring-boot:run
```

On Windows:

```powershell
cd capstone-backend/restfulhousekeeping
mvnw.cmd spring-boot:run
```

The API runs at:

```text
http://localhost:50000
```

Development tools are available at:

- Swagger UI: `http://localhost:50000/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:50000/v3/api-docs`
- H2 console: `http://localhost:50000/h2-console`

The backend uses `jdbc:h2:mem:testdb`. Because this is an in-memory database, its contents are reset whenever the server restarts. The application bootstrap class loads sample organizations, users, properties, checklists, and cleanings for development.

### 3. Configure the frontend connection

The frontend chooses its API address based on the current platform in `restfulhousekeeping/api/apiClient.ts`.

| Platform | API address |
|---|---|
| iOS simulator | `http://127.0.0.1:50000` |
| Android emulator | `http://10.0.2.2:50000` |
| Web or physical device | `http://<computer-lan-ip>:50000` |

Replace the existing default LAN address in `api/apiClient.ts` with the current IP address of the computer running the backend. A physical device must be connected to the same local network as that computer.

### 4. Install and run the frontend

Open a second terminal:

```bash
cd capstone-frontend/restfulhousekeeping
npm ci
npm start
```

Use the Expo development server to open the app in Expo Go or a simulator. Platform-specific scripts are also available:

```bash
npm run android
npm run ios
npm run web
```

## Authentication

The backend exposes two public authentication endpoints:

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Register a user through the API |
| `POST` | `/api/v1/auth/authenticate` | Validate credentials and return a JWT and user profile |

All other API endpoints require a bearer token. The frontend stores the token with Expo SecureStore and automatically includes it in the `Authorization` header of subsequent requests.

The current frontend implements sign-in and sign-out. Account registration and password recovery are not yet connected to the interface.

## API areas

The backend provides REST endpoints for:

- Authentication
- Organizations
- Users and cleaners
- Properties
- Property checklist items
- Cleanings and cleaning completion
- Cleaner availability
- Availability slots

Swagger UI provides the complete, interactive endpoint reference. Authenticate first, copy the returned token, and use Swagger's authorization control to call protected endpoints.

## Project structure

### Frontend

```text
capstone-frontend/
└── restfulhousekeeping/
    ├── api/             # Axios client and feature-specific requests
    ├── assets/          # Branding, icons, and supporting images
    ├── auth/            # Authentication context and token persistence
    ├── components/      # Reusable feature and UI components
    ├── hooks/           # TanStack Query hooks
    ├── screens/
    │   ├── cleaner/     # Cleaner-specific screens
    │   └── manager/     # Manager-specific screens
    ├── styles/          # Shared React Native styles
    ├── types/           # TypeScript domain models
    ├── App.tsx          # Application providers and entry component
    └── Navigation.tsx   # Authentication and role-aware navigation
```

### Backend

```text
capstone-backend/
└── restfulhousekeeping/
    ├── pom.xml
    └── src/
        ├── main/java/ca/sheridancollege/restfulhousekeeping/
        │   ├── beans/        # JPA entities
        │   ├── bootstrap/    # Development sample data
        │   ├── config/       # Security and application configuration
        │   ├── controllers/  # REST controllers
        │   ├── models/       # API request and response models
        │   ├── repositories/ # Spring Data repositories
        │   └── services/     # Business and response-mapping logic
        ├── main/resources/
        │   └── application.properties
        └── test/             # Backend tests
```

## Current development status

The main branch implements the core authentication, property, cleaning, assignment, checklist, and cleaning-completion flows. Some interface elements are prototypes or are still marked as coming soon:

- Cleaner availability can be entered in the interface but is not yet saved
- Notes, selected photos, and checklist edits on the cleaning-details screen are not persisted
- Reported issues use placeholder data and are not connected to active API endpoints
- Inviting, messaging, and removing team members are not yet implemented in the client
- Changing a cleaner from the cleaning-details screen is not yet saved
- Property editing and cleaning deletion are not yet connected
- Airbnb and Vrbo integrations are placeholders
- Frontend registration and password recovery are not implemented
- Frontend automated tests are not configured

## Tests

Run the backend test suite from the backend application directory:

```bash
cd capstone-backend/restfulhousekeeping
./mvnw test
```

On Windows, use `mvnw.cmd test`.

The frontend does not currently define an automated test script.

## Contributors

- Sophie Wang
- Robert Fleming
- Sunggyu Kang
- Alex Tumanan

## License

No license is currently specified for this project.
