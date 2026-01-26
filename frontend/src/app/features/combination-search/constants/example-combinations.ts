import { Combination } from '../model/combination.interface';
import { classification } from '../model/sign.interface';

export const EXAMPLE_COMBINATIONS: Combination[] = [
  {
    id: 0,
    title: "Questa è una combinazione d'esempio",
    shortDescription: "Cliccami per visualizzare le altre informazioni",
    longDescription:
      "Le informazioni che puoi visualizzare in una combianzione sono:   - I segni della combianzione con il loro intervallo in cui la combinazione ha significato: possono avere un '+' affianco, ciò significa che sono opzionali e che quindi la combianzione ha senso anche senza che questi segni siano presenti o siano nel range specificato.  Il testo di ogni sengo può essere di 5 colori che indicano il temperamento:     (nero: dipende dal contesto)      (azzurro: Cessione)      (giallo: Resistenza)       (verde: Attesa)       (rosso: assalto)   Con i bottoni bidone (rosso) e  penna (giallo) è possibile andare a eliminare o modificare una combinazione se è stata inserita dall'utente. Apri il secondo esempio per vederli!  PS: l'intervallo di grado per un segno 0-0/10 indica che il segno deve essere necessariamente assente perche la combinazione abbia significato. ",
    originalTextCondition: '',
      author: {
      id: 1,
      name: 'Utente Fittizzio',
    },
    imagePath: 'scrittura.jpg',
    signs: [
      {
        signId: 1,
        name: 'Largo di Lettere',
        max: 10,
        min: 5,
        isOptional: false,
        classification: classification.Sostanziale,
        temperamento: null,
      },
      {
        signId: 2,
        name: 'Curva',
        max: 5,
        min: 5,
        isOptional: false,
        classification: classification.Modificante,
        temperamento: 'Cessione',
      },
      {
        signId: 11,
        name: 'Angoli B',
        max: 5,
        min: 1,
        isOptional: true,
        classification: classification.Accidentale,
        temperamento: 'Resistenza',
      },
      {
        signId: 20,
        name: 'Ascendente',
        max: 6,
        min: 4,
        isOptional: true,
        classification: classification.Accidentale,
        temperamento: 'Assalto',
      },
      {
        signId: 40,
        name: 'Grossolana',
        max: 8,
        min: 6,
        isOptional: true,
        classification: classification.Accidentale,
        temperamento: 'Attesa',
      }
    ],
    sourceBook: null,
  },
  {
    id: 2,
    title: "Questo è un altro esempio di combinazione",
    shortDescription: 'Cliccami per visualizzare le altre informazioni',
    longDescription:
      "Ora incomincia pure la ricerca delle combinazioni selezionando nella barra laterale il grado di tutti i segni che hai trovato durante l'analisi!  ATTENTO! se alcuni non li inserisci il sistema li considera come assenti e non ti mostrerà le combinazioni che li riguardano (a meno che non siano segni opzionali) siccome le combinazioni sono veramente tante e altrimenti sarebbe da visualizzarne sempre tantissime!",
    originalTextCondition: '',
    author: {
      id: 1,
      name: 'Utente Fittizzio',
    },
    imagePath: 'scrittura2.jpg',
    signs: [
      {
        signId: 1,
        name: 'Largo di Lettere',
        max: 10,
        min: 5,
        isOptional: false,
        classification: classification.Sostanziale,
        temperamento: null,
      },
      {
        signId: 2,
        name: 'Curva',
        max: 5,
        min: 5,
        isOptional: false,
        classification: classification.Modificante,
        temperamento: 'Cessione',
      },
    ],
    sourceBook: null,
  },
];
