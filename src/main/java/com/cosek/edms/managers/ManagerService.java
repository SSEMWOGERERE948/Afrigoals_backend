package com.cosek.edms.managers;

import com.cosek.edms.teams.Team;
import com.cosek.edms.teams.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TeamRepository teamRepository;

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManager(Long id) {
        return managerRepository.findById(id);
    }

    public Manager createManager(Manager manager, Long clubTeamId, Long nationalTeamId) {
        if (clubTeamId != null) {
            teamRepository.findById(clubTeamId).ifPresent(manager::setClubTeam);
        }
        if (nationalTeamId != null) {
            teamRepository.findById(nationalTeamId).ifPresent(manager::setNationalTeam);
        }
        return managerRepository.save(manager);
    }

    public Optional<Manager> updateManager(Long id, Manager updatedManager, Long clubTeamId, Long nationalTeamId) {
        return managerRepository.findById(id).map(existing -> {
            updatedManager.setId(existing.getId());
            if (clubTeamId != null) {
                teamRepository.findById(clubTeamId).ifPresent(updatedManager::setClubTeam);
            }
            if (nationalTeamId != null) {
                teamRepository.findById(nationalTeamId).ifPresent(updatedManager::setNationalTeam);
            }
            return managerRepository.save(updatedManager);
        });
    }

    public boolean deleteManager(Long id) {
        if (managerRepository.existsById(id)) {
            managerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Manager> getManagersByTeamId(Long teamId) {
        List<Manager> fromClubs = managerRepository.findByClubTeamId(teamId);
        List<Manager> fromNational = managerRepository.findByNationalTeamId(teamId);
        fromClubs.addAll(fromNational);
        return fromClubs;
    }
}
