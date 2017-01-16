import { Injectable }    from '@angular/core';
import { Headers, Http } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import {PlayInfo} from './playInfo';

@Injectable()
export class DBService {

  private headers = new Headers({'Content-Type': 'application/json'});
  private heroesUrl = 'api/db';

  constructor(private http: Http) { }

  public getState(): Promise<PlayInfo> {
    const url = `${this.heroesUrl}/state`;
    return this.http.get(url)
               .toPromise()
               .then(response => response.json().data as PlayInfo)
               .catch(this.handleError);
  }

  public previous(): Promise<void> {
    const url = `${this.heroesUrl}/previous`;
    return this.http.post(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);
  }

  public next(): Promise<void> {
    const url = `${this.heroesUrl}/next`;
    return this.http.post(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);
  }
  public toggle(): Promise<void> {
    const url = `${this.heroesUrl}/togglePlay`;
    return this.http.post(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);
  }
  public random(): Promise<void> {
    const url = `${this.heroesUrl}/random`;
    return this.http.post(url, {headers: this.headers})
      .toPromise()
      .then(() => null)
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
//    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}

/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
