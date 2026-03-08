$OutputPath = Join-Path (Get-Location) "System_Test_Exercise6.xlsx"

$testCasesData = @(
    [PSCustomObject]@{
        ID = 'TC_LOGIN_001'; Module = 'Login'; Objective = 'Verify login with valid credentials'
        Pre = 'Application is running at https://localhost:7009; Valid account exists (phanhuy/1234567).'
        Steps = "1. Open /Account/Login`n2. Enter valid username and password`n3. Click Login"
        Expected = 'User is redirected away from /Account/Login after successful authentication.'
        Actual = 'Redirected away from /Account/Login as expected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginSuccess'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_002'; Module = 'Login'; Objective = 'Verify login failure with invalid credentials'
        Pre = 'Application is running; Login page is accessible.'
        Steps = "1. Open /Account/Login`n2. Enter wrong username/password`n3. Click Login"
        Expected = 'Error message is displayed and login is rejected.'
        Actual = 'Invalid credential message displayed; login rejected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginFail'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_003'; Module = 'Login'; Objective = 'Verify validation when username and password are empty'
        Pre = 'Application is running; Login page is accessible.'
        Steps = "1. Open /Account/Login`n2. Leave username and password blank`n3. Click Login"
        Expected = 'Form submission is blocked by validation; user remains on login page.'
        Actual = 'Submission blocked; remained on login page.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginEmptyCredentials'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_004'; Module = 'Login'; Objective = 'CSV Inline case #1 - valid credentials (phanhuy/1234567)'
        Pre = 'Application is running; Valid account exists.'
        Steps = "1. Open /Account/Login`n2. Input dataset #1 from @CsvSource`n3. Submit"
        Expected = 'Login succeeds and user leaves login page.'
        Actual = 'Case passed; redirect behavior matched expectation.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginCsvInline [1]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_005'; Module = 'Login'; Objective = 'CSV Inline case #2 - wrong user / valid format password'
        Pre = 'Application is running; Login page is accessible.'
        Steps = "1. Open /Account/Login`n2. Input dataset #2 from @CsvSource`n3. Submit"
        Expected = 'Login fails and error is shown.'
        Actual = 'Case passed; error message displayed.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginCsvInline [2]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_006'; Module = 'Login'; Objective = 'CSV Inline case #3 - email username with wrong password'
        Pre = 'Application is running; Login page is accessible.'
        Steps = "1. Open /Account/Login`n2. Input dataset #3 from @CsvSource`n3. Submit"
        Expected = 'Login fails and error is shown.'
        Actual = 'Case passed; invalid login feedback displayed.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginCsvInline [3]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_007'; Module = 'Login'; Objective = 'CSV Inline case #4 - EMPTY username and password'
        Pre = 'Application is running; Login page is accessible.'
        Steps = "1. Open /Account/Login`n2. Input EMPTY values from @CsvSource`n3. Submit"
        Expected = 'HTML5 validation prevents submit; still on login page.'
        Actual = 'Case passed; submission prevented as expected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginCsvInline [4]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_008'; Module = 'Login'; Objective = 'CSV File case #1 - phanhuy/1234567 (success)'
        Pre = 'Application is running; login-data.csv is loaded; valid account exists.'
        Steps = "1. Open /Account/Login`n2. Read dataset #1 from login-data.csv`n3. Submit"
        Expected = 'Login succeeds and user leaves login page.'
        Actual = 'Case passed with successful redirect.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginFromCSV [1]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_009'; Module = 'Login'; Objective = 'CSV File case #2 - invalidUser/wrongPass (error)'
        Pre = 'Application is running; login-data.csv is loaded.'
        Steps = "1. Open /Account/Login`n2. Read dataset #2 from login-data.csv`n3. Submit"
        Expected = 'Login fails and error is shown.'
        Actual = 'Case passed; error shown for invalid credentials.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginFromCSV [2]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_010'; Module = 'Login'; Objective = 'CSV File case #3 - admin/12345 (error)'
        Pre = 'Application is running; login-data.csv is loaded.'
        Steps = "1. Open /Account/Login`n2. Read dataset #3 from login-data.csv`n3. Submit"
        Expected = 'Login fails and error is shown.'
        Actual = 'Case passed; invalid login handled correctly.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginFromCSV [3]'
    }
    [PSCustomObject]@{
        ID = 'TC_LOGIN_011'; Module = 'Login'; Objective = 'CSV File case #4 - test@test.com/Password123! (error)'
        Pre = 'Application is running; login-data.csv is loaded.'
        Steps = "1. Open /Account/Login`n2. Read dataset #4 from login-data.csv`n3. Submit"
        Expected = 'Login fails and error is shown.'
        Actual = 'Case passed; error behavior matched expectation.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: LoginTest.testLoginFromCSV [4]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_001'; Module = 'Register'; Objective = 'Verify successful registration with valid data'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Enter unique valid user data`n3. Click Sign Up"
        Expected = 'Registration succeeds and user is redirected to /Account/Login.'
        Actual = 'Redirected to /Account/Login after submission.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterSuccess'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_002'; Module = 'Register'; Objective = 'Verify validation when password and confirm password mismatch'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Enter mismatched passwords`n3. Click Sign Up"
        Expected = 'Validation error is displayed; registration is not completed.'
        Actual = 'Validation error displayed as expected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterPasswordMismatch'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_003'; Module = 'Register'; Objective = 'Verify validation for weak password'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Enter weak password (123)`n3. Click Sign Up"
        Expected = 'Validation error is displayed for weak password.'
        Actual = 'Weak password validation triggered correctly.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterWeakPassword'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_004'; Module = 'Register'; Objective = 'Verify validation when required fields are empty'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Leave required fields empty`n3. Click Sign Up"
        Expected = 'Submission is blocked and user remains on register page.'
        Actual = 'Submission blocked; remained on register page.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterEmptyFields'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_005'; Module = 'Register'; Objective = 'CSV Inline case #1 - valid registration data (success)'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Input dataset #1 from @CsvSource`n3. Submit"
        Expected = 'Registration succeeds and redirects to login page.'
        Actual = 'Case passed with successful redirect.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterCsvInline [1]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_006'; Module = 'Register'; Objective = 'CSV Inline case #2 - EMPTY username (error)'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Input dataset #2 from @CsvSource`n3. Submit"
        Expected = 'Submission is blocked due to missing username.'
        Actual = 'Case passed; missing username handled correctly.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterCsvInline [2]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_007'; Module = 'Register'; Objective = 'CSV Inline case #3 - EMPTY email (error)'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Input dataset #3 from @CsvSource`n3. Submit"
        Expected = 'Submission is blocked due to missing email.'
        Actual = 'Case passed; missing email handled correctly.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterCsvInline [3]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_008'; Module = 'Register'; Objective = 'CSV Inline case #4 - short password (error)'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Input dataset #4 from @CsvSource`n3. Submit"
        Expected = 'Validation error is shown for weak password.'
        Actual = 'Case passed; weak password validation displayed.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterCsvInline [4]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_009'; Module = 'Register'; Objective = 'CSV Inline case #5 - password mismatch (error)'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Input dataset #5 from @CsvSource`n3. Submit"
        Expected = 'Validation error is shown for password mismatch.'
        Actual = 'Case passed; mismatch validation shown.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterCsvInline [5]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_010'; Module = 'Register'; Objective = 'CSV Inline case #6 - invalid email format (error)'
        Pre = 'Application is running; Register page is accessible.'
        Steps = "1. Open /Account/Register`n2. Input dataset #6 from @CsvSource`n3. Submit"
        Expected = 'Validation error is shown for invalid email format.'
        Actual = 'Case passed; invalid email rejected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterCsvInline [6]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_011'; Module = 'Register'; Objective = 'CSV File case #1 - newuser/newuser@example.com (success)'
        Pre = 'Application is running; register-data.csv is loaded.'
        Steps = "1. Open /Account/Register`n2. Read dataset #1 from register-data.csv`n3. Submit"
        Expected = 'Registration succeeds and redirects to login page.'
        Actual = 'Case passed with successful redirect.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterFromCSV [1]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_012'; Module = 'Register'; Objective = 'CSV File case #2 - empty username (error)'
        Pre = 'Application is running; register-data.csv is loaded.'
        Steps = "1. Open /Account/Register`n2. Read dataset #2 from register-data.csv`n3. Submit"
        Expected = 'Submission is blocked due to missing username.'
        Actual = 'Case passed; missing username prevented submission.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterFromCSV [2]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_013'; Module = 'Register'; Objective = 'CSV File case #3 - empty email (error)'
        Pre = 'Application is running; register-data.csv is loaded.'
        Steps = "1. Open /Account/Register`n2. Read dataset #3 from register-data.csv`n3. Submit"
        Expected = 'Submission is blocked due to missing email.'
        Actual = 'Case passed; missing email prevented submission.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterFromCSV [3]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_014'; Module = 'Register'; Objective = 'CSV File case #4 - weak password (error)'
        Pre = 'Application is running; register-data.csv is loaded.'
        Steps = "1. Open /Account/Register`n2. Read dataset #4 from register-data.csv`n3. Submit"
        Expected = 'Validation error is shown for weak password.'
        Actual = 'Case passed; weak password rejected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterFromCSV [4]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_015'; Module = 'Register'; Objective = 'CSV File case #5 - password mismatch (error)'
        Pre = 'Application is running; register-data.csv is loaded.'
        Steps = "1. Open /Account/Register`n2. Read dataset #5 from register-data.csv`n3. Submit"
        Expected = 'Validation error is shown for password mismatch.'
        Actual = 'Case passed; mismatch validation displayed.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterFromCSV [5]'
    }
    [PSCustomObject]@{
        ID = 'TC_REG_016'; Module = 'Register'; Objective = 'CSV File case #6 - invalid email format (error)'
        Pre = 'Application is running; register-data.csv is loaded.'
        Steps = "1. Open /Account/Register`n2. Read dataset #6 from register-data.csv`n3. Submit"
        Expected = 'Validation error is shown for invalid email format.'
        Actual = 'Case passed; invalid email rejected.'
        Status = 'Pass'; Bug = ''; Notes = 'Automated: RegisterTest.testRegisterFromCSV [6]'
    }
)

