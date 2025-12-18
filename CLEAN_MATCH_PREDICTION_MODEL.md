# Clean Match Prediction Domain Model

## Overview

This document describes the **bias-free, objective domain model** created for football match prediction analysis based on API-Football data. The model excludes all predictive/comparative data and contains only factual statistics suitable for unbiased AI analysis.

## Design Philosophy

### ✅ INCLUDED: Objective Factual Data
- **League Context**: id, name, country, season
- **Team Performance Metrics**:
  - Form string (W/D/L pattern, chronological)
  - Fixture records (played/wins/draws/losses with home/away splits)
  - Goal statistics (scored/conceded with totals and averages)
  - Clean sheets and failed to score counts
  - Biggest winning/drawing/losing streaks
- **Recent Form** (Last 5 matches):
  - Matches played
  - Goals scored/conceded with averages
- **Head-to-Head History**:
  - Match date, season, round
  - Venue information
  - Team names and goals scored

### ❌ EXCLUDED: Predictive/Comparative Data
- `predictions` section (winner, advice, win_or_draw, under_over, percent)
- `comparison` section (form %, att %, def %, h2h %, poisson_distribution %, total %)
- `last_5.form` percentage (e.g., "53%", "27%")
- `last_5.att` and `last_5.def` percentages
- Any winner/advice fields from API-Football

## Domain Model Structure

```
MatchAnalysisData (root)
├── LeagueInfo
│   ├── id, name, country, season
├── TeamAnalysis (homeTeam)
│   ├── id, name
│   ├── SeasonStats
│   │   ├── formString (e.g., "LLLLLLDDDDWLDW")
│   │   ├── FixtureRecord
│   │   │   ├── played (VenueRecord: home/away/total)
│   │   │   ├── wins (VenueRecord)
│   │   │   ├── draws (VenueRecord)
│   │   │   └── losses (VenueRecord)
│   │   ├── GoalRecord
│   │   │   ├── scored (GoalDetails: total + average)
│   │   │   └── conceded (GoalDetails: total + average)
│   │   ├── cleanSheets (VenueRecord)
│   │   ├── failedToScore (VenueRecord)
│   │   └── biggestStreak (StreakRecord: wins/draws/losses)
│   └── RecentFormStats (last 5 matches)
│       ├── matchesPlayed
│       └── goals (SimpleGoalStats: scored/conceded with averages)
├── TeamAnalysis (awayTeam)
│   └── [same structure as homeTeam]
└── List<HistoricalMatch> (headToHead)
    ├── date, season, round, venue
    ├── homeTeam (TeamResult: id, name, goals)
    └── awayTeam (TeamResult: id, name, goals)
```

## Key Classes

### Core Domain Classes
| Class | Purpose |
|-------|---------|
| `MatchAnalysisData` | Root object containing all match data |
| `LeagueInfo` | League metadata (id, name, country, season) |
| `TeamAnalysis` | Complete team performance data |
| `SeasonStats` | Full season statistics for a team |
| `RecentFormStats` | Last 5 matches statistics |
| `HistoricalMatch` | Single H2H match record |

### Supporting Classes
| Class | Purpose |
|-------|---------|
| `FixtureRecord` | Match results (played/wins/draws/losses) |
| `VenueRecord` | Home/away/total split for any metric |
| `GoalRecord` | Goals scored and conceded |
| `GoalDetails` | Goal totals and averages with venue splits |
| `VenueAverage` | Average values split by home/away/total |
| `StreakRecord` | Consecutive wins/draws/losses |
| `SimpleGoalStats` | Basic goal statistics for recent form |
| `TeamResult` | Team identifier with goals in a match |

### Infrastructure
| Class | Purpose |
|-------|---------|
| `MatchAnalysisMapper` | Converts API-Football `Prediction` to `MatchAnalysisData` |

## Usage Example

### Step 1: Get API-Football Prediction Data
```java
@Autowired
private ApiFootballClient apiFootballClient;

Prediction apiPrediction = apiFootballClient.getPrediction(fixtureId);
```

### Step 2: Convert to Clean Domain Model
```java
@Autowired
private MatchAnalysisMapper mapper;

MatchAnalysisData cleanData = mapper.toMatchAnalysisData(apiPrediction);
```

### Step 3: Get Unbiased AI Prediction
```java
@Autowired
private OpenAiService openAiService;

TodoPredictionPromptResponse aiPrediction = 
    openAiService.getCleanMatchPrediction(cleanData, "English");
```

## Complete Integration Example

```java
@Service
@RequiredArgsConstructor
public class FootballPredictionService {
    
    private final ApiFootballClient apiFootballClient;
    private final MatchAnalysisMapper mapper;
    private final OpenAiService openAiService;
    
    /**
     * Generates an unbiased AI prediction based on objective match data only.
     * Excludes all predictive/comparative data from API-Football.
     */
    public TodoPredictionPromptResponse predictMatchOutcome(Long fixtureId, String language) {
        // 1. Fetch raw prediction data from API-Football
        Prediction apiPrediction = apiFootballClient.getPrediction(fixtureId);
        
        // 2. Transform to clean, objective domain model
        // This step filters out predictions, comparisons, and percentages
        MatchAnalysisData cleanData = mapper.toMatchAnalysisData(apiPrediction);
        
        // 3. Let AI make its own unbiased prediction based on facts only
        return openAiService.getCleanMatchPrediction(cleanData, language);
    }
}
```

## Data Mapping Details

### From API-Football JSON to Domain Model

**League Mapping:**
```
API: league.{id, name, country, season}
→ LeagueInfo.{id, name, country, season}
```

