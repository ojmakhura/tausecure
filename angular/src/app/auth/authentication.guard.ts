import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthGuardData, createAuthGuard } from 'keycloak-angular';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles } = authData;

  if(!authenticated) {

    authData.keycloak.login();
  }

  if (authenticated) {
    return true;
  }

  const router = inject(Router);
  return router.parseUrl('/forbidden');
};

export const AuthenticationGuard = createAuthGuard<CanActivateFn>(isAccessAllowed);
