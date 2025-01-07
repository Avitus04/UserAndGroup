package fr.isen.projet.userAndGroup.interfaces.services;

import java.util.List;
import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.models.token;

//begin of modifiable zone(Javadoc).......C/8519a586-3e01-4a85-8530-f3b4dd155980

//end of modifiable zone(Javadoc).........E/8519a586-3e01-4a85-8530-f3b4dd155980
public interface membershipService {
//begin of modifiable zone(Javadoc).......C/5ceed5ca-4a60-417a-ac28-360b9fba6abe

//end of modifiable zone(Javadoc).........E/5ceed5ca-4a60-417a-ac28-360b9fba6abe
    List<membership> getAll();

//begin of modifiable zone(Javadoc).......C/d2db7746-c1ee-44be-ada1-de52c42f0775

//end of modifiable zone(Javadoc).........E/d2db7746-c1ee-44be-ada1-de52c42f0775
    membership getByID(final String ID);

//begin of modifiable zone(Javadoc).......C/354bfe10-741c-4142-9cd6-009905d7293b

//end of modifiable zone(Javadoc).........E/354bfe10-741c-4142-9cd6-009905d7293b
    String add(final membership data);

//begin of modifiable zone(Javadoc).......C/9b7a641a-b256-46b8-ae11-306f411976f1

//end of modifiable zone(Javadoc).........E/9b7a641a-b256-46b8-ae11-306f411976f1
    String update(final String ID, final membership data);

//begin of modifiable zone(Javadoc).......C/9c73e966-1c4d-47c4-8462-2b223a8dea77

//end of modifiable zone(Javadoc).........E/9c73e966-1c4d-47c4-8462-2b223a8dea77
    String removeByID(final String ID);

//begin of modifiable zone(Javadoc).......C/05a55c2e-95df-4d52-8fcd-4bfef5c74e8f

//end of modifiable zone(Javadoc).........E/05a55c2e-95df-4d52-8fcd-4bfef5c74e8f
    token connection(final String login, final String password);

//begin of modifiable zone(Javadoc).......C/b6797aa7-74a5-4b3b-9fd5-9441665505fb

//end of modifiable zone(Javadoc).........E/b6797aa7-74a5-4b3b-9fd5-9441665505fb
    String encrypt(final String pwd);

}
