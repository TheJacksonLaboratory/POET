package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> { }
