package bw.co.roguesystems.tau.keycloak;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import bw.co.roguesystems.tau.user.UserDTO;

@Component
public class KeycloakUserService {

    protected Logger logger = LoggerFactory.getLogger(KeycloakUserService.class);

    String[] excludedRoles = { "offline_access", "uma_authorization", "default-roles-bocraportal" };

    private final KeycloakService keycloakService;

    public KeycloakUserService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    private CredentialRepresentation createCredential(String type, String value, Boolean temporary) {
        CredentialRepresentation cred = new CredentialRepresentation();

        cred.setType(type);
        cred.setValue(value);
        cred.setTemporary(temporary);

        return cred;
    }

    private ClientRepresentation findAuthenticatedClientResource() {
        for (ClientRepresentation clientRep : keycloakService.getClientsResource().findAll()) {
            if (clientRep.getClientId().equals(keycloakService.getAuthClient())) {
                return clientRep;
            }
        }

        return null;
    }

    public UserDTO findByUsername(String username) {

        UsersResource usersResource = keycloakService.getUsersResource();
        List<UserRepresentation> users = usersResource.search(username, true);

        return CollectionUtils.isEmpty(users) ? null : userRepresentationUserDTO(users.get(0));
    }

    public UserDTO getLoggedInUser() {

        Jwt jwt = keycloakService.getJwt();

        String userId = jwt.getSubject();

        UsersResource usersResource = keycloakService.getUsersResource();
        UserRepresentation userRep = usersResource.get(userId).toRepresentation();

        return userRepresentationUserDTO(userRep);
    }

    public Collection<UserDTO> getUsersByRoles(String client, Set<String> roles) {

        RolesResource rolesResource = keycloakService.getClientRolesResource(client);
        return this.getRolesResourceUsers(rolesResource, roles);
    }

    private Collection<UserDTO> getRolesResourceUsers(RolesResource rolesResource, Set<String> roles) {
        Map<String, UserDTO> users = new HashMap<>();
        for (String role : roles) {
            Set<UserDTO> uvo = rolesResource.get(role).getRoleUserMembers().stream()
                    .map(user -> userRepresentationUserDTO(user)).collect(Collectors.toSet());

            uvo.stream().forEach(user -> users.put(user.getUserId(), user));
        }

        return users.values();
    }

    public Collection<UserDTO> getUsersByRoles(Set<String> roles) {
        RolesResource rolesResource = keycloakService.getRealmRolesResource();
        return this.getRolesResourceUsers(rolesResource, roles);
    }

