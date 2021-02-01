import { Component, OnInit } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";
import { UserService } from "../../shared/services/user/user.service";

@Component({
  selector: 'app-portal',
  templateUrl: './portal.component.html',
  styleUrls: ['./portal.component.scss']
})
export class PortalComponent implements OnInit {

  constructor(public auth: AuthService, private userService: UserService) {
  }

  ngOnInit(): void {

    this.auth.user$.subscribe((user) => {
      // User check With Our API to ensure they are in the database.
      if (user) {
        this.userService.checkUser();
      }
    });
  }
}
