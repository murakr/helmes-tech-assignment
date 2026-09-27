import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ProfileForm } from './profile-form/profile-form';

@Component({
  selector: 'app-root',
  imports: [ProfileForm],
  template: `
    <main class="page">
      <app-profile-form />
    </main>
  `,
  styleUrl: './app.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class App {}
