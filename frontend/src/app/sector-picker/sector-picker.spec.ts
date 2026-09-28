import { Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Sector } from '../api/sector';
import { SectorPicker } from './sector-picker';

const SECTORS: Sector[] = [
  { id: 1, name: 'Manufacturing', level: 0 },
  { id: 6, name: 'Food and Beverage', level: 1 },
  { id: 342, name: 'Bakery & confectionery products', level: 2 },
];

@Component({
  imports: [ReactiveFormsModule, SectorPicker],
  template: `<app-sector-picker [formControl]="control" [sectors]="sectors" />`,
})
class HostComponent {
  readonly sectors = SECTORS;
  readonly control = new FormControl<number[]>([], { nonNullable: true });
}

describe('SectorPicker', () => {
  let fixture: ComponentFixture<HostComponent>;
  let control: FormControl<number[]>;
  let page: HTMLElement;

  const rowFor = (name: string) =>
    Array.from(page.querySelectorAll<HTMLLabelElement>('.sector')).find((row) =>
      row.textContent?.includes(name),
    )!;
  const checkboxFor = (name: string) => rowFor(name).querySelector('input')!;
  const clearAllButton = () => page.querySelector<HTMLButtonElement>('.picker-header button')!;

  const render = async () => {
    fixture.detectChanges();
    await fixture.whenStable();
  };

  beforeEach(async () => {
    fixture = TestBed.createComponent(HostComponent);
    control = fixture.componentInstance.control;
    page = fixture.nativeElement;
    await render();
  });

  it('renders every sector indented by its level', () => {
    expect(page.querySelectorAll('.sector')).toHaveLength(3);
    expect(rowFor('Food and Beverage').style.paddingLeft).toBe('1.5rem');
    expect(rowFor('Bakery & confectionery products').style.paddingLeft).toBe('3rem');
  });

  it('checks the sectors in the form value', async () => {
    control.setValue([6, 342]);
    await render();

    expect(checkboxFor('Manufacturing').checked).toBe(false);
    expect(checkboxFor('Food and Beverage').checked).toBe(true);
    expect(checkboxFor('Bakery & confectionery products').checked).toBe(true);
    expect(page.textContent).toContain('2 selected');
  });

  it('selects and deselects sectors when clicked', async () => {
    checkboxFor('Manufacturing').click();
    checkboxFor('Food and Beverage').click();
    await render();
    expect(control.value).toEqual([1, 6]);
    expect(control.touched).toBe(true);

    checkboxFor('Manufacturing').click();
    await render();
    expect(control.value).toEqual([6]);
  });

  it('clears the whole selection and keeps focus in the list', async () => {
    control.setValue([1, 6]);
    await render();

    clearAllButton().click();
    await render();

    expect(control.value).toEqual([]);
    expect(page.textContent).toContain('0 selected');
    expect(document.activeElement).toBe(checkboxFor('Manufacturing'));
  });

  it('disables clear all when nothing is selected', () => {
    expect(clearAllButton().disabled).toBe(true);
  });
});
