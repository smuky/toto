# API Football Usage Guide

## Overview
The API Football integration provides elegant model objects and supports Redis caching with byte[] storage.

## Architecture

```
ApiFootballClient (HTTP calls) 
    → JsonNode (raw response)
    → ApiFootballAdapter (parsing)
    → Model Objects (League, Standing)
    → byte[] (for Redis cache)
```

## Model Objects

### 1. **League** - Represents a football league
- `LeagueInfo` - id, name, type, logo
- `Country` - name, code, flag
- `Season[]` - year, start, end, current, coverage

### 2. **Standing** - Represents league standings
- `League` - id, name, country, season
- `StandingEntry[]` - rank, team, points, goalsDiff, form, all/home/away stats

### 3. **ApiFootballResponse<T>** - Generic wrapper for all API responses
- `response` - List of actual data (League or Standing)
- `results` - Count of results
- `errors` - Any error messages

## Usage Examples

### Get Parsed Model Objects

```java
@Autowired
private ApiFootballService apiFootballService;

// Get leagues as parsed objects
List<League> leagues = apiFootballService.getIsraelLeagues();
for (League league : leagues) {
    System.out.println(league.getLeague().getName());
}

// Get standings as parsed objects
List<Standing> standings = apiFootballService.getIsraelPremierLeagueStandings(2024);
for (Standing standing : standings) {
    for (Standing.StandingEntry entry : standing.getLeague().getStandings().get(0)) {
        System.out.println(entry.getRank() + ". " + entry.getTeam().getName() + " - " + entry.getPoints() + " pts");
    }
}
```

### Cache in Redis as byte[]

```java
// Store in Redis
byte[] leaguesBytes = apiFootballService.getIsraelLeaguesAsBytes();
redisTemplate.opsForValue().set("israel:leagues", leaguesBytes);

byte[] standingsBytes = apiFootballService.getIsraelPremierLeagueStandingsAsBytes(2024);
redisTemplate.opsForValue().set("israel:standings:2024", standingsBytes);

// Retrieve from Redis and parse
byte[] cachedLeagues = redisTemplate.opsForValue().get("israel:leagues");
List<League> leagues = apiFootballService.parseLeaguesFromBytes(cachedLeagues);

byte[] cachedStandings = redisTemplate.opsForValue().get("israel:standings:2024");
List<Standing> standings = apiFootballService.parseStandingsFromBytes(cachedStandings);
```

### Direct Adapter Usage

```java
@Autowired
private ApiFootballAdapter adapter;

// Parse from JsonNode
JsonNode jsonNode = apiFootballClient.getLeagues("Israel");
List<League> leagues = adapter.parseLeagues(jsonNode);

// Convert to/from bytes
byte[] bytes = adapter.toBytes(jsonNode);
JsonNode restored = adapter.fromBytes(bytes);
```

## Configuration

Set the API key in your environment:

```bash
export API_FOOTBALL_KEY=your_api_key_here
```

Or in `application.yml`:

```yaml
app:
  api-football:
    api-key: ${API_FOOTBALL_KEY}
```

## Benefits

1. **Type Safety** - Work with strongly-typed Java objects instead of raw JSON
2. **Memory Efficient** - Store as byte[] in Redis to save memory
3. **Flexible** - Can work with JsonNode, Model objects, or byte[] as needed
4. **Clean Separation** - Client (HTTP) → Adapter (parsing) → Service (business logic)
