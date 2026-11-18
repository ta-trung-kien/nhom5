# Hệ thống quản lý đặt phòng học nhóm

Chương trình giúp sinh viên xem phòng học nhóm, xem giờ còn trống, đặt phòng, hủy lịch và xem phí. Giao diện là một cửa sổ trên máy tính, viết bằng Java.

File này dành cho người mới: máy chưa cài Java, chưa từng chạy dự án Java. Làm lần lượt từ trên xuống, đừng bỏ bước.

## Bạn cần gì

- Máy Windows 10 hoặc Windows 11
- Kết nối internet ở lần chạy đầu, vì chương trình phải tải thêm một thư viện
- Khoảng 15 phút cho lần cài đặt đầu

Ba phần mềm cần cài:

| Phần mềm | Việc của nó |
| --- | --- |
| Git | Tải dự án từ GitHub về máy |
| JDK 17 | Máy hiểu và chạy được code Java |
| Maven | Biên dịch code và mở chương trình |

## Bước 1. Cài Git

1. Mở trình duyệt, vào https://git-scm.com/download/win
2. Tải bản Windows 64-bit. File có dạng `Git-....-64-bit.exe`.
3. Mở file vừa tải. Cứ bấm **Next** đến hết, không cần đổi lựa chọn.
4. Cài xong, đóng hết cửa sổ lệnh cũ nếu đang mở.

Không muốn cài Git thì làm cách khác ở cuối bước 4.

## Bước 2. Cài Java

Cài bản **JDK 17**. Đừng cài bản JRE, vì bản đó chỉ chạy chương trình có sẵn, không biên dịch được code.

1. Vào https://adoptium.net/temurin/releases/?version=17
2. Chọn:
   - Operating System: **Windows**
   - Architecture: **x64**
   - Package Type: **JDK**
   - Version: **17**
3. Bấm file `.msi` để tải.
4. Mở file vừa tải.
5. Trong trình cài, bật hai ô này nếu thấy:
   - **Set JAVA_HOME variable**
   - **Add to PATH**
6. Bấm Next đến khi cài xong.

Kiểm tra:

1. Bấm phím Windows, gõ `cmd`, mở **Command Prompt**.
2. Gõ đúng dòng sau rồi Enter:

```bat
java -version
```

Thấy dòng có `openjdk version "17` là được. Ví dụ:

```text
openjdk version "17.0.16" 2025-07-15
```

Nếu báo `'java' is not recognized`:

1. Đóng cửa sổ `cmd`.
2. Mở lại một cửa sổ `cmd` mới rồi gõ lại. Cài xong phải mở cửa sổ mới thì máy mới nhận Java.
3. Vẫn lỗi thì cài lại JDK 17 và nhớ bật **Add to PATH**.

## Bước 3. Cài Maven

Maven là công cụ dựng và chạy project Java.

1. Vào https://maven.apache.org/download.cgi
2. Tải file **Binary zip archive**, tên dạng `apache-maven-3.9.9-bin.zip`. Không tải file `src.zip`.
3. Giải nén vào `C:\maven`. Bên trong phải có thư mục `bin`, tức là đường dẫn đúng kiểu:

```text
C:\maven\apache-maven-3.9.9\bin\mvn.cmd
```

4. Thêm Maven vào PATH:
   1. Bấm phím Windows, gõ `environment variables`, chọn **Edit the system environment variables**.
   2. Bấm **Environment Variables**.
   3. Ở nửa dưới, mục **System variables**, chọn biến **Path**, bấm **Edit**.
   4. Bấm **New**, dán đường dẫn thư mục `bin`. Ví dụ:

```text
C:\maven\apache-maven-3.9.9\bin
```

   5. Bấm OK cho đến khi đóng hết cửa sổ.

5. Đóng mọi cửa sổ `cmd` đang mở, mở một cửa sổ mới, gõ:

```bat
mvn -version
```

Thấy `Apache Maven 3.9` và `Java version: 17` là xong.

Nếu báo `'mvn' is not recognized`, đường dẫn `bin` vừa thêm bị sai, hoặc cửa sổ lệnh được mở trước khi thêm PATH. Mở cửa sổ mới rồi thử lại.

## Bước 4. Lấy dự án về máy

Mở Command Prompt và gõ lần lượt:

```bat
cd %USERPROFILE%\Desktop
git clone https://github.com/ta-trung-kien/nhom5.git
cd nhom5
```

Lệnh này tạo thư mục `nhom5` trên Desktop và tải code vào đó. Các lệnh chạy phía sau phải đứng trong thư mục này. Thư mục đúng là thư mục có file `pom.xml` và file `run.bat`.

Không cài Git thì làm vậy:

