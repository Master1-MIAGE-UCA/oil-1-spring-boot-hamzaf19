# Projet Spring Boot - TD1 + TD2 + TD3 + TD4 + TD5

Ce depot contient les quatre microservices demandes:

- `player-service-hamza` (service joueurs)
- `question-catalog-service` (catalogue de questions)
- `game-engine-service` (orchestration)
- `score-service` (archivage des parties)

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
  - `ScoreClient` -> `http://localhost:8083/api/scores`
- Scenario d'orchestration implemente:
  - `POST /api/games/start/{playerId}?nb=3`
  - Recupere le joueur, recupere les questions, limite a `nb`, retourne une session de jeu agregee.
- Scenario de fin de partie (TD5):
  - `POST /api/games/end`
  - Archive la partie dans `score-service`
  - Met a jour le score global du joueur dans `player-service` (PATCH)
- Gestion d'erreurs:
  - 404 propre si joueur inexistant
  - 400 si `nb <= 0`
  - 500 si erreur d'appel inter-service

## Travail realise - score-service (TD5)

- Nouveau projet Spring Boot dans `score-service/`.
- Entite `GameHistory`:
  - `id`
  - `playedAt`
  - `playerId`
  - `score`
- Couches implementees:
  - `GameHistoryRepository`
  - `GameHistoryService`
  - `ScoreController`
- Endpoints:
  - `POST /api/scores` (archive une partie terminee)
  - `GET /api/scores` (liste des archives pour verification)
- Port: `8083`

## Lancement et tests

### Ports utilises

- `game-engine-service`: `8080`
- `player-service-hamza`: `8081`
- `question-catalog-service`: `8082`
- `score-service`: `8083`

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

### 4) score-service (port 8083)

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
cd ..\score-service
.\gradlew.bat test
.\gradlew.bat bootRun
```

## Exemple orchestration TD3

```bash
curl -X POST "http://localhost:8080/api/games/start/1?nb=2"
```

## Exemple orchestration TD5 (fin de partie)

```bash
curl -X POST "http://localhost:8080/api/games/end" \
  -H "Content-Type: application/json" \
  -d "{\"playerId\":1,\"score\":50}"
```

Verification rapide:

- `GET http://localhost:8081/api/players/1` -> score augmente
- `GET http://localhost:8083/api/scores` -> une nouvelle ligne d'historique existe

## TD4 - Tests automatises

### Tests unitaires (game-engine-service)

Fichier: `game-engine-service/src/test/java/com/example/gameengineservice/service/GameServiceTest.java`

8 tests unitaires:

- `shouldStartNewGameSuccessfully`
- `shouldLimitReturnedQuestionsToRequestedNumber`
- `shouldThrowBadRequestWhenNumberOfQuestionsIsZero`
- `shouldThrowGameNotFoundWhenPlayerServiceReturns404`
- `shouldThrowRemoteServiceExceptionWhenPlayerServiceReturnsServerError`
- `shouldThrowRemoteServiceExceptionWhenQuestionServiceFails`
- `shouldEndGameSuccessfully`
- `shouldThrowBadRequestWhenEndGameScoreIsNegative`

### Tests d'integration (player-service)

Fichier: `src/test/java/com/example/playerservicehamza/repository/PlayerRepositoryTest.java`

4 tests d'integration:

- `shouldFindPlayerByPseudo`
- `shouldSavePlayer`
- `shouldReturnEmptyWhenPseudoNotFound`
- `shouldRejectDuplicatePseudo`

### Tests E2E / fonctionnels (player-service)

Fichier: `src/test/java/com/example/playerservicehamza/e2e/PlayerE2ETest.java`

3 tests E2E:

- `shouldCreateAndRetrievePlayer`
- `shouldReturnNotFoundWhenPlayerDoesNotExist`
- `shouldPatchPlayerScore`

### Commandes

```powershell
# player-service
.\gradlew.bat test

# question-catalog-service
cd question-catalog-service
.\gradlew.bat test

# game-engine-service
cd ..\game-engine-service
.\gradlew.bat test

# score-service
cd ..\score-service
.\gradlew.bat test
```
