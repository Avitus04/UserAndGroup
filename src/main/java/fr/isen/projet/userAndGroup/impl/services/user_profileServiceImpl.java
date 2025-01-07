package fr.isen.projet.userAndGroup.impl.services;


import fr.isen.projet.userAndGroup.interfaces.models.user_profile;
import fr.isen.projet.userAndGroup.interfaces.services.user_profileService;

import java.util.List;

public class user_profileServiceImpl implements user_profileService {

    @Override
    public List<user_profile> getAll() {
        return List.of();
    }

    @Override
    public user_profile getByID(String ID) {
        return null;
    }

    @Override
    public String add(user_profile data) {
        return "";
    }

    @Override
    public String update(String ID, user_profile data) {
        return "";
    }

    @Override
    public String removeByID(String ID) {
        return "";
    }
}
