import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'sortBy'
})
export class SortPipe implements PipeTransform {

  transform(items: any[], sortBy: string, category: string): unknown {
    if(sortBy == 'recent'){
      // sort by most recent date
      return items.sort((a,b) => {
        a = new Date(a.lastUpdatedDate);
        b = new Date(b.lastUpdatedDate);
        if(a > b){
          return -1;
        } else if (a < b){
          return 1;
        } else {
          return 0;
        }
      });
    } else if(sortBy == 'name'){
      // sort by name
     return items.sort((a,b) => {
       if(category == 'phenotype'){
         a = a.hpoName.toUpperCase();
         b = b.hpoName.toUpperCase();
       } else if(category == 'treatment'){
         a = a.maxoName.toUpperCase();
         b = b.maxoName.toUpperCase();
       }

       if(a < b){
         return -1;
       } else if (a > b){
         return 1;
       } else {
         return 0;
       }
     });
    } else {
      return items;
    }
  }
}
