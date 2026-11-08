# Dota 2 Draft Analyzer Lite

Dota 2 Draft Analyzer Lite is a student project for the Design Patterns course. It compares two five-hero drafts and explains their strengths during laning, midgame and late game.

The analysis uses manually assigned hero attributes, lane synergies and matchup rules. The result is a rule-based comparison under similar player execution, not a prediction of the actual win rate.

## Team

- Kassen Alikhan
- Tleubay Abilmansur

Group: SE-2540.

## Features

- A fixed pool of 30 heroes, with six heroes for each position.
- Separate Radiant and Dire drafts.
- Hero selection and replacement using role-specific lane slots.
- Prevention of duplicate picks across both teams.
- Server-side validation of team size, positions and lanes.
- Individual Top, Mid and Bottom lane scores with explanations.
- Midgame analysis influenced by laning results.
- Late-game analysis influenced by scaling and midgame results.
- A demo draft for quickly checking the application.

The MVP uses the following lane arrangement:

| Team | Top | Mid | Bottom |
|---|---|---|---|
| Radiant | Position 3 + Position 4 | Position 2 | Position 1 + Position 5 |
| Dire | Position 1 + Position 5 | Position 2 | Position 3 + Position 4 |

Each team must have exactly one hero for every position from 1 to 5. A hero cannot appear more than once in the match.

## Technology Stack

**Backend**

- Java 17 or newer
- Spring Boot 3.5.7
- Maven
- Jackson for reading JSON data
- JUnit 5 and Spring Boot Test

**Frontend**

- HTML
- CSS
- Vanilla JavaScript
- Live Server for local development

The project does not require a database. Hero attributes and lane rules are stored in local JSON files.

## Requirements

Before running the project, install:

- JDK 17 or newer. We used JDK 21 during development.
- VS Code with the Live Server extension.

A separate Maven installation is not required because the repository includes Maven Wrapper.

Make sure the terminal uses a full JDK rather than a JRE. You can check the Java installation used by Maven with:

```powershell
.\mvnw.cmd -version
```

## Running the Application

The current frontend is configured to run through Live Server, while Spring Boot provides the API. Both processes must remain running while using the application.

### 1. Start the Backend

Open a terminal in the project root.

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
bash mvnw spring-boot:run
```

Wait for the following message:

```text
Started DotaApplication
```

The backend runs on port `8080`. The frontend sends API requests to:

```text
http://127.0.0.1:8080
```

Leave the terminal open. To stop the backend, press `Ctrl+C`.

### 2. Start the Frontend

1. Open the entire repository folder in VS Code.
2. Find this file:

```text
src/main/resources/static/html/analysis.html
```

3. Right-click it and select **Open with Live Server**.

Open the repository root as the workspace, since the current HTML uses paths starting with `/src/main/resources/static/`.

Opening the HTML directly as a local file is not the intended setup.

## Using the Application

1. Select a slot in either the Radiant or Dire team.
2. Choose an available hero for that position.
3. Fill all ten slots.
4. Click **Analyze**.

The result shows the three lane scores, the applied lane rules, overall laning advantage, midgame and late-game scores, and a short summary.

Use **Fill demo draft** to load a complete example without selecting every hero manually. Use **Reset** to clear the draft.

After replacing a hero, run the analysis again to update the result.

Positive scores favour Radiant. Negative scores favour Dire. Scores are internal comparison points, not percentages.

## Analysis Logic

Both teams are evaluated using the same formulas.

### Laning

The base strength of a lane is the average `lanePower` of its heroes. Pair synergies and directed matchup adjustments are then applied:

```text
laneDiff =
    averageLanePower(Radiant)
    - averageLanePower(Dire)
    + synergy(Radiant)
    - synergy(Dire)
    + counterAdjustment
```

A mid lane contains one hero per side and does not receive a pair synergy bonus.

The overall laning result is:

```text
overallLaningDiff = (topDiff + midDiff + bottomDiff) / 3
```

### Midgame

Midgame compares tempo, greed, teamfight and push, while carrying forward part of the laning result:

```text
midDiff =
    diffAverage(tempo)
    - 0.5 * diffAverage(greed)
    + 0.4 * diffAverage(teamfight)
    + 0.2 * diffAverage(push)
    + 0.35 * overallLaningDiff
