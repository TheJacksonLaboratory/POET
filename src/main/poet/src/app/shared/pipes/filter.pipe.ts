import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: any[], statuses: any): any[] {
    if(statuses.length == 0){
      return [];
    } else if(items?.length > 0){
      return items.filter(it => {
       return statuses.indexOf(it.status) != -1;
      });
    } else {
      return items;
    }
  }
}
