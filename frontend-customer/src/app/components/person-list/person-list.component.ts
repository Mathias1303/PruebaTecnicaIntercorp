import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { PersonListResponseDto } from 'src/app/models/person-list-response.dto';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-person-list',
  templateUrl: './person-list.component.html',
  styleUrls: ['./person-list.component.scss']
})
export class PersonListComponent implements OnInit {

  public displayedColumns: string[] = ['firstname', 'lastname', 'email', 'country', 'city'];

  public searchControl = new FormControl('');
  public response: PersonListResponseDto | null = null;
  public isLoading = false;
  public error: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadAllPeople();
  }

  public loadAllPeople(): void {
    this.isLoading = true;
    this.error = null;
    this.apiService.getPeopleList().subscribe({
      next: (data) => {
        this.response = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Ocurrió un error al cargar la lista inicial.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  public onSearch(): void {
    const searchTerm = this.searchControl.value?.trim();

    this.isLoading = true;
    this.error = null;

    if (!searchTerm) {
      this.loadAllPeople();
      return;
    }

    this.apiService.searchPeople(searchTerm).subscribe({
      next: (people) => {
        this.response = {
          lastUpdated: this.response?.lastUpdated || new Date(),
          data: people,
        };
        this.isLoading = false;
      },
      error: (err) => {
        if (err.status === 404) {
          this.response = {
            lastUpdated: this.response?.lastUpdated || new Date(),
            data: [],
          };
        } else {
          this.error = 'Ocurrió un error al realizar la búsqueda.';
          console.error(err);
        }
        this.isLoading = false;
      }
    });
  }

  public clearSearch(): void {
    this.searchControl.reset('');
    this.loadAllPeople();
  }
}
