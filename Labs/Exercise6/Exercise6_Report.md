# Selenium WebDriver với JUnit 5
## Exercise 6 - Bao cao ket qua

**Nguyen Phan Huy**  
**DE180519**

## Muc luc
1. Phan tich yeu cau
2. Cau truc du an
3. Khoi tao project
4. Du lieu test
5. Ket qua chay test
6. Giai thich ket qua
7. Ket luan

## 1. Phan tich yeu cau
Exercise 6 yeu cau viet automation test cho 2 chuc nang:
- **Dang nhap** (`/Account/Login`)
- **Dang ky** (`/Account/Register`)

Yeu cau ky thuat:
- Su dung **Selenium WebDriver**
- Su dung **JUnit 5**
- To chuc theo **Page Object Model (POM)** + **BasePage pattern**
- Co test thuong va test du lieu hoa bang:
  - `@CsvSource`
  - `@CsvFileSource`

Pham vi test:
- **LoginTest**: 5 test methods (bao gom parameterized test)
- **RegisterTest**: 6 test methods (bao gom parameterized test)

## 2. Cau truc du an
```text
src
├── main
│   ├── java/huyde180519/exercise6/Exercise6Application.java
│   └── resources
│       ├── application.properties
│       └── LoginTest.java
└── test
    ├── java
    │   ├── huyde180519/exercise6/Exercise6ApplicationTests.java
    │   ├── pages
    │   │   ├── BasePage.java
    │   │   ├── LoginPage.java
    │   │   └── RegisterPage.java
    │   ├── tests
    │   │   ├── BaseTest.java
    │   │   ├── LoginTest.java
    │   │   └── RegisterTest.java
    │   └── utils/DriverFactory.java
    └── resources
        ├── login-data.csv
        └── register-data.csv
```

## 3. Khoi tao project
### Buoc 1: Tao project Maven va khai bao dependencies
File `pom.xml` su dung Java 21 va cac thu vien:
- `spring-boot-starter`
- `spring-boot-starter-test`
- `selenium-java`
- `webdrivermanager`
- `junit-jupiter-params`

```xml
<properties>
    <java.version>21</java.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.27.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.9.2</version>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Buoc 2: Tao `DriverFactory`
- Tu dong cai dat ChromeDriver bang WebDriverManager
- Cau hinh ChromeOptions:
  - `--incognito`
  - `--start-maximized`
  - `--ignore-certificate-errors`
  - `acceptInsecureCerts = true`

### Buoc 3: Tao `BasePage`
Class cha chua cac helper methods dung chung:
- `navigateTo()`
- `waitForVisibility()`
- `waitForClickability()`
- `click()`
- `type()`
- `getText()`
- `isElementVisible()`

### Buoc 4: Tao `LoginPage`
Quan ly thao tac tren trang login:
- Locators: username, password, login button, error message
- Actions:
  - `navigate()`
  - `login(username, password)`
- Verifications:
  - `isLoginSuccessful()`
  - `isOnLoginPage()`

### Buoc 5: Tao `RegisterPage`
Quan ly thao tac tren trang register:
- Locators: username, email, full name, password, confirm password, button
- Actions:
  - `navigate()`
  - `fillRegistrationForm(...)`
  - `submitRegistration()`
- Verifications:
  - `isRedirectedToLogin()`
  - `isOnRegisterPage()`

### Buoc 6: Tao `BaseTest`
- `@BeforeAll`: khoi tao driver
- `@AfterAll`: dong driver
- Base URL dang dung: `https://localhost:7009`

### Buoc 7: Tao `LoginTest`
Gom 5 test methods:
1. `testLoginSuccess`
2. `testLoginFail`
3. `testLoginEmptyCredentials`
4. `testLoginCsvInline` (`@CsvSource`, 4 bo du lieu)
5. `testLoginFromCSV` (`@CsvFileSource`, 4 bo du lieu)

### Buoc 8: Tao `RegisterTest`
Gom 6 test methods:
1. `testRegisterSuccess`
2. `testRegisterPasswordMismatch`
3. `testRegisterWeakPassword`
4. `testRegisterEmptyFields`
5. `testRegisterCsvInline` (`@CsvSource`, 6 bo du lieu)
6. `testRegisterFromCSV` (`@CsvFileSource`, 6 bo du lieu)

## 4. Du lieu test
### `login-data.csv`
```csv
username,password,expected
phanhuy,1234567,success
invalidUser,wrongPass,error
admin,12345,error
test@test.com,Password123!,error
```

