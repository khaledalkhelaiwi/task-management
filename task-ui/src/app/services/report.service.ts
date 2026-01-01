import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = 'http://localhost:8080/api/reports/tasks';

  constructor(private http: HttpClient) {}

  downloadMyTasksPdf(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/my`, { responseType: 'blob' });
  }

  downloadAllTasksPdf(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/all`, { responseType: 'blob' });
  }
}