1. Mở https://github.com/ta-trung-kien/nhom5
2. Bấm nút xanh **Code**, rồi **Download ZIP**.
3. Giải nén. Vào thư mục bên trong cho đến khi thấy `pom.xml`.
4. Mở Command Prompt tại thư mục đó: vào thanh địa chỉ của thư mục, gõ `cmd`, nhấn Enter.

## Bước 5. Chạy chương trình

Trong thư mục `nhom5`, gõ:

```bat
run.bat
```

Hoặc:

```bat
mvn compile exec:java
```

Lần đầu Maven tải thư viện SQLite, có thể mất 1 đến 3 phút. Cứ để nó chạy, đừng tắt cửa sổ. Khi xong, một cửa sổ tên **Đặt phòng học nhóm** hiện ra.

Cửa sổ đen `cmd` phải để mở trong lúc dùng chương trình. Tắt cửa sổ đen là tắt luôn chương trình.

Muốn thoát: bấm nút đóng X trên cửa sổ đặt phòng.

Lần sau chỉ cần vào thư mục `nhom5` và chạy lại `run.bat`. Không phải cài Java hay Maven lại.

## Cách dùng

Trên cùng có ô **Sinh viên đang dùng**. Mọi lịch ở tab **Lịch của tôi** là của người đang được chọn ở ô này.

Dữ liệu mẫu lúc mới chạy:

| Mã | Tên | Lớp |
| --- | --- | --- |
| SV001 | Nguyễn Văn An | CNTT01 |
| SV002 | Trần Thị Bình | CNTT02 |
| SV003 | Lê Hoàng Cường | KTPM01 |

Phòng mẫu:

| Mã | Tên | Sức chứa | Loại | Trạng thái | Phí |
| --- | --- | --- | --- | --- | --- |
| P101 | Phòng tự học A | 6 | Phòng thường | Đang hoạt động | Miễn phí |
| P102 | Phòng tự học B | 4 | Phòng thường | Đang hoạt động | Miễn phí |
| P201 | Phòng máy chiếu 1 | 10 | Phòng có máy chiếu | Đang hoạt động | 20.000đ/giờ |
| P202 | Phòng máy chiếu 2 | 8 | Phòng có máy chiếu | Đang bảo trì | 20.000đ/giờ |
| P301 | Phòng họp seminar A | 20 | Phòng họp seminar | Đang hoạt động | 50.000đ/giờ |
| P302 | Phòng họp seminar B | 12 | Phòng họp seminar | Đang hoạt động | 50.000đ/giờ |

Giờ mở cửa mỗi ngày là 07:00 đến 21:00. Một sinh viên đặt tối đa 4 giờ trong một ngày.

### Tab Phòng học

Dùng để xem phòng và lịch trống.

1. Gõ mã hoặc tên vào ô **Mã hoặc tên**, ví dụ `P20`, rồi bấm **Tìm phòng**.
2. Hoặc chọn **Loại phòng** rồi bấm **Tìm phòng**.
3. Bấm **Xem tất cả** để hiện lại đủ phòng.
4. Bấm một dòng phòng trong bảng.
5. Sửa ô ngày nếu cần. Ngày viết dạng `2026-12-01`.
6. Bấm **Xem lịch trống**. Khung bên dưới hiện giờ đã có người đặt và giờ còn trống.
7. Bấm **Đặt phòng này** để sang tab đặt phòng, mã phòng được điền sẵn.
8. Bấm **Đổi trạng thái** để chuyển phòng đang chọn sang bảo trì hoặc mở lại. Phòng bảo trì không đặt được.

### Tab Đặt phòng

1. **Mã sinh viên**: thường đã là người đang chọn ở trên. Có thể gõ mã khác, ví dụ `SV002`.
2. **Mã phòng**: ví dụ `P101`.
3. **Ngày**: dạng `yyyy-MM-dd`, ví dụ `2026-12-01`. Phải là ngày trong tương lai.
4. **Giờ bắt đầu** và **Giờ kết thúc**: dạng `HH:mm`, ví dụ `08:00` và `10:00`. Giờ kết thúc phải sau giờ bắt đầu.
5. **Số người tham gia**: số nguyên, không lớn hơn sức chứa phòng.
6. Dòng phí phía dưới đổi theo loại phòng và số giờ.
7. Bấm **Đặt phòng**.

Đặt được thì dòng trạng thái ở đáy cửa sổ hiện mã đặt, dạng `DP...`, và số tiền. Không đặt được thì có hộp thoại nói rõ lý do.

Ví dụ phí:

- P101 từ 08:00 đến 10:00: 0đ
- P201 từ 08:00 đến 10:00: 40.000đ
- P301 từ 08:00 đến 09:30: 75.000đ

### Tab Lịch của tôi

Hiện mọi lịch của sinh viên đang chọn, kể cả lịch đã hủy.

1. Bấm một dòng trong bảng. Mã đặt phòng được điền xuống ô bên dưới.
2. Ô **Mã sinh viên thực hiện** là người bấm hủy. Mặc định là sinh viên đang chọn.
3. Bấm **Hủy lịch**.

