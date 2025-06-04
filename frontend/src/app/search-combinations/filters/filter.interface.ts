enum grado {
  assente,
  basso,
  sotto_media,
  medio,
  sopra_media,
  alto,
}

export interface Filter {
  largaTraLettere: grado;
  // attesa
  curva: grado;
  discendente: grado;
  titubante: grado;
  aperturaACapoDelleOeA: grado;
  asteColConcavoADestra: grado;
  pendente: grado;
  profusa: grado;
  fluida: grado;
  attaccata: grado;
  // resistenza
  angoliB: grado;
  mantieneIlRigo: grado;
  secca: grado;
  asteRette: grado;
  dritta: grado;
  recisa: grado;
  austera: grado;
  // assalto
  angoliA: grado;
  intozzata1Modo: grado;
  ascendente: grado;
  scattante: grado;
  asteColConcavoASinistra: grado;
  ardita: grado;
  slanciata: grado;
  impaziente: grado;
  spavalda: grado;
  acuta: grado;
  veloce: grado;
  solenne: grado;
  // Attesa
  intozzata2Modo: grado;
  contorta: grado;
  sinuosa: grado;
  stentata: grado;
  tentennante: grado;
  ponderata: grado;
  calma: grado;
  filiforme: grado;
  fine: grado;
  grossa: grado;
  grossolana: grado;
  ricciNascondimento: grado;
  ricciAmmanieramento: grado;
  ricciMitomania: grado;
  vezzosa: grado;
  accurata: grado;
  minuta: grado;
  minuziosa: grado;
  pedante: grado;
  uguale: grado;
  parca: grado;
  staccata: grado;
  levigata: grado;
  angoliC: grado;
  largaDiLettere: grado;
  largaTraParole: grado;
  disugualeMetodicamente: grado;
}
