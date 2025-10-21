package com.cosek.edms.managers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    List<Manager> findByClubTeamId(Long teamId);
    List<Manager> findByNationalTeamId(Long teamId);
    List<Manager> findByRole(ManagerRole role);

}
