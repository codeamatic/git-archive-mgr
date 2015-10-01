package codeamatic.gam.archives.support;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import codeamatic.gam.archives.Archive;

@Repository
public interface ArchiveRepository extends CrudRepository<Archive, Long> {

}
