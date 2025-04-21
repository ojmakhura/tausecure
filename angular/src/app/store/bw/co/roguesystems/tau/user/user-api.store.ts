
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@app/model/search-object';
import { Page } from '@app/model/page.model';
import { UserDTO } from '@app/model/bw/co/roguesystems/tau/user/user-dto';
import { UserApi } from '@app/service/bw/co/roguesystems/tau/user/user-api';

export type UserApiState = AppState<any, any> & {};

const initialState: UserApiState = {
  data: null,
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  error: null,
  loading: false,
  success: false,
  messages: [],
  loaderMessage: ''
};

export const UserApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const userApi = inject(UserApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      addRole: rxMethod<{userId: string | any , role: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.addRole(data.userId, data.role, ).pipe(
            tapResponse({
              next: (data: boolean | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      changePassword: rxMethod<{userId: string | any , newPassword: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.changePassword(data.userId, data.newPassword, ).pipe(
            tapResponse({
              next: (data: string | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByClientRoles: rxMethod<{roles: Array<string> | any , clientId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.findByClientRoles(data.roles, data.clientId, ).pipe(
            tapResponse({
              next: (data: UserDTO[] | any[]) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByDepartmentId: rxMethod<{departmentId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.findByDepartmentId(data.departmentId, ).pipe(
            tapResponse({
              next: (data: UserDTO[] | any[]) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByDepartmentName: rxMethod<{departmentName: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.findByDepartmentName(data.departmentName, ).pipe(
            tapResponse({
              next: (data: UserDTO[] | any[]) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      findByRealmRoles: rxMethod<{roles: Array<string> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.findByRealmRoles(data.roles, ).pipe(
            tapResponse({
              next: (data: UserDTO[] | any[]) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      findUserById: rxMethod<{userId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.findUserById(data.userId, ).pipe(
            tapResponse({
              next: (data: UserDTO | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      loadUsers: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.loadUsers().pipe(
            tapResponse({
              next: (data: UserDTO[] | any[]) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      removeRole: rxMethod<{userId: string | any , role: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.removeRole(data.userId, data.role, ).pipe(
            tapResponse({
              next: (data: boolean | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      saveUser: rxMethod<{user: UserDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.saveUser(data.user, ).pipe(
            tapResponse({
              next: (data: UserDTO | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      search: rxMethod<{criteria: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.search(data.criteria, ).pipe(
            tapResponse({
              next: (data: UserDTO | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
      updateUserName: rxMethod<{userId: string | any , username: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return userApi.updateUserName(data.userId, data.username, ).pipe(
            tapResponse({
              next: (data: boolean | any) => {
                //patchState(
                  //store, 
                  // { 
                  //    data, 
                  //    loading: false, 
                  //    error: false,
                  //    success: true, 
                  //    messages: [] 
                  //}
                //);
              },
              error: (error: any) => {
                patchState(
                  store, { 
                    error, 
                    loading: false, 
                    success: false,
                    messages: [error?.error ? error.error : error] 
                  }
                );
              },
            }),
          );
        }),
      ),
    }
  }),
);
