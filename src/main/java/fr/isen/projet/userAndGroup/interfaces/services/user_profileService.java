package fr.isen.projet.userAndGroup.interfaces.services;

import java.util.List;
import fr.isen.projet.userAndGroup.interfaces.models.user_profile;

//begin of modifiable zone(Javadoc).......C/ba2334f8-40cc-4335-8df2-818b7e69a588

//end of modifiable zone(Javadoc).........E/ba2334f8-40cc-4335-8df2-818b7e69a588
public interface user_profileService {
//begin of modifiable zone(Javadoc).......C/d5be7fab-de4b-494f-a450-09eec9e73ef1

//end of modifiable zone(Javadoc).........E/d5be7fab-de4b-494f-a450-09eec9e73ef1
    List<user_profile> getAll();

//begin of modifiable zone(Javadoc).......C/5e3add24-e8e1-4704-a4a0-60732cfaa965

//end of modifiable zone(Javadoc).........E/5e3add24-e8e1-4704-a4a0-60732cfaa965
    user_profile getByID(final String ID);

//begin of modifiable zone(Javadoc).......C/ad7568c6-75bb-402e-b0b4-97f79233205e

//end of modifiable zone(Javadoc).........E/ad7568c6-75bb-402e-b0b4-97f79233205e
    String add(final user_profile data);

//begin of modifiable zone(Javadoc).......C/a3ac813d-63dd-4965-9891-7bca6dbb561e

//end of modifiable zone(Javadoc).........E/a3ac813d-63dd-4965-9891-7bca6dbb561e
    String update(final String ID, final user_profile data);

//begin of modifiable zone(Javadoc).......C/73c4ec94-54b9-4ee7-9c69-db87351731a2

//end of modifiable zone(Javadoc).........E/73c4ec94-54b9-4ee7-9c69-db87351731a2
    String removeByID(final String ID);

}
