import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginResponse } from '../../dto/login-response.dto';
import { User } from '../../model/auth/user';
import { ConfigService } from '../config/config.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private config: ConfigService,
    private router: Router
  ) {}

  login(user: User): Observable<LoginResponse> {
    let url = `${this.config.apiUrl}/login`;
    return this.http.post<LoginResponse>(url, user);
  }

  checkLogin(): boolean {
    const token = sessionStorage.getItem('rs-token');
    return token !== null && token?.length > 8;
  }

  logout() {
    let url = `${this.config.apiUrl}/logout/${sessionStorage.getItem(
      'username'
    )}`;
    this.http.get<any>(url).subscribe(
      (resp) => {
        sessionStorage.clear();
        this.router.navigate(['/login']);
      },
      (error) => {
        console.log('logout failed');
      }
    );
  }
}
