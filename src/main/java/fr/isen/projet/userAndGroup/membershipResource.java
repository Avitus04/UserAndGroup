package fr.isen.projet.userAndGroup;

import fr.isen.projet.userAndGroup.impl.services.membershipServiceImpl;
import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.services.membershipService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;


import java.util.List;

@Path("/") // Chemin de base de la ressource REST
@Produces(MediaType.APPLICATION_JSON) // Les méthodes retourneront du JSON
@Consumes(MediaType.APPLICATION_JSON) // Les méthodes accepteront du JSON
public class membershipResource {

    private membershipService membershipServ;

    public membershipResource()
    {
        this.membershipServ = new membershipServiceImpl();
    }

    @GET
    @Path("/membership")
    public List<membership> getAllMembers(){
        return membershipServ.getAll();
    }

    // peut servir à appeller le CRUD d'une autre equipe
    @GET
    @Path("/address")
    public String getAllAddress(){
        return membershipServ.showAddress();
    }

    @GET
    @Path("/membership/{id}")
    public membership getById(@PathParam("id") String id){
        return this.membershipServ.getByID(id);
    }

    @POST
    @Path("/membership")
    public String create(membership member) throws Exception {
        return this.membershipServ.add(member);
    }

    @PUT
    @Path("/membership/{id}")
    public String update(@PathParam("id") String Id, membership updatedMember) throws Exception {
        return this.membershipServ.update(Id, updatedMember);
    }

    @DELETE
    @Path("/membership/{id}")
    public String delete(@PathParam("id") String Id) throws Exception {
        return this.membershipServ.removeByID(Id);
    }

    @POST
    @Path("/membership/{id}")
    public Response login( LoginResource.Credentials credentials) throws Exception {
    return null;
    }

}

