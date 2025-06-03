import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchCombinationsPageComponent } from './search-combinations-page.component';

describe('SearchCombinationsPageComponent', () => {
  let component: SearchCombinationsPageComponent;
  let fixture: ComponentFixture<SearchCombinationsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchCombinationsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchCombinationsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
