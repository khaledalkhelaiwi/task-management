import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';  /// المسؤل عن قت ست دليت بوت 

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private tokenKey = 'token';      // token : lkjlkhlkhlhlhlhgyf86r8f8fljv,
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    return this.http.post<{ token: string }>(   //  هنا تم استعمال بوست عن طريق اتش تي تي بي .بوست من ات تي تي بي كلاينت
      `${this.apiUrl}/login`,    /// هنا تم  استخدام  تمبلت لتر لا الخذ الي ار ل و اكماله 
      { username, password }
    );
  }

  saveToken(token: string) {
    localStorage.setItem(this.tokenKey, token);   //  in this line that shoing new things the localStrage is to save the valus in localStoreg   setItm is have to val the first is  key and the saecond is the val   here wy have token from API II
  }

  getToken(): string {
    return localStorage.getItem(this.tokenKey) || '';
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
  }
}


