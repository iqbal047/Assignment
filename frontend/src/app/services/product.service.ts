import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConfigService } from './config/config.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient, private config: ConfigService) { }

  uploadFile(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(`${this.config.apiUrl}/product/upload-csv`, formData);
  }

  downloadCSV(): Observable<Blob> {
    return this.http.get(`${this.config.apiUrl}/product/download-csv`, { responseType: 'blob' });
  }
}
