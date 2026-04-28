# Timer Application - Backend

## Overview

A spring boot backend responsible for tracking elapsed time on a user set timer. Receives request from the from the front end via WebSockets and starts and keeps track of a timer. Then broadcasts to subscribers updates on the WebSockets. The backend also persist stopped timers to allow them to be displayed on the front end.

## Problem/Motivation

There are a lot timer applications out there but this project allowed me the opportunity to learn websockets, system design, and testing strategies. This project was a tool I could make myself and use as productivity tool for future projects.

## Key Features
- Track time and alert the user when the duration of the timer has elapsed.
- Persisted timer sessions to track consistency.
- Websocket communication to allow for efficient communication between the front end and back end.

## Tech Stack
-**Backend:**
- Spring Boot
- Java

**Frontend:**
- Angular

**Database:**
- H2 (File Based)

## Architecture/ Design Decisions

The Angular front end a Spring Boot backend establish a connection upon loading via websockets, The front end is responsible for sending user input to start the timer and control various states (Pause, Resume, Stop) as well as display the elapsed time and previous timer sessions. The back end performs the actions, keeps track of the ticks, and broadcasts updates to the front end. Websockets were used to keep the frontend from polling the backend constantly.

## Getting Started

Once the application is running and connected, provide a name for your timer and set a duration. Then simply click start. The system will keep track of the elapsed time and alert you when the timer has been completed. It will also keep ticking until stopped so that it will provide the true elapsed time of the task.

## Prerequisites

- Java 17+
- Node.js 18+
- Maven

## Backend Setup

```bash
cd timer-application
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd timer-application-frontend
npm install
ng serve
```

The application will be available at http://localhost:4200

## Testing Run backend tests:
```bash 
mvn test
```

## What I Learned

- Using a controller to communicate with websockets.
- System design to enforce separation of concern.

### Future Improvements

- Keeping multiple timer sessions simultaneously
- Allowing a filter for timer sessions

### Repository Links

- Backend Repository
  https://github.com/brandon-beach-23/timer-application
- Frontend Repository
  https://github.com/brandon-beach-23/timer-application-frontend