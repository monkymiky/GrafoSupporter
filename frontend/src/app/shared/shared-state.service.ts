import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { Combination } from '../search-combinations/combination/combination.interface';
import { Sign_filter } from '../search-combinations/filters/sign-filter.interface';

@Injectable({
  providedIn: 'root',
})
export class SharedStateService {
  private combinations: WritableSignal<Combination[]> = signal([]);
  private filters: WritableSignal<Sign_filter> = signal({
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

  getcombinations(): WritableSignal<Combination[]> {
    return this.combinations;
  }

  getfilters(): WritableSignal<Sign_filter> {
    return this.filters;
  }

  setcombinations(newCombinations: Combination[]): void {
    this.combinations.set(newCombinations);
  }

  setFilters(newFilter: Sign_filter): void {
    this.filters.set(newFilter);
  }
}
