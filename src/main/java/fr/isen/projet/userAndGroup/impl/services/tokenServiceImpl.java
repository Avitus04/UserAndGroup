package fr.isen.projet.userAndGroup.impl.services;

import fr.isen.projet.userAndGroup.interfaces.models.token;
import fr.isen.projet.userAndGroup.interfaces.services.tokenService;

public class tokenServiceImpl implements tokenService {
    @Override
    public boolean checkToken(String token) {
        return false;
    }

    @Override
    public token create() {
        return null;
    }
}
