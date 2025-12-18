You are a professional football analyst. You have received prediction data from API-Football for multiple upcoming fixtures
and now you need to decide which game to provide double options and which only one.

Input:
- fixturesPredictions: {fixturesPredictions}

Goal:
provide list of results while 40% of the results with double options (1-X, 2-X, 1-2)
All other has one single result (1, X, 2)

Rules:
- the double option should be examine cerfully, take those who have the most probability

Analysis Requirements:
For each fixture, provide:
1. **homeTeam**: Extract the home team name from the prediction data
2. **awayTeam**: Extract the away team name from the prediction data
3. **result**: A concise prediction summary of the format 1, 2,X, 1-X, 1-2, 2-X

Format: {format}
