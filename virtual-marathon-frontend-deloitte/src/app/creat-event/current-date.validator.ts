import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class CurrentDateValidator {
  static LessThanToday(
    control: AbstractControl
  ): { [key: string]: any } | null {
    let today: Date = new Date();
    // console.log('today', today);
    // console.log('form date ', new Date(control.value));
    if (compareDates(new Date(control.value), today)) {
      return { LessThanToday: true };
    }
    // console.log('no error');
    return null;
  }
}

function compareDates(date1: Date, date2: Date) {
  if (date1.getFullYear() < date2.getFullYear()) return true;
  else if (date1.getFullYear() === date2.getFullYear()) {
    if (date1.getMonth() < date2.getMonth()) return true;
    else if (date1.getMonth() === date2.getMonth()) {
      if (date1.getDate() < date2.getDate()) {
        return true;
      } else return false;
    } else return false;
  } else {
    return false;
  }
}
