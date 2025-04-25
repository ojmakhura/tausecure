import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@app/model/search-object';
import { Page } from '@app/model/page.model';
import { AccessPointTypeDTO } from '@app/model/bw/co/roguesystems/tau/access/type/access-point-type-dto';
import { AccessPointTypeApi } from '@app/service/bw/co/roguesystems/tau/access/type/access-point-type-api';

export type AccessPointTypeApiState = AppState<any, any> & {};

const initialState: AccessPointTypeApiState = {
  data: null,
  dataList: [],
  dataPage: new Page<any>(),
  searchCriteria: new SearchObject<any>(),
  error: null,
  loading: false,
  success: false,
  messages: [],
  loaderMessage: '',
};

export const AccessPointTypeApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const accessPointTypeApi = inject(AccessPointTypeApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findById: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.findById(data.id).pipe(
            tapResponse({
              next: (data: AccessPointTypeDTO | any) => {
                patchState(
                store,
                {
                   data,
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.getAll().pipe(
            tapResponse({
              next: (dataList: AccessPointTypeDTO[] | any[]) => {
                patchState(
                store,
                {
                  dataList,
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
      getAllPaged: rxMethod<{ pageNumber: number | any; pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.getAllPaged(data.pageNumber, data.pageSize).pipe(
            tapResponse({
              next: (dataPage: Page<AccessPointTypeDTO>) => {
                patchState(
                store,
                {
                  dataPage,
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
      pagedSearch: rxMethod<{ criteria: SearchObject<string> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.pagedSearch(data.criteria).pipe(
            tapResponse({
              next: (dataPage: Page<AccessPointTypeDTO>) => {
                patchState(
                store,
                {
                  dataPage,
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
      remove: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.remove(data.id).pipe(
            tapResponse({
              next: (data: boolean | any) => {
                patchState(
                store,
                {
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
      save: rxMethod<{ accessPointType: AccessPointTypeDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.save(data.accessPointType).pipe(
            tapResponse({
              next: (data: AccessPointTypeDTO) => {
                patchState(
                store,
                {
                   data,
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
      search: rxMethod<{ criteria: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointTypeApi.search(data.criteria).pipe(
            tapResponse({
              next: (dataList: AccessPointTypeDTO[] | any[]) => {
                patchState(
                store,
                {
                  dataList,
                   loading: false,
                   error: false,
                   success: true,
                   messages: []
                }
                );
              },
              error: (error: any) => {
                patchState(store, {
                  error,
                  loading: false,
                  success: false,
                  messages: [error?.error ? error.error : error],
                });
              },
            }),
          );
        }),
      ),
    };
  }),
);
