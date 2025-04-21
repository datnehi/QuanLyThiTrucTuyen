export interface NguoiDung {
    id: string;               // char(7)
    hoten: string;            // nvarchar(100)
    gioitinh: boolean;        // bit
    ngaysinh: Date;           // date
    avatar?: string;          // varchar(255) - optional
    sodienthoai: string;      // char(10)
    email: string;            // varchar(100)
    matkhau: string;          // varchar(50)
    role: string;             // varchar(50)
    trangthai?: boolean;      // bit - default 0 (optional với giá trị mặc định)
  }