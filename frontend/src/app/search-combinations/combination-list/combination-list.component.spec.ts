import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CombinationListComponent } from './combination-list.component';

describe('CombinationListComponent', () => {
  let component: CombinationListComponent;
  let fixture: ComponentFixture<CombinationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CombinationListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CombinationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
