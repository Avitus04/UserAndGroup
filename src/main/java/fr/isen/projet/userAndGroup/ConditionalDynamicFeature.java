package fr.isen.projet.userAndGroup;

import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConditionalDynamicFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        // Vérifie si la méthode ou la classe est annotée avec @RequiresCondition
        if (resourceInfo.getResourceMethod().isAnnotationPresent(RequiresCondition.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(RequiresCondition.class)) {
            context.register(RouteBlockingFilter.class); // Applique le filtre
        }
    }
}
