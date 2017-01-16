import { Component, OnInit } from '@angular/core';

import { PlayInfo }        from './playInfo';
import {DBService} from './db.service';

@Component({
//  moduleId: module.id,
  selector: 'deadb',
  templateUrl: 'assets/app/db.component.html',
  styleUrls: [ 'assets/app/db.component.css' ],
})
export class DBComponent implements OnInit {
  public playInfo: PlayInfo = new PlayInfo();

  constructor(private dbService: DBService) { }

  public ngOnInit(): void {
    this.reload();
  }

  public reload(): void {
    this.dbService.getState()
      .then(pi => this.playInfo = pi);
  }

  public previous(): void {
    this.dbService.previous()
      .then(() => this.reload());
  }
  public toggle(): void {
    this.dbService.toggle()
      .then(() => this.reload());
  }
  public next(): void {
    this.dbService.next()
      .then(() => this.reload());
  }
  public random(): void {
    this.dbService.random()
      .then(() => this.reload());
  }
}

/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
