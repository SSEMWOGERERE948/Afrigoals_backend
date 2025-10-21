    package com.cosek.edms.futsal.futsalteams;

    import com.cosek.edms.futsal.entity.FutsalTeam;
    import org.springframework.beans.BeanUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class FutsalTeamService {
        @Autowired
        private FutsalTeamRepository teamRepo;

        public List<FutsalTeam> getAll() { return teamRepo.findAll(); }

        public FutsalTeam getById(Long id) { return teamRepo.findById(id).orElseThrow(); }

        public FutsalTeam create(FutsalTeam team) { return teamRepo.save(team); }

        public FutsalTeam update(Long id, FutsalTeam updated) {
            FutsalTeam team = getById(id);
            BeanUtils.copyProperties(updated, team, "id");
            return teamRepo.save(team);
        }

        public void delete(Long id) { teamRepo.deleteById(id); }
    }