**Team Season Stats Mapping:**
```
API: teams.home.league.form
→ TeamAnalysis.seasonStats.formString

API: teams.home.league.fixtures.{played, wins, draws, loses}
→ TeamAnalysis.seasonStats.fixtures.{played, wins, draws, losses}

API: teams.home.league.goals.for/against
→ TeamAnalysis.seasonStats.goals.scored/conceded

API: teams.home.league.clean_sheet
→ TeamAnalysis.seasonStats.cleanSheets

API: teams.home.league.failed_to_score
→ TeamAnalysis.seasonStats.failedToScore

API: teams.home.league.biggest.streak
→ TeamAnalysis.seasonStats.biggestStreak
```

**Recent Form Mapping:**
```
API: teams.home.last_5.played
→ TeamAnalysis.recentForm.matchesPlayed

API: teams.home.last_5.goals.for/against
→ TeamAnalysis.recentForm.goals.{scored, conceded}

EXCLUDED: last_5.form (percentage like "53%")
EXCLUDED: last_5.att, last_5.def (percentages)
```

**Head-to-Head Mapping:**
```
API: h2h[].{fixture.date, league.season, league.round, fixture.venue.name}
→ HistoricalMatch.{date, season, round, venue}

API: h2h[].teams.{home, away} + goals.{home, away}
→ HistoricalMatch.{homeTeam, awayTeam}
```

## Prompt Template

The AI prompt (`CleanMatchPredictionPrompt.st`) instructs the model to:

1. **Analyze Form**: Compare W/D/L patterns, recent vs season-long form, home/away splits
2. **Evaluate Goals**: Assess scoring/defensive records, averages, clean sheets
3. **Review H2H**: Examine historical matchups, venue context, goal patterns
4. **Consider Streaks**: Identify momentum from winning/losing/drawing streaks
5. **Generate Probabilities**: Predict 1/X/2 percentages (must sum to 100%)
6. **Justify**: Provide data-driven explanation under 150 words

## Benefits

| Benefit | Description |
|---------|-------------|
| **Bias-Free** | No predictive data from API-Football influences the AI |
| **Objective** | Only factual statistics, no opinions or advice |
| **Clean Architecture** | Clear separation between API schema and domain model |
| **AI-Optimized** | Structured data designed for LLM consumption |
| **Maintainable** | Single responsibility classes with clear purpose |
| **Testable** | Pure domain objects with no external dependencies |
| **Transparent** | Easy to understand what data influences predictions |

## File Locations

### Domain Model
```
com.muky.toto.domain.prediction/
├── MatchAnalysisData.java       (root)
├── LeagueInfo.java
├── TeamAnalysis.java
├── SeasonStats.java
├── FixtureRecord.java
├── VenueRecord.java
├── GoalRecord.java
├── GoalDetails.java
├── VenueAverage.java
├── StreakRecord.java
├── RecentFormStats.java
├── SimpleGoalStats.java
├── HistoricalMatch.java
└── TeamResult.java
```

### Mapper
```
com.muky.toto.mapper/
└── MatchAnalysisMapper.java
```

### Service
```
com.muky.toto.service/
├── OpenAiService.java                    (interface with getCleanMatchPrediction)
└── SpringAiPerplexityService.java        (implementation)
```

### Template
```
src/main/resources/templates/
└── CleanMatchPredictionPrompt.st
```

## Example Data Flow

### Input (API-Football JSON)
```json
{
  "predictions": { ... },           // ❌ EXCLUDED
  "league": { ... },                // ✅ INCLUDED
  "teams": {
    "home": {
      "league": {
        "form": "LLLLLLDDDDWLDW",   // ✅ INCLUDED
        "fixtures": { ... },         // ✅ INCLUDED
        "goals": { ... }             // ✅ INCLUDED
      },
      "last_5": {
        "form": "53%",               // ❌ EXCLUDED (percentage)
        "att": "40%",                // ❌ EXCLUDED (percentage)
        "def": "73%",                // ❌ EXCLUDED (percentage)
        "goals": { ... }             // ✅ INCLUDED
      }
    }
  },
  "comparison": { ... },             // ❌ EXCLUDED
  "h2h": [ ... ]                     // ✅ INCLUDED
}
```

### Output (Clean Domain Model)
```java
MatchAnalysisData {
  league: LeagueInfo { id, name, country, season },
  homeTeam: TeamAnalysis {
    seasonStats: SeasonStats {
      formString: "LLLLLLDDDDWLDW",
      fixtures: { played, wins, draws, losses },
      goals: { scored, conceded }
    },
    recentForm: RecentFormStats {
      matchesPlayed: 5,
      goals: { scored, conceded }
    }
  },
  awayTeam: TeamAnalysis { ... },
  headToHead: [ HistoricalMatch { ... } ]
}
```

## Testing the Implementation

```java
@Test
public void testCleanMatchPrediction() {
    // Given: API-Football prediction data
    Prediction apiPrediction = loadTestPrediction();
    
    // When: Convert to clean domain model
    MatchAnalysisData cleanData = mapper.toMatchAnalysisData(apiPrediction);
    
    // Then: Verify no predictive data is present
    assertNull(cleanData.getPredictions());  // No predictions field
    assertNull(cleanData.getComparison());   // No comparison field
    assertNotNull(cleanData.getLeague());
    assertNotNull(cleanData.getHomeTeam().getSeasonStats().getFormString());
    
    // When: Get AI prediction
    TodoPredictionPromptResponse prediction = 
        openAiService.getCleanMatchPrediction(cleanData, "English");
    
    // Then: Verify AI generated its own prediction
    assertNotNull(prediction);
    assertEquals(100, prediction.home() + prediction.draw() + prediction.away());
}
```

## Summary

This clean domain model ensures that AI predictions are based **solely on objective, factual data** without any bias from API-Football's own predictions or comparative percentages. The result is a more transparent, maintainable, and trustworthy prediction system.
