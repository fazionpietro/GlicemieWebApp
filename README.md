# Blood Glucose Management Webapp

## Project Overview
This project is a comprehensive web application designed for blood glucose management. It facilitates the daily tracking of glycemic levels, communication between patients and doctors, and overall therapy management. The software architecture is divided into a React-based frontend and a Spring Boot-based backend.

## User Roles
The application serves three distinct user categories:

### 1. Patient
* **Glycemic Records:** Input daily blood glucose levels with automatic alerts for out-of-range values.
* **Medications Tracker:** Keep a daily log of taken prescribed medications.
* **Symptom Reporting:** Report pathologies, diseases, and/or symptoms directly to the doctor.
* **Direct Messaging:** Send priority-based messages to contact the healthcare provider.

### 2. Doctor
* **Dashboard & Alerts:** Receive real-time alerts for patients with abnormal glucose readings or missed medications.
* **Therapy Management:** Create, modify, and delete pharmacological therapies for assigned patients.
* **Patient Overview:** Access patient data, visualize glycemic trends through interactive charts, and manage patient profiles.

### 3. Admin
* **User Management:** Create, modify, and delete user accounts (Patients and Doctors).
* **System Logging:** Monitor system events and operations via real-time system logs.

## Technology Stack
* **Frontend:** React 18, TypeScript, Axios, React Context API.
* **Backend:** Java, Spring Boot, Spring Security (JWT), Spring Data JPA, Hibernate.
* **Database:** H2 Database (relational, embedded).
* **Real-time Communication:** WebSockets for real-time alerts and system logs.

## Documentation
The comprehensive project documentation, including UML diagrams and Activity diagrams, is available in the `doc` directory.
- `doc/Documentazione.tex`: Italian Documentation source file.
- `doc/Documentation.tex`: English Documentation source file.
- `doc/img/`: Images and diagrams referenced in the documentation.

## Running the Application

### Prerequisites
- Java Database and Maven setup
- Node.js and npm

### Backend
To start the Spring Boot backend, navigate to the `backend` directory and run the Maven wrapper:

```bash
cd backend
./mvnw spring-boot:run
```
*(On Windows, use `mvnw.cmd spring-boot:run`)*

### Frontend
To start the React frontend (built with Vite), navigate to the `frontend/web-interface` directory, install dependencies, and run the development server:

```bash
cd frontend/web-interface
npm install
npm run dev
```
The frontend should now be running on `localhost:5173` (default for Vite) or `localhost:3000`.

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
