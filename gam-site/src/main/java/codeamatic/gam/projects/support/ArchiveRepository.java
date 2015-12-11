package codeamatic.gam.projects.support;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import codeamatic.gam.projects.Archive;

@Repository
public interface ArchiveRepository extends CrudRepository<Archive, Long> {

}
