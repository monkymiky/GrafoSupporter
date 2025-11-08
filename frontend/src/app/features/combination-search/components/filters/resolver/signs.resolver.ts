import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { tap } from 'rxjs';
import {
  SignsService,
  SignApiResponseItem,
} from '../../combination-list/services/signs.service';
import { SharedStateService } from '../../../../../shared/services/shared-state.service';
import { Grado } from '../../../model/filter.interface';

export const signsResolver: ResolveFn<SignApiResponseItem[]> = () => {
  const signsService = inject(SignsService);
  const sharedState = inject(SharedStateService);

  if (sharedState.signs().length > 0) {
    return sharedState.signs();
  }

  return signsService.getSigns().pipe(
    tap((signs) => {
      sharedState.signs.set(signs);

      const initialFilters = new Map<number, Grado>();
      for (const sign of signs) {
        initialFilters.set(sign.id, Grado.ASSENTE);
      }
      sharedState.filters.set(initialFilters);

      console.log(
        'Signs Resolver: Dati dei segni caricati e stato inizializzato.'
      );
    })
  );
};
