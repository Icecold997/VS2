package de.htwsaar.server.persistence;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by Timo on 30.11.2017.
 */
@Transactional
public interface ForwardingDAO extends CrudRepository<ForwardingConfig, Integer> {
    ForwardingConfig save(ForwardingConfig forwardingConfig);

}