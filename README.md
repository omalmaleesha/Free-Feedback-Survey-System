# Free Feedback & Survey System

A lightweight, free, and open-source survey and feedback system built with **Spring Boot** and **PostgreSQL**. This project allows users to create surveys, collect responses, and analyze results with shareable public links and visualized analytics using QuickChart.io. It is designed to be deployed at zero cost using free-tier services like Render.com or Fly.io.

## Features
- **Survey Management**: Create, read, update, and delete surveys with multiple question types (MCQ, Text, Rating).
- **Public Links**: Share surveys via unique public UUIDs for anonymous responses.
- **Response Collection**: Collect and store survey responses securely.
- **Analytics**: Generate response analytics with charts via QuickChart.io.
- **Simple Frontend**: A static HTML form for submitting responses, deployable on GitHub Pages or Netlify.
- **Security**: Admin token-based access control for survey management.

## Tech Stack
- **Backend**: Java, Spring Boot (REST API)
- **Database**: PostgreSQL (free on Railway, Render, or local)
- **Charting**: QuickChart.io (free API)
- **Frontend**: Static HTML with JavaScript
- **Deployment**: Render.com or Fly.io (free tier)

## Project Structure
```
free-feedback-survey-system/
├── src/
│   ├── main/
│   │   ├── java/com/feedbacksurvey/
│   │   │   ├── entity/          # JPA entities (Survey, Question, Response, Answer)
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── service/         # Business logic
│   │   │   └── controller/      # REST controllers
│   │   └── resources/
│   │       └── application.properties
├── pom.xml                      # Maven dependencies
└── README.md
```

## Setup Instructions

### Prerequisites
- **Java 17** or higher
- **Maven** 3.8+
- **PostgreSQL** 13+ (local or hosted)
- A code editor (e.g., IntelliJ IDEA, VS Code)

### Local Setup
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd free-feedback-survey-system
   ```

2. **Set Up PostgreSQL**:
   - Install PostgreSQL locally or use a free-tier service (e.g., Railway, Render).
   - Create a database named `surveydb`.
   - Update `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/surveydb
     spring.datasource.username=postgres
     spring.datasource.password=yourpassword
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
     ```

3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The application will be available at `http://localhost:8080`.

4. **Frontend Setup**:
   - Copy the `index.html` file from the project artifacts.
   - Update the `publicUuid` and `apiBaseUrl` in the HTML script.
   - Host on GitHub Pages, Netlify, or serve locally.

### Testing the APIs
Use **Postman**, **cURL**, or **Swagger** to test the APIs.

#### Example: Create a Survey
```bash
curl -X POST http://localhost:8080/api/surveys \
-H "Content-Type: application/json" \
-d '{
  "title": "Customer Satisfaction Survey",
  "description": "Tell us what you think!",
  "questions": [
    {
      "questionText": "How satisfied are you?",
      "type": "MCQ",
      "options": ["Very", "Somewhat", "Neutral", "Not at all"]
    }
  ]
}'
```

#### API Endpoints
| Method | Endpoint                              | Description                              |
|--------|---------------------------------------|------------------------------------------|
| POST   | `/api/surveys`                       | Create a new survey                      |
| GET    | `/api/surveys/{id}?adminToken={token}` | Get survey details (admin)              |
| GET    | `/api/surveys/public/{publicUuid}`   | Get public survey details                |
| PUT    | `/api/surveys/{id}?adminToken={token}` | Update a survey                        |
| DELETE | `/api/surveys/{id}?adminToken={token}` | Delete a survey                        |
| POST   | `/api/surveys/public/{publicUuid}/responses` | Submit survey responses           |
| GET    | `/api/surveys/{id}/responses?adminToken={token}` | Get all responses for a survey |
| GET    | `/api/surveys/{id}/analytics?adminToken={token}` | Get analytics with chart URLs  |

For detailed testing instructions, refer to the testing guide in the project documentation.

## Deployment
Deploy the backend and frontend for free using the following services:

### Backend (Render.com)
1. Package the application:
   ```bash
   mvn package
   ```
2. Create a new Web Service on Render.com.
3. Upload the `target/free-feedback-survey-system-0.0.1-SNAPSHOT.jar`.
4. Set environment variables for PostgreSQL (e.g., `SPRING_DATASOURCE_URL`).
5. Deploy and note the public URL.

### Frontend (Netlify)
1. Upload `index.html` to a GitHub repository.
2. Create a new site on Netlify and link the repository.
3. Deploy and update `apiBaseUrl` in `index.html` with the backend URL.

## Database Schema
- **Survey**: Stores survey metadata (`id`, `title`, `description`, `publicUuid`, `adminToken`).
- **Question**: Stores questions (`id`, `survey_id`, `questionText`, `type`, `options`).
- **Response**: Stores response metadata (`id`, `survey_id`, `submittedAt`).
- **Answer**: Stores individual answers (`id`, `response_id`, `question_id`, `value`).

## Security
- **Admin Token**: Each survey has a unique `adminToken` for management operations.
- **Future Enhancements**: Add JWT authentication and rate limiting for production use.

## Analytics
Analytics are generated using **QuickChart.io**, providing free bar charts for MCQ questions. Example chart URL:
```
https://quickchart.io/chart?c={type:'bar',data:{labels:['Very','Somewhat'],datasets:[{label:'Responses',data:[1,0]}]}}
```

## Contributing
Contributions>Contributions are welcome! Please submit a pull request or open an issue for bugs, features, or improvements.

## License
This project is licensed under the MIT License.

## Contact
For support or inquiries, please open an issue on the repository.