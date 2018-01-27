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
    Optional<FileArrangementConfig> findByFileLocationAndFilename(String fileLocation,String filename);
    Optional<List<FileArrangementConfig>> findAllByFileLocation(String fileLocation);
    Optional<List<FileArrangementConfig>> findAllByisDirectoryAndIsLocal(boolean isDirectory,boolean isLocal);
    Optional<FileArrangementConfig> findByfilenameAndIsDirectory(String fileName,boolean isDirectory);
    void deleteByfilenameAndFileLocation(String fileName,String fileLocation);
    List<FileArrangementConfig> findAll();

}