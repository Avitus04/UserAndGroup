package fr.isen.projet.userAndGroup;

import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;

@Provider
@Priority(Priorities.AUTHENTICATION) // Priorité élevée (ex. : AUTHENTICATION = 1000)
public class RouteBlockingFilter implements ContainerRequestFilter {

    private static boolean conditionMet = false; // Remplacez par votre logique réelle

    @Override
    public void filter(ContainerRequestContext context) {
        String path = context.getUriInfo().getPath();
        // Vérifiez si le chemin correspond à la route accessible
        if (path.equals("/api/login")) {
            return; // Autorise l'accès à cette route
        }

        if (!conditionMet) {
            context.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\": \"Action requise avant l'accès à cette ressource.\"}")
                    .build());
        }
    }

    // Méthode pour changer la condition
    public static void setConditionMet(boolean conditionMet) {
        RouteBlockingFilter.conditionMet = conditionMet;
    }
}
