import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class DateValidator {
  static dateLessThan(
    startDate: string,
    endDate: string,
    errorName: string = 'startDateLessThanEnd'
  ): ValidatorFn {
    return (eventForm: AbstractControl): { [key: string]: boolean } | null => {
      const fromDate = eventForm?.get(startDate)?.value;
      //   console.log(fromDate);
      const toDate = eventForm?.get(endDate)?.value;

      if (fromDate !== null && toDate !== null && fromDate > toDate) {
        // console.log('error');
        eventForm.get(endDate)?.setErrors({ [errorName]: true });
        return { [errorName]: true };
      }
      //   console.log('no error');
      return null;
    };
  }
}