### `register-data.csv`
```csv
username,email,fullName,password,confirmPassword,expected
newuser,newuser@example.com,New User,StrongP@ss1!,StrongP@ss1!,success
,test@email.com,Test User,Pass123!,Pass123!,error
testuser2,,Test User,Pass123!,Pass123!,error
testuser3,test3@email.com,Test User,123,123,error
testuser4,test4@email.com,Test User,StrongP@ss1!,Mismatch!,error
testuser5,invalidemail,Test User,StrongP@ss1!,StrongP@ss1!,error
```

## 5. Ket qua chay test
### Lenh thuc thi
```bash
mvn test
```

### Thoi diem chay gan nhat
- **23:11:06, 08/03/2026 (GMT+7)**

### Tong ket build
- **BUILD SUCCESS**
- **Tests run: 28, Failures: 0, Errors: 0, Skipped: 0**

### Chi tiet theo test set (tu surefire report)
| Test set | Tests run | Failures | Errors | Skipped | Time |
|---|---:|---:|---:|---:|---:|
| `huyde180519.exercise6.Exercise6ApplicationTests` | 1 | 0 | 0 | 0 | 1.985s |
| `tests.LoginTest` | 11 | 0 | 0 | 0 | 8.656s |
| `tests.RegisterTest` | 16 | 0 | 0 | 0 | 12.11s |

## 6. Giai thich ket qua
### 6.1 LoginTest
1. **testLoginSuccess (Order 1)**
- Muc dich: dang nhap voi tai khoan hop le (`phanhuy / 1234567`).
- Ket qua mong doi: redirect khoi `/Account/Login`.
- Ket qua thuc te: **PASS**.

2. **testLoginFail (Order 2)**
- Muc dich: dang nhap sai thong tin.
- Ket qua mong doi: hien thong bao loi, co chua "invalid".
- Ket qua thuc te: **PASS**.

3. **testLoginEmptyCredentials (Order 3)**
- Muc dich: de trong username va password.
- Ket qua mong doi: form khong submit, van o login page.
- Ket qua thuc te: **PASS**.

4. **testLoginCsvInline (Order 4) - @ParameterizedTest**
- Muc dich: test nhieu bo du lieu inline (`success` + `error` + rong).
- So lan chay: **4 sub-tests**.
- Ket qua thuc te: **PASS tat ca 4/4**.

5. **testLoginFromCSV (Order 5) - @ParameterizedTest**
- Muc dich: test du lieu tu `login-data.csv`.
- So lan chay: **4 sub-tests**.
- Ket qua thuc te: **PASS tat ca 4/4**.

### 6.2 RegisterTest
1. **testRegisterSuccess (Order 1)**
- Muc dich: dang ky voi du lieu hop le.
- Ket qua mong doi: redirect sang `/Account/Login`.
- Ket qua thuc te: **PASS**.

2. **testRegisterPasswordMismatch (Order 2)**
- Muc dich: password va confirm password khong khop.
- Ket qua mong doi: hien validation error.
- Ket qua thuc te: **PASS**.

3. **testRegisterWeakPassword (Order 3)**
- Muc dich: dang ky voi password yeu (`123`).
- Ket qua mong doi: hien validation error.
- Ket qua thuc te: **PASS**.

4. **testRegisterEmptyFields (Order 4)**
- Muc dich: bo trong cac truong bat buoc.
- Ket qua mong doi: khong submit, van o register page.
- Ket qua thuc te: **PASS**.

5. **testRegisterCsvInline (Order 5) - @ParameterizedTest**
- Muc dich: test 6 bo du lieu inline (hop le/khong hop le).
- So lan chay: **6 sub-tests**.
- Ket qua thuc te: **PASS tat ca 6/6**.

6. **testRegisterFromCSV (Order 6) - @ParameterizedTest**
- Muc dich: test 6 bo du lieu tu `register-data.csv`.
- So lan chay: **6 sub-tests**.
- Ket qua thuc te: **PASS tat ca 6/6**.

### 6.3 Exercise6ApplicationTests
- Muc dich: kiem tra Spring context load thanh cong.
- Ket qua: **PASS**.

## 7. Ket luan
- Exercise6 da ap dung dung POM + BasePage pattern.
- Bo test bao phu duoc cac luong co ban va invalid cho ca Login/Register.
- Parameterized test (`CsvSource`, `CsvFileSource`) giup tang do bao phu du lieu.
- Ket qua chay thuc te moi nhat: **28/28 test PASS, BUILD SUCCESS**.
