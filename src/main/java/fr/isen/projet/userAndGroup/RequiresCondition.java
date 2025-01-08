package fr.isen.projet.userAndGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // Peut être appliqué aux classes ou aux méthodes
@Retention(RetentionPolicy.RUNTIME) // Disponible au moment de l'exécution
public @interface RequiresCondition {
}
