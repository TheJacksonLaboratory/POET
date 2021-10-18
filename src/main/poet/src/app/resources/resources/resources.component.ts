import { Component, OnInit } from '@angular/core';
import { Router} from "@angular/router";

@Component({
  selector: 'app-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.scss']
})
export class ResourcesComponent implements OnInit {

  selectedIndex: number = 2;
  constructor(private router: Router) { }

  ngOnInit(): void {
    if(this.router.url.includes("faq")){
      this.selectedIndex = 1;
    }
    // Will need to expand when we have documentation
  }

}
