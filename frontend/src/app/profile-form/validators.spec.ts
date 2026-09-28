import { FormControl } from '@angular/forms';
import { notBlank } from './validators';

describe('notBlank', () => {
  const control = (value: string) => new FormControl(value, { nonNullable: true });

  it('accepts text', () => {
    expect(notBlank(control('testUser'))).toBeNull();
  });

  it('accepts text surrounded by whitespace', () => {
    expect(notBlank(control('  testUser  '))).toBeNull();
  });

  it.each(['', '   ', '\t\n'])('rejects %j', (value) => {
    expect(notBlank(control(value))).toEqual({ blank: true });
  });
});
