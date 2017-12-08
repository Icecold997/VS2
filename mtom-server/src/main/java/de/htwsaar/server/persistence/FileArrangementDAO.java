package de.htwsaar.server.persistence;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by Timo on 30.11.2017.
 */
@Transactional
public interface FileArrangementDAO extends CrudRepository<FileArrangementConfig, Integer> {
    FileArrangementConfig save(FileArrangementConfig fileArrangementConfig);
    Optional<FileArrangementConfig> findByfilename(String fileName);
    Optional<List<FileArrangementConfig>> findAllByisDirectory(boolean isDirectory);
    void deleteByfilename(String fileName);

}