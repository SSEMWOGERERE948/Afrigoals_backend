package com.cosek.edms.soccer;

import com.cosek.edms.players.PlayerRepository;
import com.cosek.edms.soccer.fixtures.ApiFixturesRepository;
import com.cosek.edms.soccer.fixtures.FixtureEntity;
import com.cosek.edms.soccer.player.ApiPlayersRepository;
import com.cosek.edms.soccer.player.PlayerEntity;
import com.cosek.edms.soccer.player.PlayerResponse;
import com.cosek.edms.soccer.player.PlayerResponseWrapper;
import com.cosek.edms.soccer.teams.TeamEntity;
import com.cosek.edms.soccer.teams.ApiTeamsRepository;
import com.cosek.edms.soccer.teams.VenueEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SoccerApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RapidApiConfig rapidApiConfig;

    @Autowired
    private ApiTeamsRepository teamRepository;

    @Autowired
    private ApiPlayersRepository apiPlayersRepository;

    @Autowired
    private ApiFixturesRepository fixturesRepository;


    private final String baseUrl = "https://api-football-v1.p.rapidapi.com/v3";

    // Get all leagues and filter by East Africa
    public List<Map<String, Object>> getEastAfricanLeagues() {
        String url = baseUrl + "/leagues";

        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> allLeagues = (List<Map<String, Object>>) response.getBody().get("response");

        // East African countries
        Set<String> eastAfrica = Set.of("Uganda", "Kenya", "Tanzania", "Rwanda", "Burundi");

        return allLeagues.stream()
                .filter(league -> {
                    Map<String, Object> country = (Map<String, Object>) league.get("country");
                    return eastAfrica.contains(country.get("name"));
                })
                .collect(Collectors.toList());
    }

    // Get fixtures for a specific league
    public String getFixturesForLeague(int leagueId, int season) {
        String url = baseUrl + "/fixtures?league=" + leagueId + "&season=" + season;
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public List<TeamResponse> getTeamsByLeague(int leagueId, int season) {
        String url = baseUrl + "/teams?league=" + leagueId + "&season=" + season;

        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> rawList = (List<Map<String, Object>>) response.getBody().get("response");

        // Convert each Map to TeamResponse using ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        return rawList.stream()
                .map(obj -> mapper.convertValue(obj, TeamResponse.class))
                .collect(Collectors.toList());
    }


    public String getPlayersByTeam(int teamId, int season) {
        String url = baseUrl + "/players?team=" + teamId + "&season=" + season;
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String getCoachByTeam(int teamId) {
        String url = baseUrl + "/coachs?team=" + teamId;
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String getStandingsByLeague(int leagueId, int season) {
        String url = baseUrl + "/standings?league=" + leagueId + "&season=" + season;
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    // ✅ Get Fixtures by League and Season
    public String getFixtures(int leagueId, int season) {
        String url = baseUrl + "/fixtures?league=" + leagueId + "&season=" + season;
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    // ✅ Get Top Scorers by League and Season
    public String getTopScorers(int leagueId, int season) {
        String url = baseUrl + "/players/topscorers?league=" + leagueId + "&season=" + season;
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    // ✅ Get All Venues (optionally filter by country or league later)
    public String getVenues() {
        String url = baseUrl + "/venues";
        HttpEntity<String> entity = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public List<TeamEntity> getTeamsCached(int leagueId, int season) {
        List<TeamEntity> existing = teamRepository.findAllByLeagueIdAndSeason(leagueId, season);
        if (!existing.isEmpty()) {
            return existing; // ✅ Return cached data
        }

        // 🔄 Fetch from API
        List<TeamResponse> teamsFromApi = getTeamsByLeague(leagueId, season);

        List<TeamEntity> entities = teamsFromApi.stream().map(response -> {
            TeamResponse.Team team = response.getTeam();
            TeamResponse.Venue venue = response.getVenue();

            VenueEntity venueEntity = VenueEntity.builder()
                    .name(venue.getName())
                    .address(venue.getAddress())
                    .city(venue.getCity())
                    .capacity(venue.getCapacity())
                    .surface(venue.getSurface())
                    .image(venue.getImage())
                    .build();

            return TeamEntity.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .code(team.getCode())
                    .country(team.getCountry())
                    .founded(team.getFounded())
                    .isNational(team.isNational())
                    .logo(team.getLogo())
                    .leagueId(leagueId)
                    .season(season)
                    .venue(venueEntity)
                    .build();

        }).toList();

        return teamRepository.saveAll(entities);
    }


    public List<PlayerEntity> getPlayersByTeam(int teamId, int season, boolean forceRefresh) {
        List<PlayerEntity> existing = apiPlayersRepository.findAllByTeamIdAndSeason(teamId, season);
        if (!existing.isEmpty() && !forceRefresh) {
            return existing;
        }

        apiPlayersRepository.deleteAllByTeamIdAndSeason(teamId, season); // prevent duplicates

        String url = baseUrl + "/players?team=" + teamId + "&season=" + season;
        HttpEntity<Void> request = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<PlayerResponseWrapper> response = restTemplate.exchange(
                url, HttpMethod.GET, request, PlayerResponseWrapper.class
        );

        List<PlayerEntity> players = response.getBody().getResponse().stream()
                .map(playerResponse -> {
                    PlayerResponse.Player p = playerResponse.getPlayer();  // ✅ Corrected type here
                    return PlayerEntity.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .firstname(p.getFirstname())
                            .lastname(p.getLastname())
                            .nationality(p.getNationality())
                            .position(p.getPosition())
                            .age(p.getAge())
                            .teamId(teamId)
                            .season(season)
                            .build();
                })
                .toList();


        return apiPlayersRepository.saveAll(players);
    }

    public List<FixtureEntity> getFixturesCached(int leagueId, int season, boolean forceRefresh) {
        List<FixtureEntity> existing = fixturesRepository.findAllByLeagueIdAndSeason(leagueId, season);
        if (!existing.isEmpty() && !forceRefresh) {
            return existing;
        }

        fixturesRepository.deleteAllByLeagueIdAndSeason(leagueId, season);

        String url = baseUrl + "/fixtures?league=" + leagueId + "&season=" + season;
        HttpEntity<String> request = new HttpEntity<>(rapidApiConfig.createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        List<Map<String, Object>> rawFixtures = (List<Map<String, Object>>) response.getBody().get("response");

        List<FixtureEntity> fixtures = rawFixtures.stream().map(fx -> {
            Map<String, Object> fixture = (Map<String, Object>) fx.get("fixture");
            Map<String, Object> teams = (Map<String, Object>) fx.get("teams");
            Map<String, Object> league = (Map<String, Object>) fx.get("league");
            Map<String, Object> venue = (Map<String, Object>) fixture.get("venue");

            Map<String, Object> home = (Map<String, Object>) teams.get("home");
            Map<String, Object> away = (Map<String, Object>) teams.get("away");
            Map<String, Object> goals = (Map<String, Object>) fx.get("goals");

            return FixtureEntity.builder()
                    .fixtureId(((Number) fixture.get("id")).longValue())
                    .date((String) fixture.get("date"))
                    .status((String) ((Map<String, Object>) fixture.get("status")).get("short"))
                    .leagueId(((Number) league.get("id")).intValue())
                    .season(((Number) league.get("season")).intValue())

                    .homeTeamId(((Number) home.get("id")).intValue())
                    .homeTeamName((String) home.get("name"))
                    .awayTeamId(((Number) away.get("id")).intValue())
                    .awayTeamName((String) away.get("name"))

                    .homeGoals(goals.get("home") != null ? ((Number) goals.get("home")).intValue() : null)
                    .awayGoals(goals.get("away") != null ? ((Number) goals.get("away")).intValue() : null)

                    .venueName(venue != null ? (String) venue.get("name") : null)
                    .referee((String) fixture.get("referee"))
                    .build();
        }).toList();

        return fixturesRepository.saveAll(fixtures);
    }

}

