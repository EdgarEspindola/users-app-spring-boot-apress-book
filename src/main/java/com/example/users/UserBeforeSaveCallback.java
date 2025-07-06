package com.example.users;

import java.util.List;

import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

@Component
public class UserBeforeSaveCallback implements BeforeSaveCallback<User> {

    @Override
    public User onBeforeSave(User aggregate, MutableAggregateChange<User> aggregateChange) {
        if (aggregate.getGravatarUrl()==null)
            aggregate.setGravatarUrl(UserGravatar.getGravatarUrlFromEmail(aggregate.getEmail()));
        if (aggregate.getUserRole() == null)
            aggregate.setUserRole(List.of(UserRole.INFO));
        return aggregate;
    }

}
