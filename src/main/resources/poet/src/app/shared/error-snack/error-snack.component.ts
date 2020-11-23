import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from "@angular/material/snack-bar";
import { PortalCurateComponent } from "../../portal/portal-curate/portal-curate.component";

@Component({
  selector: 'app-error-snack',
  templateUrl: './error-snack.component.html',
  styleUrls: ['./error-snack.component.scss']
})
export class ErrorSnackComponent implements OnInit {

  message: string = "[ERROR] Someone spilled the ink. We brought you back here for saftey!";
  constructor(private _snackBar: MatSnackBar) {}

  openSnackBar(message: string) {
    this.message = message;
    this._snackBar.openFromComponent(PortalCurateComponent, {
      duration: 3 * 1000,
    });
  }

  ngOnInit(): void {
  }

}
