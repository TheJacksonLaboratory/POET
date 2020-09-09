import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DialogCurationComponent} from "./dialog-curation/dialog-curation.component";

@Component({
  selector: 'app-portal-curate',
  templateUrl: './portal-curate.component.html',
  styleUrls: ['./portal-curate.component.scss']
})
export class PortalCurateComponent implements OnInit {


  constructor(private route: ActivatedRoute, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    // Open modal dialog to figure out what is the goal
    const dialogRef = this.dialog.open(DialogCurationComponent, {
      width: '500px',
      height: '500px'
    });
    //
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
    });
  }

}
