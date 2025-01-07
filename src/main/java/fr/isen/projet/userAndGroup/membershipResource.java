package fr.isen.projet.userAndGroup;

import fr.isen.projet.userAndGroup.impl.services.membershipServiceImpl;
import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.services.membershipService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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

    @GET
    @Path("/membership/{id}")
    public membership getById(@PathParam("id") String id){
        return this.membershipServ.getByID(id);
    }

    @POST
    @Path("/membership")
    public String createTicket(membership member) throws Exception {
        return this.membershipServ.add(member);
    }

    @PUT
    @Path("/membership/{id}")
    public String updateTicket(@PathParam("id") String Id, membership updatedMember) throws Exception {
        return this.membershipServ.update(Id, updatedMember);
    }

    @DELETE
    @Path("/membership/{id}")
    public String deleteTicket(@PathParam("id") String Id) throws Exception {
        return this.membershipServ.removeByID(Id);
    }

}

