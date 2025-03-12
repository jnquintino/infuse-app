import { Routes } from '@angular/router';
import {CreditoComponent} from './credito/credito.component';

export const routes: Routes = [
  { path: 'consulta', component: CreditoComponent },
  { path: '', redirectTo: 'consulta', pathMatch: 'full' }
];
