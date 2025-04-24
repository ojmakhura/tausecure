
import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { AppState } from '@app/store/app-state';
import { SearchObject } from '@app/model/search-object';
import { Page } from '@app/model/page.model';
import { AccessPointDTO } from '@app/model/bw/co/roguesystems/tau/access/access-point-dto';
import { AccessPointCriteria } from '@app/model/bw/co/roguesystems/tau/access/access-point-criteria';
import { AccessPointListDTO } from '@app/model/bw/co/roguesystems/tau/access/access-point-list-dto';
import { AccessPointApi } from '@app/service/bw/co/roguesystems/tau/access/access-point-api';

export type AccessPointApiState = AppState<any, any> & {};

const initialState: AccessPointApiState = {
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

export const AccessPointApiStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store: any) => {
    const accessPointApi = inject(AccessPointApi);
    return {
      reset: () => {
        patchState(store, initialState);
      },
      findApplicationAccessPoints: rxMethod<{ applicationId: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.findApplicationAccessPoints(data.applicationId,).pipe(
            tapResponse({
              next: (dataList: AccessPointListDTO[] | any[]) => {
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
      findApplicationAccessPointsPaged: rxMethod<{ applicationId: string | any, pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.findApplicationAccessPointsPaged(data.applicationId, data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (dataList: AccessPointListDTO[] | any[]) => {
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
      findById: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.findById(data.id,).pipe(
            tapResponse({
              next: (data: AccessPointDTO | any) => {
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
      getAll: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.getAll().pipe(
            tapResponse({
              next: (dataList: AccessPointListDTO[] | any[]) => {
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
      getAllPaged: rxMethod<{ pageNumber: number | any, pageSize: number | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.getAllPaged(data.pageNumber, data.pageSize,).pipe(
            tapResponse({
              next: (dataPage: Page<AccessPointListDTO> | any) => {
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
      pagedSearch: rxMethod<{ criteria: SearchObject<AccessPointCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.pagedSearch(data.criteria,).pipe(
            tapResponse({
              next: (dataPage: Page<AccessPointListDTO> | any) => {
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
      pagedSearchOr: rxMethod<{ criteria: SearchObject<AccessPointCriteria> | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.pagedSearchOr(data.criteria,).pipe(
            tapResponse({
              next: (dataPage: Page<AccessPointListDTO> | any) => {
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
      remove: rxMethod<{ id: string | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.remove(data.id,).pipe(
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
      save: rxMethod<{ accessPoint: AccessPointDTO | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.save(data.accessPoint,).pipe(
            tapResponse({
              next: (data: AccessPointDTO | any) => {
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
      search: rxMethod<{ criteria: AccessPointCriteria | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.search(data.criteria,).pipe(
            tapResponse({
              next: (dataList: AccessPointListDTO[] | any[]) => {
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
      searchOr: rxMethod<{ criteria: AccessPointCriteria | any }>(
        switchMap((data: any) => {
          patchState(store, { loading: true, loaderMessage: 'Loading ...' });
          return accessPointApi.searchOr(data.criteria,).pipe(
            tapResponse({
              next: (dataList: AccessPointListDTO[] | any[]) => {
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