Chỉ người tạo lịch mới hủy được. Hủy xong, trạng thái thành **Đã hủy**. Bấm hủy lần nữa thì chương trình báo lịch đã hủy trước đó.

Muốn thử hủy nhầm của người khác: chọn một lịch của SV001, sửa ô mã sinh viên thành `SV002`, rồi bấm **Hủy lịch**. Chương trình sẽ từ chối.

### Tab Sinh viên

Thêm một sinh viên mới:

1. **Mã sinh viên**: 3 đến 15 chữ hoặc số, ví dụ `SV010`.
2. **Họ tên**.
3. **Số điện thoại**: 9 đến 11 số, bắt đầu bằng 0, ví dụ `0901234567`.
4. **Lớp**, ví dụ `KTPM02`.
5. **Email**, ví dụ `duc@student.edu.vn`.
6. Bấm **Thêm sinh viên**.

Người vừa thêm xuất hiện trong ô chọn ở trên và trong bảng bên phải. Mã đã có rồi thì không thêm lại được.

## Những gì chương trình không cho làm

- Đặt khi chưa có sinh viên hoặc chưa có phòng đó.
- Đặt phòng đang bảo trì.
- Giờ kết thúc sớm hơn hoặc bằng giờ bắt đầu.
- Đặt ngoài 07:00–21:00.
- Đặt ngày hoặc giờ đã qua.
- Đặt trùng giờ một phòng. Hai lịch chỉ chạm nhau ở mốc giờ, ví dụ 08:00–10:00 và 10:00–12:00, thì vẫn được.
- Số người nhiều hơn sức chứa.
- Một sinh viên đặt quá 4 giờ trong cùng một ngày. Đúng 4 giờ thì được.
- Hủy lịch của sinh viên khác.
- Hủy lịch đã hủy.

## Dữ liệu nằm ở đâu

Lần chạy đầu, chương trình tự tạo file `data\studyroom.db` và nạp sinh viên, phòng từ:

- `data\seed\students.csv`
- `data\seed\rooms.csv`

Sau mỗi lần thêm sinh viên, đặt, hủy hoặc đổi trạng thái phòng, chương trình ghi thêm:

- `data\export\students.csv`
- `data\export\rooms.csv`
- `data\export\bookings.csv`
- `data\booking-log.txt`

Tắt chương trình rồi mở lại, lịch đã đặt vẫn còn.

Muốn xóa hết lịch và quay về 3 sinh viên, 6 phòng mẫu:

1. Tắt cửa sổ chương trình.
2. Xóa file `data\studyroom.db`.
3. Chạy lại `run.bat`.

Đừng xóa thư mục `data\seed`. Xóa thư mục đó thì lần tạo mới sẽ không có dữ liệu mẫu.

## Gặp lỗi thì làm gì

**`'java' is not recognized` hoặc `'mvn' is not recognized`**

Chưa cài, hoặc đã cài nhưng chưa mở lại cửa sổ lệnh. Làm lại bước 2 hoặc bước 3, rồi đóng `cmd` và mở `cmd` mới.

**`Hay chay lenh tai thu muc goc cua project`**

Bạn đang đứng nhầm thư mục. Dùng `cd` vào đúng thư mục có file `pom.xml`, rồi chạy lại.

**Lần đầu đứng lâu ở dòng `Downloading`**

Bình thường. Máy đang tải thư viện. Cần có internet. Đợi cho đến khi cửa sổ đặt phòng mở.

**Cửa sổ đặt phòng không thấy đâu**

Nhìn thanh taskbar ở dưới màn hình, hoặc màn hình thứ hai nếu máy có hai màn hình. Tên cửa sổ là **Đặt phòng học nhóm**.

**Sửa code xong chạy lại bị lỗi file đang được dùng**

Tắt cửa sổ đặt phòng trước, rồi mới chạy `run.bat` lại.

**Đặt phòng báo lỗi dù bạn thấy phòng còn trống**

Đọc đúng câu trong hộp thoại. Hay gặp nhất là ngày đã qua, giờ ngoài 07:00–21:00, hoặc sinh viên đó đã đủ 4 giờ trong ngày.

## Các thư mục trong project

```text
nhom5
  run.bat                 Bam de chay tren Windows
  pom.xml                 Khai bao Java va thu vien
  data\seed               Sinh vien va phong mau
  src\main\java\com\nhom5
    model                 Sinh vien, phong, lich dat
    policy                Cach tinh phi tung loai phong
    validator             Kiem tra dieu kien dat phong
    service               Dat, huy, tim phong, xem lich
    repository            Luu SQLite va doc ghi file CSV
    ui                    Cua so Java Swing
    main                  Cho nay mo chuong trinh
```
