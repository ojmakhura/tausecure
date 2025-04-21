package bw.co.roguesystems.tau.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class KeycloakService {
    
    public Keycloak getKeycloak() {
        Jwt jwt = getJwt();

        String iss = jwt.getClaimAsString("iss");

        int i = iss.lastIndexOf("/");
        String realm = iss.substring(i + 1);
        i = iss.indexOf("/realms");
        String serverUrl = iss.substring(0, i);
        String clientId = jwt.getClaimAsString("azp");

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .authorization(jwt.getTokenValue())
                .build();
        return keycloak;
    }

    public Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) authentication.getPrincipal();
    }

    public RealmResource getRealmResource() {
        
        Jwt jwt = getJwt();
        String iss = jwt.getClaimAsString("iss");
        int i = iss.lastIndexOf("/");
        String realm = iss.substring(i + 1);

        return getKeycloak().realm(realm);
    }

    public String getAuthClient() {

        Jwt jwt = getJwt();

        return jwt.getClaimAsString("azp");
    }

    private String getRealm() {
        Jwt jwt = getJwt();

        int i = jwt.getClaimAsString("iss").lastIndexOf("/");
        return jwt.getClaimAsString("iss").substring(i + 1);
    }   

    public ClientRepresentation findAuthenticatedClientResource() {
        for (ClientRepresentation clientRep : getRealmResource().clients().findAll()) {
            if (clientRep.getClientId().equals(getAuthClient())) {
                return clientRep;
            }
        }

        return null;
    }

    public UsersResource getUsersResource() {

        Keycloak keycloak = getKeycloak();

        return keycloak.realm(getRealm()).users();
    }

    public RolesResource getRealmRolesResource() {
        Keycloak keycloak = getKeycloak();

        return keycloak.realm(getRealm()).roles();
    }

    public RolesResource getClientRolesResource(String clientId) {

        Keycloak keycloak = getKeycloak();

        return keycloak.realm(getRealm()).clients().get(clientId).roles();
    }

    public ClientsResource getClientsResource() {

        return getRealmResource().clients();
    }
}
