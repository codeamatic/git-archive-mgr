package codeamatic.gam.projects.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codeamatic.gam.projects.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findById(Integer id);

    Project findByName(String name);
}

