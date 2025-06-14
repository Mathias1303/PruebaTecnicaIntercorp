import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDto } from '../models/customer.dto';
import { PersonListResponseDto } from '../models/person-list-response.dto';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiUrl = 'http://localhost:8088/api/data';

  constructor(private http: HttpClient) { }


  public getPeopleList(): Observable<PersonListResponseDto> {
    return this.http.get<PersonListResponseDto>(`${this.apiUrl}/list`);
  }

  public searchPeople(name: string): Observable<CustomerDto[]> {
    return this.http.get<CustomerDto[]>(`${this.apiUrl}/search`, { params: { name } });
  }
}