```

Here, `diffAverage(attribute)` means the Radiant average minus the Dire average.

### Late Game

Late-game analysis combines scaling, teamfight and the previous phase:

```text
lateDiff =
    diffAverage(scaling)
    + 0.25 * diffAverage(teamfight)
    + 0.20 * midDiff
```

Phase results use the following labels:

| Absolute score difference | Label |
|---|---|
| Below 0.5 | Even |
| From 0.5 to below 1.5 | Slight Advantage |
| 1.5 or higher | Strong Advantage |

When `abs(midDiff) >= 1.5`, the application shows a strong tempo warning. This does not mean an automatic victory.

All coefficients are expert assumptions used for this educational model.

## Design Patterns

The project integrates six design patterns.

| Pattern | Main classes | Purpose |
|---|---|---|
| Factory Method | `HeroFactory`, `CarryFactory`, `MidFactory`, `OfflaneFactory`, `SoftSupportFactory`, `HardSupportFactory` | Creates heroes through a factory assigned to a particular position |
| Builder | `DraftBuilder` | Builds a complete team and checks its positions and unique hero IDs |
| Observer | `DraftSubject`, `DraftObserver`, `DraftLogger` | Notifies subscribed listeners when a hero is added |
| Strategy | `PhaseAnalysisStrategy`, `MidgameStrategy`, `LategameStrategy` | Provides different phase calculations through a shared interface |
| Decorator | `ScoreComponent`, `ScoreDecorator`, score modifiers | Adds teamfight, push and previous-phase contributions to a base score |
| Facade | `AnalysisFacade` | Runs laning, midgame and late-game analysis through one public operation |

The patterns participate in the actual request flow. The mapper selects factories and builds the teams. Accepted hero additions trigger Observer notifications. The facade then runs laning and the phase strategies, whose scores are assembled using decorators.

## Data Files

The data files are located in:

```text
src/main/resources/data/
```

| File | Contents |
|---|---|
| `heroes.json` | Hero IDs, names, positions and attributes |
| `synergy.json` | Bonuses for allied hero pairs |
| `matchups.json` | Directed hero matchup adjustments |

Each hero has six numerical attributes:

- `lanePower`
- `tempo`
- `greed`
- `scaling`
- `teamfight`
- `push`

Attributes use a scale from 0 to 10.

## API

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/heroes` | Returns the hero catalog |
| POST | `/api/draft/validate` | Checks a draft and returns validation errors |
| POST | `/api/analyze` | Returns the analysis of all three phases |

Both POST endpoints accept an object containing `radiant` and `dire` arrays. Each pick contains:

```json
{
  "heroId": "juggernaut",
  "position": 1,
  "lane": "BOTTOM"
}
```

A complete request contains five picks in each team.

The validation endpoint returns a result such as:

```json
{
  "valid": false,
  "errors": [
    "Radiant must contain exactly 5 heroes"
  ]
}
```

The analysis response contains `laning`, `midgame`, `lategame`, `thresholdX` and `summary`. Individual lane results include a numerical `diff` and a list of `reasons`.

## Project Structure

```text
src/main/java/com/dota/
├── analysis/       Phase strategies, decorators and facade
├── controllers/    API controllers and request mapping
├── draft/          Factories, builder and observer
├── laning/         Lane calculations and rule catalog
└── model/          Hero, draft and result models

src/main/resources/
├── data/           Hero attributes and lane rules
└── static/
    ├── css/        Frontend styles
    ├── html/       Application page
    └── js/         Draft editor and result rendering

src/test/java/      Unit and API integration tests
```

## Tests

Run the test suite from the project root.

On Windows:

```powershell
.\mvnw.cmd clean test
```

On Linux or macOS:

```bash
bash mvnw clean test
```

The tests cover hero data, factories, draft building, invalid picks, Observer notifications, lane calculations, phase formulas and API responses.

Frontend interactions should also be checked in the browser: selecting heroes, replacing picks, resetting the draft and displaying updated results.

## Limitations and Further Work

The application uses a limited hero pool and manually defined rules. It does not account for items, player skill, the current game patch, actual gold and experience, or in-match execution.

Hero portraits are loaded from external sources. When an image is unavailable, the interface displays the hero's initials.

Possible improvements include expanding the hero pool, adding more matchup rules, storing portraits locally and evaluating the scoring coefficients against real match data.