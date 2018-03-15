package de.htwsaar.server.persistence;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Timo on 30.11.2017.
 */
@Transactional
public interface  ServerDAO extends CrudRepository<ServerInfo, Integer> {
    ServerInfo save(ServerInfo serverInfo);
    Optional<ServerInfo> findByServerGroupAndServerRang(int serverGroup,int serverRang);

}