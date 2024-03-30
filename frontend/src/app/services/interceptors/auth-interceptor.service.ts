import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Modify the request or do something before sending it
    // For example, add an authorization header

    const modifiedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${sessionStorage.getItem('rs-token')}`
      }
    });

    // Pass on the modified request
    return next.handle(modifiedReq);
  }
}
