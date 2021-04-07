package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.repository.EventRepository;

import java.util.List;

public class EventRepositoryImpl extends BasicRepositoryImpl<Event> implements EventRepository {

    public EventRepositoryImpl() {
        super(Event.class);
    }

    @Override
    public Event find(long id) {
        String query = "SELECT * FROM User WHERE id=?";
        return findSingle(query, List.of(id));
    }

    @Override
    public void save(Event event) {
        String query = "INSERT INTO `Event` (`userId`, `type`, `creationTime`) VALUES (?, ?, NOW())";
        save(query, event, List.of(event.getUserId(), event.getType().getValue()));
    }
}
