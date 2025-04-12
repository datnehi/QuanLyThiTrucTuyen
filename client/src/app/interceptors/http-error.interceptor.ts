import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastr = inject(ToastrService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'Đã xảy ra lỗi không xác định!';

      switch (error.status) {
        case 0:
          message = 'Không thể kết nối đến máy chủ! Vui lòng kiểm tra kết nối mạng.';
          break;
        case 400:
          message = 'Yêu cầu không hợp lệ!';
          break;
        case 401:
          message = 'Bạn chưa đăng nhập hoặc phiên đăng nhập đã hết hạn.';
          break;
        case 404:
          message = 'Không tìm thấy dữ liệu!';
          break;
        case 405:
          message = 'Phương thức không được hỗ trợ!';
          break;
        case 500:
          message = 'Lỗi server, vui lòng thử lại sau!';
          break;
      }

      if (error.error?.message) {
        message = error.error.message;
      }

      toastr.error(message);
      return throwError(() => error);
    })
  );
};
