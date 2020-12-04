import { Component, OnInit } from '@angular/core';
import { AuthService } from "@auth0/auth0-angular";

@Component({
  selector: 'app-portal',
  templateUrl: './portal.component.html',
  styleUrls: ['./portal.component.scss']
})
export class PortalComponent implements OnInit {

  constructor(public auth: AuthService) { }

  ngOnInit(): void {

    this.auth.user$.subscribe((user) => {
      console.log(user);
    });
  }
}
