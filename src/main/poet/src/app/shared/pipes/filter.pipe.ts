import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: any[], statuses: any, user: any, showAll: boolean): any[] {
    if(statuses.length == 0){
      return [];
    } else if(items?.length > 0){
        return items.filter(it => {
          return statuses.indexOf(it.status) != -1;
        }).filter( it => {
          if(it.status === "UNDER_REVIEW"){
            if(user.role == "CURATOR" && it.owner.nickname != user.nickname){
              return false;
            } else if(showAll) {
              return true;
            } else {
              return true;
            }
          } else {
            return true;
          }
        });
      }
  }
}
