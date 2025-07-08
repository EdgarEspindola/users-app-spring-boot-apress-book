package com.example.users.repository;

import java.util.List;

import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import com.example.users.model.UserDataJdbc;
import com.example.users.model.UserGravatar;
import com.example.users.model.UserRole;

@Component
public class UserBeforeSaveCallback implements BeforeSaveCallback<UserDataJdbc> {

    @Override
    public UserDataJdbc onBeforeSave(UserDataJdbc aggregate, MutableAggregateChange<UserDataJdbc> aggregateChange) {
        if (aggregate.getGravatarUrl()==null)
            aggregate.setGravatarUrl(UserGravatar.getGravatarUrlFromEmail(aggregate.getEmail()));
        if (aggregate.getUserRole() == null)
            aggregate.setUserRole(List.of(UserRole.INFO));
        return aggregate;
    }

}
