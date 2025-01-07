package fr.isen.projet.userAndGroup.impl.services;

import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.models.token;
import fr.isen.projet.userAndGroup.interfaces.services.membershipService;

import java.util.List;

public class membershipServiceImpl implements membershipService {

    @Override
    public List<membership> getAll() {
        return List.of();
    }

    @Override
    public membership getByID(String ID) {
        return null;
    }

    @Override
    public String add(membership data) {
        return "";
    }

    @Override
    public String update(String ID, membership data) {
        return "";
    }

    @Override
    public String removeByID(String ID) {
        return "";
    }

    @Override
    public token connection(String login, String password) {
        return null;
    }
}
