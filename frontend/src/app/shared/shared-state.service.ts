import { inject, Injectable, signal } from '@angular/core';
import { Combination } from '../search-combinations/combination-list/combination.interface';
import { CombiantionService } from './combinations.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, Observable, of, startWith, switchMap, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  combiantionsService = inject(CombiantionService);
  errorMessage = signal('');
  triggerCombinationSearch = signal(0);

  private combinationsObservable: Observable<Combination[]> = toObservable(
    this.triggerCombinationSearch
  ).pipe(
    startWith([]),
    switchMap(() => {
      return this.combiantionsService.searchCombinations(this.filters()).pipe(
        tap(() => this.errorMessage.set('')),
        catchError((err) => {
          this.errorMessage.set(
            `Errore caricamento combinazioni : ${err.message}`
          );
          return of([] as Combination[]);
        })
      );
    })
  );

  readonly combinationsDisplayed = toSignal(this.combinationsObservable, {
    initialValue: [] as Combination[],
  });

  filters = signal({
    largaTraLettere: 0,
    // attesa
    curva: 0,
    discendente: 0,
    titubante: 0,
    aperturaACapoDelleOeA: 0,
    asteColConcavoADestra: 0,
    pendente: 0,
    profusa: 0,
    fluida: 0,
    attaccata: 0,
    // resistenza
    angoliB: 0,
    mantieneIlRigo: 0,
    secca: 0,
    asteRette: 0,
    dritta: 0,
    recisa: 0,
    austera: 0,
    // assalto
    angoliA: 0,
    intozzata1Modo: 0,
    ascendente: 0,
    scattante: 0,
    asteColConcavoASinistra: 0,
    ardita: 0,
    slanciata: 0,
    impaziente: 0,
    spavalda: 0,
    acuta: 0,
    veloce: 0,
    solenne: 0,
    // Attesa
    intozzata2Modo: 0,
    contorta: 0,
    sinuosa: 0,
    stentata: 0,
    tentennante: 0,
    ponderata: 0,
    calma: 0,
    filiforme: 0,
    fine: 0,
    grossa: 0,
    grossolana: 0,
    ricciNascondimento: 0,
    ricciAmmanieramento: 0,
    ricciMitomania: 0,
    vezzosa: 0,
    accurata: 0,
    minuta: 0,
    minuziosa: 0,
    pedante: 0,
    uguale: 0,
    parca: 0,
    staccata: 0,
    levigata: 0,
    angoliC: 0,
    largaDiLettere: 0,
    largaTraParole: 0,
    disugualeMetodicamente: 0,
  });
}