$excel = $null
$workbook = $null
$overview = $null
$testCases = $null

try {
    $excel = New-Object -ComObject Excel.Application
    $excel.Visible = $false
    $excel.DisplayAlerts = $false

    $workbook = $excel.Workbooks.Add()

    while ($workbook.Worksheets.Count -lt 2) {
        [void]$workbook.Worksheets.Add()
    }

    while ($workbook.Worksheets.Count -gt 2) {
        $workbook.Worksheets.Item($workbook.Worksheets.Count).Delete()
    }

    $overview = $workbook.Worksheets.Item(1)
    $overview.Name = 'Overview'
    $testCases = $workbook.Worksheets.Item(2)
    $testCases.Name = 'Test Cases'

    # Overview sheet
    $overview.Range('A1:D1').Merge() | Out-Null
    $overview.Range('A1').Value2 = 'System Test Report - Exercise 6 (Login and Register)'
    $overview.Range('A1').Font.Bold = $true
    $overview.Range('A1').Font.Size = 14

    $overview.Range('A3').Value2 = 'Project / Module'
    $overview.Range('B3').Value2 = 'MiniSocialNetwork - Account (Login, Register)'
    $overview.Range('A4').Value2 = 'Test Environment'
    $overview.Range('B4').Value2 = 'Staging (Localhost), URL: https://localhost:7009, Browser: Chrome 142, OS: Windows 11'
    $overview.Range('A5').Value2 = 'Tester'
    $overview.Range('B5').Value2 = 'Nguyen Phan Huy (DE180519)'
    $overview.Range('A6').Value2 = 'Test Execution Start'
    $overview.Range('B6').Value2 = '08-Mar-2026 23:10 (GMT+7)'
    $overview.Range('A7').Value2 = 'Test Execution End'
    $overview.Range('B7').Value2 = '08-Mar-2026 23:11 (GMT+7)'
    $overview.Range('A8').Value2 = 'Automation Framework'
    $overview.Range('B8').Value2 = 'Selenium WebDriver + JUnit 5 + Maven + WebDriverManager'

    $overview.Range('A10').Value2 = 'Summary'
    $overview.Range('A10').Font.Bold = $true
    $overview.Range('A11').Value2 = 'Total Test Cases'
    $overview.Range('B11').Formula = "=COUNTA('Test Cases'!A:A)-1"
    $overview.Range('A12').Value2 = 'Pass'
    $overview.Range('B12').Formula = '=COUNTIF(''Test Cases''!H:H,"Pass")'
    $overview.Range('A13').Value2 = 'Fail'
    $overview.Range('B13').Formula = '=COUNTIF(''Test Cases''!H:H,"Fail")'
    $overview.Range('A14').Value2 = 'Blocked'
    $overview.Range('B14').Formula = '=COUNTIF(''Test Cases''!H:H,"Blocked")'
    $overview.Range('A15').Value2 = 'Untested'
    $overview.Range('B15').Formula = '=COUNTIF(''Test Cases''!H:H,"N/A")'

    $overview.Range('A17').Value2 = 'Build Result'
    $overview.Range('B17').Value2 = 'BUILD SUCCESS'
    $overview.Range('A18').Value2 = 'Execution Evidence'
    $overview.Range('B18').Value2 = 'Tests run: 28, Failures: 0, Errors: 0, Skipped: 0'
    $overview.Range('A19').Value2 = 'Notes'
    $overview.Range('B19').Value2 = '27 system test cases for Login and Register passed.'

    $overview.Range('A3:A19').Font.Bold = $true
    $overview.Columns('A').ColumnWidth = 24
    $overview.Columns('B').ColumnWidth = 95
    $overview.Columns('C:D').ColumnWidth = 2
    $overview.Range('B3:B19').WrapText = $true
    $overview.Range('A3:B19').VerticalAlignment = -4160
    $overview.Range('A10:B15').Borders.LineStyle = 1

    # Test Cases sheet
    $headers = @(
        'Test Case ID',
        'Module / Feature',
        'Objective / Description',
        'Pre-conditions',
        'Test Steps',
        'Expected Result',
        'Actual Result',
        'Status',
        'Bug ID / Jira Link',
        'Notes'
    )

    for ($col = 1; $col -le $headers.Count; $col++) {
        $testCases.Cells.Item(1, $col).Value2 = $headers[$col - 1]
    }

    $row = 2
    foreach ($tc in $testCasesData) {
        $testCases.Cells.Item($row, 1).Value2 = $tc.ID
        $testCases.Cells.Item($row, 2).Value2 = $tc.Module
        $testCases.Cells.Item($row, 3).Value2 = $tc.Objective
        $testCases.Cells.Item($row, 4).Value2 = $tc.Pre
        $testCases.Cells.Item($row, 5).Value2 = $tc.Steps
        $testCases.Cells.Item($row, 6).Value2 = $tc.Expected
        $testCases.Cells.Item($row, 7).Value2 = $tc.Actual
        $testCases.Cells.Item($row, 8).Value2 = $tc.Status
        $testCases.Cells.Item($row, 9).Value2 = $tc.Bug
        $testCases.Cells.Item($row, 10).Value2 = $tc.Notes
        $row++
    }

    $lastRow = $row - 1

    $testCases.Range("A1:J1").Font.Bold = $true
    $testCases.Range("A1:J1").Interior.ColorIndex = 15
    $testCases.Range("A1:J1").HorizontalAlignment = -4108
    $testCases.Range("A1:J1").VerticalAlignment = -4108

    $testCases.Range("A1:J$lastRow").Borders.LineStyle = 1
    $testCases.Range("C2:J$lastRow").WrapText = $true
    $testCases.Range("A1:J$lastRow").VerticalAlignment = -4160
    $testCases.Range("A1:J$lastRow").AutoFilter() | Out-Null

    $testCases.Columns('A').ColumnWidth = 16
    $testCases.Columns('B').ColumnWidth = 18
    $testCases.Columns('C').ColumnWidth = 38
    $testCases.Columns('D').ColumnWidth = 42
    $testCases.Columns('E').ColumnWidth = 44
    $testCases.Columns('F').ColumnWidth = 40
    $testCases.Columns('G').ColumnWidth = 38
    $testCases.Columns('H').ColumnWidth = 12
    $testCases.Columns('I').ColumnWidth = 20
    $testCases.Columns('J').ColumnWidth = 34

    $testCases.Rows('1:1').RowHeight = 24
    $testCases.Rows("2:$lastRow").RowHeight = 58

    $statusRange = $testCases.Range("H2:H$lastRow")
    $statusRange.HorizontalAlignment = -4108

    $statusRange.Validation.Delete()
    $statusRange.Validation.Add(3, 1, 1, 'Pass,Fail,Blocked,N/A') | Out-Null

    # Freeze header row
    $testCases.Activate()
    $excel.ActiveWindow.SplitRow = 1
    $excel.ActiveWindow.FreezePanes = $true

    # Save workbook
    $workbook.SaveAs($OutputPath, 51)
    Write-Output "Created: $OutputPath"
}
finally {
    if ($workbook -ne $null) { $workbook.Close($true) | Out-Null }
    if ($excel -ne $null) { $excel.Quit() }

    foreach ($obj in @($statusRange, $testCases, $overview, $workbook, $excel)) {
        if ($obj -ne $null) {
            [void][System.Runtime.Interopservices.Marshal]::ReleaseComObject($obj)
        }
    }

    [GC]::Collect()
    [GC]::WaitForPendingFinalizers()
}