    private UserRepresentation UserDTOUserRepresentation(UserDTO user) {

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEnabled(user.getEnabled());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));
        userRepresentation.setCredentials(Collections
                .singletonList(createCredential(CredentialRepresentation.PASSWORD, user.getPassword(), true)));

        Map<String, List<String>> attributes = new HashMap<>();

        

        // if(StringUtils.isNotBlank(user.getDepartmentId())) {

        //     attributes.put("departmentId", Arrays.asList(user.getDepartmentId()));
        // }

        // if(StringUtils.isNotBlank(user.getDepartment())) {

        //     attributes.put("department", Arrays.asList(user.getDepartment()));
        // }

        if(attributes.size() > 0) {
            userRepresentation.setAttributes(attributes);
        }

        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            userRepresentation.setRealmRoles(user.getRoles().stream().collect(Collectors.toList()));
        }

        return userRepresentation;
    }

    private UserDTO userRepresentationUserDTO(UserRepresentation userRepresentation) {
        UserDTO user = new UserDTO();

        user.setUserId(userRepresentation.getId());
        user.setEmail(userRepresentation.getEmail());
        user.setEnabled(userRepresentation.isEnabled());
        user.setFirstName(userRepresentation.getFirstName());
        user.setUsername(userRepresentation.getUsername());
        user.setLastName(userRepresentation.getLastName());
        user.setRoles(new ArrayList<>());

        if (userRepresentation.getAttributes() != null &&
                !userRepresentation.getAttributes().isEmpty()) {
            // List<String> departmentIds = userRepresentation.getAttributes().get("departmentId");
            // List<String> departmentNames = userRepresentation.getAttributes().get("departmentName");

            // if(CollectionUtils.isNotEmpty(departmentNames)) {
            //     user.setDepartment(departmentNames.get(0));
            // }

            // if(CollectionUtils.isNotEmpty(departmentIds)) {
            //     user.setDepartmentId(departmentIds.get(0));
            // }

            // List<String> orgIds = userRepresentation.getAttributes().get("organisationId");
            // List<String> orgNames = userRepresentation.getAttributes().get("organisationName");


            // if(CollectionUtils.isNotEmpty(orgNames)) {
            //     user.setOrganisation(orgNames.get(0));
            // }

            // if(CollectionUtils.isNotEmpty(orgIds)) {
            //     user.setOrganisationId(orgIds.get(0));
            // }

        } else {
            // user.setDepartment(null);
            // user.setOrganisation(null);
        }

        RealmResource realmResource = keycloakService.getRealmResource();
        UserResource userResource = realmResource.users().get(userRepresentation.getId());
        List<RoleRepresentation> roles = userResource.roles().realmLevel().listAll();

        roles = roles.stream().filter(role -> !ArrayUtils.contains(excludedRoles, role.getName()))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(roles)) {

            for (RoleRepresentation roleRep : roles) {
                user.getRoles().add(roleRep.getName());
            }
        }

        return user;
    }

    public Collection<UserDTO> getDepartmentUsers(String departmentId) {

        List<UserRepresentation> userRep = keycloakService.getUsersResource()
                .searchByAttributes("departmentId:" + departmentId);

        Collection<UserDTO> users = new ArrayList<>();

        for (UserRepresentation user : userRep) {
            UserDTO vo = this.userRepresentationUserDTO(user);
            users.add(vo);
        }

        return users;
    }
    // public Collection<UserDTO> getOrganisationUsers(String organisationId) {

    //     List<UserRepresentation> userRep = keycloakService.getUsersResource()
    //             .searchByAttributes("organisationId:" + organisationId);

    //     Collection<UserDTO> users = new ArrayList<>();

    //     for (UserRepresentation user : userRep) {
    //         UserDTO vo = this.userRepresentationUserDTO(user);
    //         users.add(vo);
    //     }

    //     return null;
    // }

    public boolean updateUserPassword(String userId, String newPassword) {

        if (StringUtils.isNotBlank(userId)) {
            userId = keycloakService.getJwt().getSubject();
        }

        UsersResource usersResource = keycloakService.getUsersResource();
        UserResource userResource = usersResource.get(userId);
        CredentialRepresentation credential = createCredential(CredentialRepresentation.PASSWORD, newPassword, false);
        userResource.resetPassword(credential);

        return true;
    }

    public void updateUser(UserDTO user) {

        if (StringUtils.isNotBlank(user.getUserId())) {
            UsersResource usersResource = keycloakService.getUsersResource();
            UserResource userResource = usersResource.get(user.getUserId());

            UserRepresentation userRep = userResource.toRepresentation();
            userRep.setEmail(user.getEmail());
            userRep.setFirstName(user.getFirstName());
            userRep.setLastName(user.getLastName());
            userRep.setEnabled(user.getEnabled());

            userResource.update(userRep);
        }
    }

    public static String getCreatedId(Response response) {
        URI location = response.getLocation();
        // if (!response.getStatusInfo().equals(Status.CREATED)) {
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            StatusType statusInfo = response.getStatusInfo();
            response.bufferEntity();
            String body = response.readEntity(String.class);
            throw new WebApplicationException("Create method returned status "
                    + statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode()
                    + "); expected status: Created (201). Response body: " + body, response);
        }

        if (location == null) {
            return null;
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    public ResponseEntity<?> createUser(UserDTO user) {

        UsersResource usersResource = keycloakService.getUsersResource();
        UserRepresentation userRepresentation = this.UserDTOUserRepresentation(user);
        UserResource userResource = null;

        if (StringUtils.isBlank(user.getUserId())) {

            Response res = usersResource.create(userRepresentation);

            if (res.getStatus() != HttpStatus.CREATED.value()) {
                return ResponseEntity.status(res.getStatus()).body(getCreatedId(res));
            }

            userResource = usersResource.get(getCreatedId(res));
            user.setUserId(getCreatedId(res));
        } else {
            userResource = usersResource.get(user.getUserId());
        }

        if (userResource != null) {

            List<RoleRepresentation> roleReps = new ArrayList<>();

            for (String role : user.getRoles()) {
                RolesResource rolesResource = keycloakService.getRealmRolesResource();
                rolesResource.get(role);

                RoleRepresentation roleRep = rolesResource.get(role).toRepresentation();
                if (StringUtils.isNotBlank(roleRep.getId())) {
                    roleReps.add(roleRep);
                }
            }

            if (CollectionUtils.isNotEmpty(roleReps)) {
                userResource.roles().realmLevel().add(roleReps);
            }

        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User not created!");
        }

        return ResponseEntity.ok(user);
    }

    public Collection<UserDTO> loadUsers() {

        UserDTO loggedInUser = this.getLoggedInUser();
        // boolean hasDepartment = loggedInUser.getDepartment() != null;
        // boolean hasOrganisation = loggedInUser.getOrganisation() != null;

        UsersResource usersResource = keycloakService.getUsersResource();

        List<UserRepresentation> userRep = null;

        // if(hasDepartment) {
        //     userRep = usersResource.searchByAttributes("departmentId:" + loggedInUser.getOrganisationId() );
        // } else if (hasOrganisation) {
        //     userRep = usersResource.searchByAttributes("organisationId:" + loggedInUser.getOrganisationId());
            
        // } else {
            userRep = usersResource.list();
        // }

        Collection<UserDTO> users = new ArrayList<>();

        for (UserRepresentation user : userRep) {
            UserDTO vo = this.userRepresentationUserDTO(user);
            users.add(vo);
        }

        return users;
    }

    public UserDTO updateUserName(String username, String userId) {

        return null;
    }

    private List<UserDTO> userRepsToVOs(List<UserRepresentation> usersRep) {

        if (CollectionUtils.isEmpty(usersRep)) {
            return new ArrayList<>();
        }

        List<UserDTO> users = new ArrayList<>();

        for (UserRepresentation rep : usersRep) {
            users.add(userRepresentationUserDTO(rep));
        }

        return users;
    }

    public List<UserDTO> searchByAttributes(String criteria) {

        List<UserRepresentation> usersRep = keycloakService.getUsersResource().searchByAttributes(criteria);

        return this.userRepsToVOs(usersRep);
    }

    public List<UserDTO> search(String criteria) {

        System.out.println(333);

        UserDTO loggedInUser = this.getLoggedInUser();
        // boolean hasDepartment = loggedInUser.getDepartment() != null;
        // boolean hasOrganisation = loggedInUser.getOrganisation() != null;

        List<UserRepresentation> usersRep = null;

        // if(hasDepartment) {
        //     System.out.println(222);
        //     usersRep = keycloakService.getUsersResource()
        //             .searchByAttributes("departmentId:" + loggedInUser.getOrganisationId());
        // } else if (hasOrganisation) {
        //     System.out.println(1);

        //     usersRep = keycloakService.getUsersResource()
        //             .searchByAttributes("organisationId:" + loggedInUser.getOrganisationId());
            
        // } else {

            if(StringUtils.isBlank(criteria)) {
                System.out.println(21222);
                usersRep = keycloakService.getUsersResource().list();
            } else {
                System.out.println(7777);
                usersRep = keycloakService.getUsersResource().search(criteria);
            }
        // }

        if (StringUtils.isNotBlank(criteria)) {

            String lower = criteria.toLowerCase();

            usersRep = usersRep.stream().filter(user -> {
                return user.getUsername().toLowerCase().contains(lower)
                        || user.getEmail().toLowerCase().contains(lower)
                        || user.getFirstName().toLowerCase().contains(lower)
                        || user.getLastName().toLowerCase().contains(lower);
            }).collect(Collectors.toList());
        }

        return this.userRepsToVOs(usersRep);
    }

    public UserDTO addClientRoles(String clientId, Set<String> roles, String userId) {
        List<RoleRepresentation> roleReps = new ArrayList<>();
        UserResource userResource = keycloakService.getUsersResource().get(userId);
        RolesResource rolesResource = keycloakService.getRealmRolesResource();
        UserRepresentation rep = userResource.toRepresentation();

        if (StringUtils.isBlank(rep.getId())) {
            return null;
        }

        for (String role : roles) {

            RoleRepresentation roleRep = rolesResource.get(role).toRepresentation();
            if (StringUtils.isNotBlank(roleRep.getId())) {
                roleReps.add(roleRep);
            }
        }

        if (CollectionUtils.isNotEmpty(roleReps)) {
            userResource.roles().clientLevel(clientId).add(roleReps);
        }

        return userRepresentationUserDTO(rep);
    }

    public UserDTO findUserById(String userId) {
        UserRepresentation rep = keycloakService.getUsersResource().get(userId).toRepresentation();

        if (StringUtils.isNotBlank(rep.getId())) {
            return userRepresentationUserDTO(rep);
        }

        return null;
    }

    public boolean updateUserRoles(String userId, String role, int action) {

        RoleResource roleResource = keycloakService.getRealmRolesResource().get(role);

        if (roleResource == null) {
            throw new RuntimeException("Role not found!");
        }

        UserResource userResource = keycloakService.getUsersResource().get(userId);

        if (userResource == null) {
            throw new RuntimeException("User not found!");
        }

        RoleScopeResource roleScopeResource = userResource.roles().realmLevel();

        RoleRepresentation roleRep = roleResource.toRepresentation();

        if (action > 0) {

            roleScopeResource.add(Arrays.asList(roleRep));

        } else {
            roleScopeResource.remove(Arrays.asList(roleRep));
        }

        return true;
    }
}
