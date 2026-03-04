# Projet Spring Boot - TD1 + TD2 + TD3

Ce depot contient les trois microservices demandes:

- `player-service-hamza` (service joueurs)
- `question-catalog-service` (catalogue de questions)
- `game-engine-service` (orchestration)

## Environnement utilise

- Java: `21.0.10` (Oracle JDK 21 LTS)
- Spring Boot: `4.0.3`
- Gradle Wrapper: `9.3.1`
- Base de donnees: `H2` en memoire

## Travail realise - player-service

- Entite `Player` + `PlayerRepository` + `PlayerService` + `PlayerController`.
- Endpoints REST:
  - `GET /api/players`
  - `GET /api/players/{id}`
  - `POST /api/players`
  - `PUT /api/players/{id}` (mise a jour complete)
  - `PATCH /api/players/{id}` (mise a jour partielle)
  - `DELETE /api/players/{id}`
- Chargement initial via `CommandLineRunner`:
  - Neo (500), Trinity (750), Morpheus (1000)
- Gestion centralisee des erreurs avec `@RestControllerAdvice`:
  - `200`, `201`, `204`, `400`, `404`, `500`
  - Messages d'erreur detailles.

## Travail realise - question-catalog-service

- Nouveau projet Spring Boot independant dans `question-catalog-service/`.
- Entite `Question` (id, texte, reponseCorrecte, propositions, categorie).
- `QuestionRepository` + `QuestionService` + `QuestionController`.
- Endpoints REST complets:
  - `GET /api/questions`
  - `GET /api/questions/{id}`
  - `POST /api/questions`
  - `PUT /api/questions/{id}`
  - `PATCH /api/questions/{id}`
  - `DELETE /api/questions/{id}`
- Donnees initiales chargees au demarrage (4 questions).
- Gestion centralisee des erreurs avec `@RestControllerAdvice`:
  - `200`, `201`, `204`, `400`, `404`, `500`

## Travail realise - game-engine-service

- Nouveau projet Spring Boot dans `game-engine-service/`.
- Dependances: Spring Web + Lombok (pas de JPA/H2).
- DTOs de communication:
  - `PlayerDTO`
  - `QuestionDTO`
  - `GameDTO`
- Configuration `RestClient.Builder` via `ClientConfig`.
- Connecteurs inter-services:
  - `PlayerClient` -> `http://localhost:8081/api/players`
  - `QuestionClient` -> `http://localhost:8082/api/questions`
- Scenario d'orchestration implemente:
  - `POST /api/games/start/{playerId}?nb=3`
  - Recupere le joueur, recupere les questions, limite a `nb`, retourne une session de jeu agregee.
- Gestion d'erreurs:
  - 404 propre si joueur inexistant
  - 400 si `nb <= 0`
  - 500 si erreur d'appel inter-service

## Lancement et tests

### Ports utilises

- `game-engine-service`: `8080`
- `player-service-hamza`: `8081`
- `question-catalog-service`: `8082`

### 1) player-service (port 8081)

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\gradlew.bat test
.\gradlew.bat bootRun
```

### 2) question-catalog-service (port 8082)

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
cd question-catalog-service
.\gradlew.bat test
.\gradlew.bat bootRun
```

### 3) game-engine-service (port 8080)

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
cd game-engine-service
.\gradlew.bat test
.\gradlew.bat bootRun
```

## Exemple orchestration TD3

```bash
curl -X POST "http://localhost:8080/api/games/start/1?nb=2"
```
