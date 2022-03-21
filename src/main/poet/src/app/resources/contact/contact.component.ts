import { Component } from '@angular/core';

@Component({
  selector: 'poet-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent {

  generalContacts = [
    {name: 'Peter Robinson', affiliation: 'The Jackson Laboratory for Genomic Medicine',
      email: 'peter.robinson@jax.org', src: '/assets/contact-images/peter-robinson.jpeg'},
    {name: 'Sebastian Köhler', affiliation: 'Monarch Initiative & Information Architect at Ada Health',
      email: 'dr.sebastian.koehler@gmail.com', src: '/assets/contact-images/sebastian-kohler.jpeg'},
    {name: 'Michael Gargano', affiliation: 'The Jackson Laboratory for Genomic Medicine',
      email: 'michael.gargano@jax.org', src: '/assets/contact-images/michael-gargano.jpeg'},
    {name: 'Melissa Haendel', affiliation: 'University of Colorado Center for Health AI',
      email: 'melissa@tislab.org', src: '/assets/contact-images/melissa-haendel.jpeg'}
  ];

  constructor() { }

}
