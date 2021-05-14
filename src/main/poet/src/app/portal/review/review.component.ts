import { Component, OnInit } from '@angular/core';
import { environment } from "../../../environments/environment";
import { AuthService } from "@auth0/auth0-angular";
import {CurationService} from "../../shared/services/curation/curation.service";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {
  user: any;
  userRole: string;
  phenotypeDataSource = new MatTableDataSource();
  treatmentDataSource = new MatTableDataSource();
  displayedPhenotypeColumns = ['owner', 'phenotypeId', 'phenotypeName', 'sex', 'frequency', 'qualifier'];
  displayedTreatmentColumns = ['position'];

  constructor(public authService: AuthService, public curationService: CurationService) { }

  ngOnInit(): void {
    this.authService.user$.subscribe((user) => {
      this.user = user;
      this.userRole = user[environment.AUDIENCE_ROLE];
      if(this.userRole == "ELEVATED_CURATOR"){
        this.curationService.getTreatmentAnnotations(null, null, null, true).subscribe(data => {
          this.treatmentDataSource.data = data;
        });
        this.curationService.getPhenotypeAnnotations(null, null, null, true).subscribe(data => {
          this.phenotypeDataSource.data = data;
        });
      }
    });
  }

  hasReviews(){
    return this.treatmentDataSource.data.length > 0 || this.phenotypeDataSource.data.length > 0;
  }

}
