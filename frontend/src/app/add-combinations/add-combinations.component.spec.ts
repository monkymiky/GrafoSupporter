import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCombinationsComponent } from './add-combinations.component';

describe('AddCombinationsComponent', () => {
  let component: AddCombinationsComponent;
  let fixture: ComponentFixture<AddCombinationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCombinationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCombinationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
