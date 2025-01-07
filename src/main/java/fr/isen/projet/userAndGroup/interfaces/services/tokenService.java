package fr.isen.projet.userAndGroup.interfaces.services;

import fr.isen.projet.userAndGroup.interfaces.models.token;

//begin of modifiable zone(Javadoc).......C/fc0d74c7-58bb-4ed2-b389-c3877c83f2df

//end of modifiable zone(Javadoc).........E/fc0d74c7-58bb-4ed2-b389-c3877c83f2df
public interface tokenService {
//begin of modifiable zone(Javadoc).......C/6452586c-a417-4993-bb21-d5b4de69aa79

//end of modifiable zone(Javadoc).........E/6452586c-a417-4993-bb21-d5b4de69aa79
    boolean checkToken(final String token);

//begin of modifiable zone(Javadoc).......C/c98120e8-7e6e-4b9b-954c-20811adffbdb

//end of modifiable zone(Javadoc).........E/c98120e8-7e6e-4b9b-954c-20811adffbdb
    token create();

}
