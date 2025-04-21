package bw.co.roguesystems.tau.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import bw.co.roguesystems.tau.authorisation.Authorisation;
import bw.co.roguesystems.tau.authorisation.AuthorisationListDTO;
import bw.co.roguesystems.tau.authorisation.AuthorisationRepository;
import bw.co.roguesystems.tau.authorisation.AuthorisationService;

@Aspect
@Component
public class SecurityAuthorisationCheckAspect {

    private final AuthorisationService authorisationService;

    public SecurityAuthorisationCheckAspect(AuthorisationService authorisationService) {
        this.authorisationService = authorisationService;
    }

    @Before("@annotation(bw.co.roguesystems.tau.security.SecurityAuthorisationCheck)")
    public Boolean isAuthorised(JoinPoint joinPoint) throws Throwable {

        Method matchedMethod = null;
        List<Class> params = null;  
        
        for (Method method : joinPoint.getThis().getClass().getDeclaredMethods()) {
            
            boolean matched = true;
            
            // Make sure the names are the same and the parameter lengths are the same as well
            if (method.getName().equals(joinPoint.getSignature().getName()) && joinPoint.getArgs().length == method.getParameterTypes().length) {

                params = new ArrayList<>();  

                for (int i = 0; i < method.getParameterTypes().length; i++) {       
                    
                    if(joinPoint.getArgs()[i] != null && !method.getParameterTypes()[i].isAssignableFrom(joinPoint.getArgs()[i].getClass())) {
                        matched = false;
                        break; 
                    }

                    params.add(method.getParameterTypes()[i]);

                }
                
            } else {
                matched = false;
            }

            if(matched) {
                matchedMethod = method;
                break;
            }
        }

        if(matchedMethod == null) {
            throw new AuthenticationServiceException("No matching method found");
        }

        Method method = joinPoint.getSignature().getDeclaringType().getMethod(joinPoint.getSignature().getName(),
                matchedMethod.getParameterTypes());

        String path = method.getAnnotation(SecurityAuthorisationCheck.class).path();
        String methodCall = method.getAnnotation(SecurityAuthorisationCheck.class).method();

        boolean authorised = StringUtils.isBlank(methodCall) ? this.isAuthorised(path)
                : this.handleIsAuthorised(methodCall + " " + path);

        if (!authorised) {

            throw new AuthorizationServiceException("Not authorised");
        }

        return authorised;
    }

    public boolean isAuthorised(String apiEndpoint) {

        return this.handleIsAuthorised(apiEndpoint);
    }

    private boolean handleIsAuthorised(String url) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        /// Find the api with the requesting URL
        Collection<AuthorisationListDTO> auths = this.authorisationService.findByAccessUrlAndTypeCode(url, "API");

        // If there are no authorisations, we assume the request is authorised by
        // default
        if (CollectionUtils.isEmpty(auths)) {
            return true;
        }

        if (authentication.getDetails() instanceof SimpleKeycloakAccount) {
            SimpleKeycloakAccount acc = (SimpleKeycloakAccount) authentication.getDetails();
            AccessToken token = acc.getKeycloakSecurityContext().getToken();
            Access access = token.getResourceAccess(token.getIssuedFor()); // Get the authenticated client

            Set<String> roles = access != null ? access.getRoles() : new HashSet<>();
            if (roles == null) {
                roles = token.getRealmAccess().getRoles();
            } else {
                roles.addAll(token.getRealmAccess().getRoles());
            }

            for (AuthorisationListDTO auth : auths) {

                for (String role : auth.getRoles().split(",")) {
                    if (role.equals("UNSECURED") || roles.contains(role) ) {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }

        return false;
    }
}
