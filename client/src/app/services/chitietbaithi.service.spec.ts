import { TestBed } from '@angular/core/testing';

import { ChitietbaithiService } from './chitietbaithi.service';

describe('ChitietbaithiService', () => {
  let service: ChitietbaithiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChitietbaithiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
