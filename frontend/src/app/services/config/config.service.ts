import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class ConfigService {
    get apiUrl(): string {
        return environment.apiUrl;
    }

    get apiKey(): string {
        return environment.apiKey;
    }
}
