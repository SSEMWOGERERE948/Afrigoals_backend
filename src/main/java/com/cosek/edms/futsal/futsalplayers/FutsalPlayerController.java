    package com.cosek.edms.futsal.futsalplayers;

    import com.cosek.edms.futsal.dto.FutsalPlayerResponse;
    import com.cosek.edms.futsal.entity.FutsalPlayer;
    import com.cosek.edms.futsal.entity.FutsalPlayerRequest;
    import com.cosek.edms.futsal.entity.FutsalTeam;
    import com.cosek.edms.futsal.futsalteams.FutsalTeamRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/futsalplayers")
    @CrossOrigin
    public class FutsalPlayerController {
        @Autowired
        private FutsalPlayerService playerService;
        @Autowired
        private FutsalTeamRepository futsalTeamRepository;
        @Autowired
        private FutsalPlayerRepository futsalPlayerRepository;

        @GetMapping("/")
        public List<FutsalPlayerResponse> getAll() {
            List<FutsalPlayer> players = playerService.getAll();
            return players.stream().map(player -> {
                FutsalPlayerResponse dto = new FutsalPlayerResponse();
                dto.setId(player.getId());
                dto.setName(player.getName());
                dto.setNumber(player.getNumber());
                dto.setPositionId(player.getPositionId());
                dto.setNationality(player.getNationality());
                dto.setAge(player.getAge());
                dto.setHeight(player.getHeight());
                dto.setWeight(player.getWeight());
                dto.setPreferredFoot(player.getPreferredFoot());
                dto.setStudentId(player.getStudentId());
                dto.setCourse(player.getCourse());
                dto.setYearOfStudy(player.getYearOfStudy());
                dto.setImage(player.getImage());
                dto.setStats(player.getStats());
                dto.setTeamName(player.getTeam() != null ? player.getTeam().getTeamName() : "Unknown Team");
                return dto;
            }).toList();
        }


        @GetMapping("/{id}")
        public FutsalPlayer getById(@PathVariable Long id) {
            return playerService.getById(id);
        }

        @PostMapping("/create")
        public FutsalPlayer create(@RequestBody FutsalPlayerRequest request) {
            FutsalPlayer player = request.getPlayer();
            FutsalTeam team = futsalTeamRepository.findById(request.getTeamId()).orElseThrow();
            player.setTeam(team);
            player.setPositionId(request.getPositionId());
            return futsalPlayerRepository.save(player);
        }


        @PutMapping("/{id}")
        public FutsalPlayer update(@PathVariable Long id, @RequestBody FutsalPlayer player) {
            return playerService.update(id, player);
        }

        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            playerService.delete(id);
        }
    }

