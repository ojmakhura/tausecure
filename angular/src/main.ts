/*
 * Entry point of the application.
 * Only platform bootstrapping code should be here.
 * For app-specific initialization, use `app/app.component.ts`.
 */

import { enableProdMode, importProvidersFrom, isDevMode, provideZoneChangeDetection } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from '@app/app.component';
import { appConfig } from '@app/app.config';
import {
  AutoRefreshTokenService,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  includeBearerTokenInterceptor,
  provideKeycloak,
  UserActivityService,
  withAutoRefreshToken,
} from 'keycloak-angular';

import { environment } from '@env/environment';
import { UseCaseScope } from '@app/utils/use-case-scope';
import { provideRouter, RouteReuseStrategy, withHashLocation } from '@angular/router';
import { routes } from '@app/app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { apiPrefixInterceptor } from '@app/@core/http/api-prefix.interceptor';
import { errorHandlerInterceptor } from '@app/@core/http/error-handler.interceptor';
import { provideStoreDevtools, StoreDevtoolsModule } from '@ngrx/store-devtools';
import { TranslateModule } from '@ngx-translate/core';
import { ServiceWorkerModule } from '@angular/service-worker';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { RouteReusableStrategy } from '@app/@shared';
import { provideToastr } from 'ngx-toastr';

if (environment.production) {
  enableProdMode();
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/ngsw-worker.js');
  }
}

fetch('env.json')
  .then((response) => response.json())
  .then((env) => {
    console.log(env);
    bootstrapApplication(AppComponent, {
      providers: [
        UseCaseScope,
        provideKeycloak({
          config: {
            url: env.authDomain,
            realm: env.realm,
            clientId: env.clientId,
          },
          initOptions: {
            onLoad: 'check-sso',
            silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
          },
          features: [
            withAutoRefreshToken({
              onInactivityTimeout: 'logout',
              sessionTimeout: 60000,
            }),
          ],
          providers: [AutoRefreshTokenService, UserActivityService],
        }),
        {
          provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
          useValue: [
            {
              urlPattern: /^(\/.*)?$/i,
              bearerPrefix: 'Bearer',
            },
            {
              urlPattern: /^(https:\/\/keycloak\.roguedev\.local)(\/.*)?$/i,
              bearerPrefix: 'Bearer',
            }
          ],
        },
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes, withHashLocation()),
        provideAnimations(),
        provideToastr(),
        provideHttpClient(
          withInterceptorsFromDi(),
          withInterceptors([apiPrefixInterceptor, errorHandlerInterceptor, includeBearerTokenInterceptor]),
        ),
        provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
        importProvidersFrom(
          StoreDevtoolsModule.instrument({}),
          TranslateModule.forRoot(),
          ServiceWorkerModule.register('./ngsw-worker.js', { enabled: environment.production }),
        ),
        { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
        {
          provide: RouteReuseStrategy,
          useClass: RouteReusableStrategy,
        },
      ],
    }).catch((err) => console.error(err));
  })
  .catch((error) => {
    console.error('Error loading env.json:', error);
    bootstrapApplication(AppComponent, appConfig).catch((err) => console.error(err));
  });
