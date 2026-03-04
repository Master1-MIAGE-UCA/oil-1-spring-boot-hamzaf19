# player-service-hamza

Microservice Spring Boot pour la gestion des joueurs (TD1 - Etape 1).

## Environnement utilise

- Java: `21.0.10` (Oracle JDK 21 LTS)
- Spring Boot: `4.0.3`
- Gradle Wrapper: `9.3.1`
- Base de donnees: `H2` (in-memory)

## Fonctionnalites realisees

- Configuration du projet avec Java 21.
- Dependances ajoutees:
  - Spring Web
  - Spring Data JPA
  - H2 Database
  - Lombok
- Entite `Player`:
  - `id` auto-genere
  - `pseudo` unique et non nul
  - `score` initialise par defaut a `0`
- Repository `PlayerRepository` (`JpaRepository<Player, Long>`).
- Service `PlayerService` avec:
  - `findAllPlayers()`
  - `findPlayerById(Long id)`
  - `createPlayer(Player player)`
  - `updatePlayer(Long id, Player player)`
  - `deletePlayer(Long id)`
- Controller REST `PlayerController` (`/api/players`):
  - `GET /api/players`
  - `GET /api/players/{id}`
  - `POST /api/players`
  - (en plus) `PUT /api/players/{id}`
  - (en plus) `DELETE /api/players/{id}`
- Chargement des donnees initiales au demarrage via `CommandLineRunner`:
  - Neo (500)
  - Trinity (750)
  - Morpheus (1000)
- Gestion d'erreur `404` si un joueur n'existe pas.

## Configuration applicative

Le fichier `src/main/resources/application.properties` configure:

- H2 en memoire: `jdbc:h2:mem:playersdb`
- Creation/suppression schema auto: `spring.jpa.hibernate.ddl-auto=create-drop`
- Console H2 activee: `/h2-console`

## Execution et tests

### Lancer les tests

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\gradlew.bat test
```

### Lancer l'application

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.10'
.\gradlew.bat bootRun
```

### Exemples d'appels API

```bash
curl http://localhost:8080/api/players
curl http://localhost:8080/api/players/1
curl -X POST http://localhost:8080/api/players \
  -H "Content-Type: application/json" \
  -d "{\"pseudo\":\"Alice\",\"score\":100}"
```

## Note JAVA_HOME

Dans cet environnement, `java -version` fonctionne mais `JAVA_HOME` etait mal configure systeme (`C:\Program Files\Java`).  
Pour utiliser Gradle, il faut pointer `JAVA_HOME` vers le JDK complet, par exemple:

`C:\Program Files\Java\jdk-21.0.10`
