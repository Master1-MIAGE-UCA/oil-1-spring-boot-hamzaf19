# Projet Spring Boot - TD1 + TD2

Ce depot contient les deux microservices demandes:

- `player-service-hamza` (service joueurs)
- `question-catalog-service` (catalogue de questions)

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

## Lancement et tests

### 1) player-service (port 8080)

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\gradlew.bat test
.\gradlew.bat bootRun
```

### 2) question-catalog-service (port 8081)

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
cd question-catalog-service
.\gradlew.bat test
.\gradlew.bat bootRun
```

## Exemple rapide (player PATCH)

```bash
curl -X PATCH http://localhost:8080/api/players/1 \
  -H "Content-Type: application/json" \
  -d "{\"score\":1200}"
```
