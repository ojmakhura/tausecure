import { Component, effect, inject, OnDestroy, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, NavigationEnd, Router, RouterModule, RouterOutlet } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { merge } from 'rxjs';
import { filter, map, switchMap } from 'rxjs/operators';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '@env/environment';
import { MaterialModule } from './material.module';
import { I18nService } from './i18n/i18n.service';
import { Logger } from './@shared';
import { AppEnvStore } from './store/app-env.state';

import Keycloak from 'keycloak-js';
import { AuthorisationApiStore } from './store/bw/co/roguesystems/tau/authorisation/authorisation-api.store';
import { AuthorisationApi } from './service/bw/co/roguesystems/tau/authorisation/authorisation-api';
import { HttpClient } from '@angular/common/http';
import { SelectItem } from './utils/select-item';
import * as nav from './shell/navigation';

const log = new Logger('App');

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  imports: [CommonModule, RouterOutlet, RouterModule, FormsModule, MaterialModule],
})
export class AppComponent implements OnInit, OnDestroy {
  router = inject(Router);
  activatedRoute = inject(ActivatedRoute);
  private titleService = inject(Title);
  private translateService = inject(TranslateService);
  private i18nService = inject(I18nService);
  readonly appStore = inject(AppEnvStore);
  protected keycloak = inject(Keycloak);
  private authorisationStore = inject(AuthorisationApiStore);
  private authorisationApi = inject(AuthorisationApi);
  http = inject(HttpClient);
  env = this.appStore.env;

  loadingEnv = false;

  constructor() {
    effect(() => {
      if (!this.env) {
        return;
      }

      let e = this.env();
      if (e) {
        if (e && this.loadingEnv) {
          this.loadRealmRoles(e);
          this.loadingEnv = false;
        }
      }

      let realmRoles = this.appStore.realmRoles();
    });
  }

  ngOnInit() {
    this.appStore.setAuthorisedPathsLoaded(false);
    this.loadingEnv = true;
    this.appStore.getEnv();

    // Setup logger
    if (environment.production) {
      Logger.enableProductionMode();
    }

    log.debug('init');

    // Setup translations
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);

    const onNavigationEnd = this.router.events.pipe(filter((event) => event instanceof NavigationEnd));

    // Change page title on navigation or language change, based on route data
    merge(this.translateService.onLangChange, onNavigationEnd)
      .pipe(
        map(() => {
          let route = this.activatedRoute;
          while (route.firstChild) {
            route = route.firstChild;
          }
          return route;
        }),
        filter((route) => route.outlet === 'primary'),
        switchMap((route) => route.data),
        // untilDestroyed(this),
      )
      .subscribe((event) => {
        const title = event['title'];
        if (title) {
          this.titleService.setTitle(this.translateService.instant(title));
        }
      });

    // this.keycloak.keycloakEvents$.subscribe({
    //   next: (event) => {
    //     if (event.type === KeycloakEventType.OnAuthLogout) {
    //       this.appStore.reset();
    //       this.router.navigate(['/']);
    //     }
    //   },
    // });

    if (!this.keycloak.authenticated) {
      this.appStore.setAuthorisedPathsLoaded(true);
    }
  }

  ngOnDestroy() {
    this.i18nService.destroy();
  }

  loadRealmRoles(env: any) {
    if (this.keycloak.authenticated) {

      this.keycloak.loadUserInfo().then((userInfo) => {
        console.log(userInfo);
        this.appStore.setUsername((userInfo || {} as any)['preferred_username']);
        console.log(this.keycloak.userInfo);
      });

      console.log(this.keycloak.clientId)

      let realmUrl = `${env.authDomain}/realms/devel`//${environment.keycloak.realm}`;
      console.log(realmUrl);
      console.log(this.keycloak);
      console.log(this.keycloak.realmAccess);
      console.log(this.keycloak.resourceAccess);

      // this.keycloak.realmAccess?.roles.sort((a, b) => a.name.localeCompare(b.name))
      // .forEach((role) => {
      //   if (this.keycloak.getUserRoles().includes(role.name)) {
      //     let item = new SelectItem();
      //     item.label = role['description'];
      //     item.value = role['name'];

      //     this.appStore.addRealmRole(item);
      //   }
      // });

      this.keycloak.loadUserProfile().then((profile) => {
        console.log(profile);
        this.appStore.setIsLoggedIn(this.keycloak.authenticated || false);

        if (!profile) return;

        this.appStore.setAccountUri(
          `${env.authDomain}/realms/${environment.keycloak.realm}/account?referrer=' + ${encodeURIComponent(this.keycloak.clientId || '')}&referrer_uri=' + ${encodeURIComponent(environment.keycloak.redirectUri)}`,
        );
        

        // this.http.get<any[]>(`${realmUrl}/clients`).subscribe((clients) => {
        //   console.log(clients);
        //   // let client = clients.filter((client) => client.clientId === environment.keycloak.clientId)[0];
        //   this.http
        //     .get<any[]>(`${realmUrl}/users/${profile.id}/role-mappings/clients/${this.keycloak.clientId}/composite`)
        //     .subscribe((roles) => {
        //       console.log(roles);
        //       roles
        //         .sort((a, b) => a.name.localeCompare(b.name))
        //         .forEach((role) => {
        //           // if (this.keycloak.getUserRoles().includes(role.name)) {
        //           //   let item = new SelectItem();
        //           //   item.label = role['description'];
        //           //   item.value = role['name'];

        //           //   this.appStore.addRealmRole(item);
        //           // }
        //         });

        //       this.loadAuthorisedPaths();
        //     });
        // });

      //   this.http.get<any[]>(`${realmUrl}/users/${profile.id}/role-mappings/realm/composite`).subscribe((roles) => {
      //     roles
      //       .sort((a, b) => a.name.localeCompare(b.name))
      //       .forEach((role: any) => {
      //         // if (this.keycloak..includes(role.name) && !role.description?.includes('${')) {
      //         //   let item = new SelectItem();
      //         //   item.label = role['description'];
      //         //   item.value = role['name'];

      //         //   this.appStore.addRealmRole(item);
      //         // }
      //       });
      //     this.loadAuthorisedPaths();
      //   });
      });
    }
  }

  loadAuthorisedPaths() {
    let loggedIn = this.keycloak.authenticated;

    if (loggedIn && this.appStore.realmRoles().length > 0) {
      this.appStore.setAuthorisedPathsLoaded(false);
      let menus = new Array<string>();
      menus.push('MENU');
      menus.push('VIEW');

      this.authorisationApi
        .getAccessTypeCodeAuthorisations(
          this.appStore.realmRoles().map((role) => role.value),
          menus,
        )
        .subscribe({
          next: (authorisations) => {
            this.appStore.addMenus(
              nav.menuItems.map((menu) => {
                let m = authorisations.find((auth) => auth.accessPointUrl === menu.routerLink);
                if (m) {
                  return menu;
                }
              }),
            );

            this.appStore.setLoadingMenus(false);
            this.appStore.setAuthorisedPaths(authorisations.map((auth) => auth.accessPointUrl));
            this.appStore.setAuthorisedPathsLoaded(true);
          },
          error: (error) => {
            this.appStore.setAuthorisedPathsLoaded(true);
          },
          complete: () => {},
        });
    }
  }
}
