import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MessageToastComponent } from './shared/components/message-toast/message-toast.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MessageToastComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
