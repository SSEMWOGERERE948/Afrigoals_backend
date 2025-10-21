package com.cosek.edms.managers;

import com.cosek.edms.teams.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TeamRepository teamRepository;


    @GetMapping
    public List<Manager> getAllManagers() {
        return managerService.getAllManagers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getManager(@PathVariable Long id) {
        return managerService.getManager(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public Manager createManager(@RequestBody ManagerRequest request) {
        Manager manager = new Manager();
        manager.setName(request.getManager().getName());
        manager.setNationality(request.getManager().getNationality());
        manager.setAge(request.getManager().getAge());
        manager.setImage(request.getManager().getImage());
        manager.setExperience(request.getManager().getExperience());
        manager.setAchievements(request.getManager().getAchievements());
        manager.setTacticalStyle(request.getManager().getTacticalStyle());
        manager.setContractStart(request.getManager().getContractStart());
        manager.setContractEnd(request.getManager().getContractEnd());
        manager.setRole(request.getManager().getRole());
        manager.setSpecialization(request.getManager().getSpecialization());

        if (request.getClubTeamId() != null) {
            teamRepository.findById(request.getClubTeamId()).ifPresentOrElse(
                    team -> {
                        manager.setClubTeam(team);
                        System.out.println("✅ Club team assigned: " + team.getName());
                    },
                    () -> System.out.println("❌ Club team NOT found with ID: " + request.getClubTeamId())
            );
        }

        if (request.getNationalTeamId() != null) {
            teamRepository.findById(request.getNationalTeamId()).ifPresentOrElse(
                    team -> {
                        manager.setNationalTeam(team);
                        System.out.println("✅ National team assigned: " + team.getName());
                    },
                    () -> System.out.println("❌ National team NOT found with ID: " + request.getNationalTeamId())
            );
        }

        return managerRepository.save(manager);
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updateManager(@PathVariable Long id, @RequestBody ManagerRequest request) {
        return managerService.updateManager(id, request.getManager(), request.getClubTeamId(), request.getNationalTeamId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Long id) {
        return managerService.deleteManager(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/team/{teamId}")
    public List<Manager> getByTeamId(@PathVariable Long teamId) {
        return managerService.getManagersByTeamId(teamId);
    }

    @GetMapping("/head-managers")
    public List<Manager> getHeadManagers() {
        return managerRepository.findByRole(ManagerRole.HEAD_MANAGER);
    }

    @GetMapping("/assistant-managers")
    public List<Manager> getAssistantManagers() {
        return managerRepository.findByRole(ManagerRole.ASSISTANT_MANAGER);
    }

}
