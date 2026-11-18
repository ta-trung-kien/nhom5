@echo off
chcp 65001 >nul
cd /d "%~dp0"

where java >nul 2>&1
if errorlevel 1 (
    echo.
    echo Chua tim thay Java. Hay cai JDK 17 va mo lai cua so lenh.
    echo Cach cai nam trong file README.md, muc "Buoc 2. Cai Java".
    echo.
    pause
    exit /b 1
)

where mvn >nul 2>&1
if errorlevel 1 (
    echo.
    echo Chua tim thay Maven. Hay cai Maven va mo lai cua so lenh.
    echo Cach cai nam trong file README.md, muc "Buoc 3. Cai Maven".
    echo.
    pause
    exit /b 1
)

echo Dang chuan bi chuong trinh. Lan dau tien can mang va co the mat vai phut.
echo.
mvn compile exec:java
if errorlevel 1 (
    echo.
    echo Chuong trinh khong chay duoc. Doc muc "Gap loi thi lam gi" trong README.md.
    echo.
    pause
)
