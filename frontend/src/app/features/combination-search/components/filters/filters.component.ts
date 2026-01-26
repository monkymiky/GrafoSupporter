import {
  Component,
  computed,
  effect,
  HostListener,
  inject,
  Signal,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  FormControl,
  FormArray,
} from '@angular/forms';
import { Grado } from '../../model/filter.interface';
import { SharedStateService } from '../../../../shared/services/shared-state.service';
import { NgbTooltip, NgbTooltipConfig } from '@ng-bootstrap/ng-bootstrap';
import { AuthorsService } from '../../services/authors.service';
import { Author } from '../../model/author.interface';
import {
  Subject,
  debounceTime,
  distinctUntilChanged,
  switchMap,
  catchError,
  of,
} from 'rxjs';

interface SignFilterFormRow {
  id: number;
  signName: string;
  temperamento: string | null;
  type: string;
  radioOptions: { value: number }[];
}

@Component({
  selector: 'app-filters',
  imports: [ReactiveFormsModule, CommonModule, NgbTooltip],
  providers: [NgbTooltipConfig],
  templateUrl: './filters.component.html',
  styleUrl: './filters.component.scss',
})
export class FiltersComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly sharedState = inject(SharedStateService);
  private readonly authorsService = inject(AuthorsService);
  signTypes = this.sharedState.signTypes;

  signsForm!: FormGroup;
  authorSearchControl = new FormControl<string>('');

  readonly errorMessage = signal<string | null>(null);
  readonly authorSuggestions = signal<Author[]>([]);
  readonly isSearchingAuthors = signal<boolean>(false);
  readonly showAuthorDropdown = signal<boolean>(false);
  readonly selectedAuthors = computed(() => this.sharedState.selectedAuthors());

  private authorSearchSubject = new Subject<string>();

  signsData: Signal<SignFilterFormRow[]> = computed(() => {
    const result: SignFilterFormRow[] = [];
    for (const singleSign of this.sharedState.signs()) {
      const id = singleSign.id;
      const signName = singleSign.name;
      const temperamento = singleSign.temperamento;
      const type = singleSign.tipo;
      result.push({
        id,
        signName,
        temperamento,
        type,
        radioOptions: [
          { value: Grado.ASSENTE },
          { value: Grado.BASSO },
          { value: Grado.SOTTO_MEDIA },
          { value: Grado.MEDIO },
          { value: Grado.SOPRA_MEDIA },
          { value: Grado.ALTO },
        ],
      });
    }
    return result;
  });

  constructor(config: NgbTooltipConfig) {
    config.container = 'body';
    config.tooltipClass = 'tooltipW';
    effect(() => {
      const currentSignsData = this.signsData();
      this.signsForm = this.formBuilder.group({
        rowSelections: this.formBuilder.array(
          currentSignsData.map(() =>
            this.formBuilder.control<Grado | null>(null)
          )
        ),
      });
    });

    this.authorSearchSubject
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((prefix) => {
          if (!prefix || prefix.trim().length < 2) {
            this.isSearchingAuthors.set(false);
            this.authorSuggestions.set([]);
            return of([]);
          }
          this.isSearchingAuthors.set(true);
          return this.authorsService.searchAuthors(prefix).pipe(
            catchError((err) => {
              console.error('Error searching authors:', err);
              this.isSearchingAuthors.set(false);
              return of([]);
            })
          );
        })
      )
      .subscribe((authors) => {
        this.isSearchingAuthors.set(false);
        const filtered = this.filterAlreadySelectedAuthors(authors);
        this.authorSuggestions.set(filtered);
        this.showAuthorDropdown.set(filtered.length > 0 || this.isSearchingAuthors());
      });
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (
      !target.closest('.author-search-container') &&
      !target.closest('.author-dropdown')
    ) {
      this.showAuthorDropdown.set(false);
    }
  }

  get rowSelectionsFormArray(): FormArray {
    return this.signsForm.get('rowSelections') as FormArray;
  }

  onRadioClick(rowIndex: number, radioValue: number): void {
    const formArray = this.rowSelectionsFormArray;
    const currentControl = formArray.at(rowIndex) as FormControl;
    const currentValue = currentControl.value;

    if (currentValue === radioValue) {
      currentControl.setValue(null);
    } else {
      currentControl.setValue(radioValue);
    }
  }
  filteredSignsByType(type: string) {
    return this.signsData()
      .map((data, i) => ({ ...data, index: i }))
      .filter((d) => d.type === type);
  }

  onAuthorInputChange(value: string): void {
    this.authorSearchSubject.next(value || '');
    if (value && value.trim().length >= 2) {
      this.showAuthorDropdown.set(true);
    }
  }

  onAuthorInputFocus(): void {
    if (this.authorSuggestions().length > 0) {
      this.showAuthorDropdown.set(true);
    }
  }

  selectAuthor(author: Author): void {
    const current = this.sharedState.selectedAuthors();
    if (!current.some((a) => a.id === author.id)) {
      this.sharedState.selectedAuthors.set([...current, author]);
    }
    this.authorSearchControl.setValue('');
    this.authorSuggestions.set([]);
    this.showAuthorDropdown.set(false);
  }

  removeAuthor(author: Author): void {
    const current = this.sharedState.selectedAuthors();
    this.sharedState.selectedAuthors.set(
      current.filter((a) => a.id !== author.id)
    );
  }

  getAuthorDisplayName(author: Author): string {
    return author.name;
  }

  filterAlreadySelectedAuthors(authors: Author[]): Author[] {
    const selectedIds = new Set(this.selectedAuthors().map((a) => a.id));
    return authors.filter((author) => !selectedIds.has(author.id));
  }

  onAuthorImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.style.display = 'none';
  }

  onSubmit(): void {
    this.errorMessage.set('');
    if (this.signsForm.invalid) {
      this.signsForm.markAllAsTouched();
      return;
    }

    const selections: (Grado | null)[] =
      this.signsForm.getRawValue().rowSelections;
    const newFilterMap = new Map<number, Grado>();
    const currentSignsData = this.signsData();

    for (let i = 0; i < currentSignsData.length; i++) {
      const signId = currentSignsData[i].id;
      const selectedValue = selections[i];

      if (selectedValue !== null) {
        newFilterMap.set(signId, selectedValue);
      } else {
        newFilterMap.set(signId, Grado.ASSENTE);
      }
    }

    this.sharedState.filters.set(newFilterMap);
    this.sharedState.combinationsSearchTrigger.set(Date.now());
  }
}
