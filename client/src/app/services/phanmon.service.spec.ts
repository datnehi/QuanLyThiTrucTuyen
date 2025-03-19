import { TestBed } from '@angular/core/testing';

import { PhanmonService } from './phanmon.service';

describe('PhanmonService', () => {
  let service: PhanmonService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PhanmonService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
