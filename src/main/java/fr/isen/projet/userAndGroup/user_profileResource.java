package fr.isen.projet.userAndGroup;

import fr.isen.projet.userAndGroup.impl.services.user_profileServiceImpl;
import fr.isen.projet.userAndGroup.interfaces.models.user_profile;
import fr.isen.projet.userAndGroup.interfaces.services.user_profileService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/") // Chemin de base de la ressource REST
@Produces(MediaType.APPLICATION_JSON) // Les méthodes retourneront du JSON
@Consumes(MediaType.APPLICATION_JSON) // Les méthodes accepteront du JSON
public class user_profileResource {

    private user_profileService userProfileService;

    public user_profileResource()
    {
        this.userProfileService = new user_profileServiceImpl();
    }

    @GET
    @Path("/user_profile")
    public List<user_profile> getAllProfiles(){
        return userProfileService.getAll();
    }

    @GET
    @Path("/user_profile/{id}")
    public user_profile getById(@PathParam("id") String id){
        return this.userProfileService.getByID(id);
    }

    @POST
    @Path("/user_profile")
    public String create(user_profile userProfile) throws Exception {
        return this.userProfileService.add(userProfile);
    }

    @PUT
    @Path("/user_profile/{id}")
    public String update(@PathParam("id") String Id, user_profile updatedUserProfile) throws Exception {
        return this.userProfileService.update(Id, updatedUserProfile);
    }

    @DELETE
    @Path("/user_profile/{id}")
    public String delete(@PathParam("id") String Id) throws Exception {
        return this.userProfileService.removeByID(Id);
    }

}

