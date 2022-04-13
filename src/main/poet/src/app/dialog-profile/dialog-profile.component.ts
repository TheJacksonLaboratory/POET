import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogReviewComponent} from '../portal/portal-curate/dialog-review/dialog-review.component';
import {UserService} from '../shared/services/user/user.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-dialog-profile',
  templateUrl: './dialog-profile.component.html',
  styleUrls: ['./dialog-profile.component.scss']
})
export class DialogProfileComponent implements OnInit {
  profileForm = new FormGroup({
    orcidFormControl: new FormControl({value: '', disabled: false}, [Validators.required,
      Validators.minLength(19), Validators.maxLength(19),
      Validators.pattern('\\d{4}-\\d{4}-\\d{4}-\\w{4,5}')])
  });

  user: any;
  constructor(public dialogRef: MatDialogRef<DialogReviewComponent>, public userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.userService.getUserDetails().subscribe((userDetails) => {
      this.user = userDetails;
      this.profileForm.markAllAsTouched();
      if (this.user.orcid !== null) {
        this.profileForm.get('orcidFormControl').setValue(this.user.orcid);
        this.profileForm.get('orcidFormControl').disable();
      }
    });
  }

  prettyRole(){
    return this.user.curationRole.replace('_', ' ');
  }
}
