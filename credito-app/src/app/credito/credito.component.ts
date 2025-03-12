import { Component } from '@angular/core';
import {Credito, CreditoService} from './credito.service';
import {CurrencyPipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-credito',
  imports: [
    CurrencyPipe,
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './credito.component.html',
  styleUrl: './credito.component.scss'
})
export class CreditoComponent {
  numero: string = '';
  tipoBusca: 'nfse' | 'credito' = 'nfse';
  resultados: Credito[] = [];
  erro: string = '';

  constructor(private creditoService: CreditoService) {}

  buscar() {
    this.resultados = [];
    this.erro = '';

    if (this.tipoBusca === 'nfse') {
      this.creditoService.buscarPorNfse(this.numero).subscribe(
        (data) => this.resultados = data,
        (error) => this.erro = 'Nenhum crédito encontrado.'
      );
    } else {
      this.creditoService.buscarPorCredito(this.numero).subscribe(
        (data: any) => {
          console.log(data);
          this.resultados = [data];
        },
        (error) => this.erro = 'Crédito não encontrado.'
      );
    }
  }
}
