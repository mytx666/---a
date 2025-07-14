# 测试登录功能
$loginUrl = "http://localhost:8080/api/users/login"
$loginData = @{
    username = "admin"
    password = "123456"
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri $loginUrl -Method POST -ContentType "application/json" -Body $loginData
    Write-Host "状态码: $($response.StatusCode)"
    Write-Host "响应内容:"
    Write-Host $response.Content
} catch {
    Write-Host "请求失败:"
    Write-Host $_.Exception.Message
    if ($_.Exception.Response) {
        Write-Host "错误响应:"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        Write-Host $reader.ReadToEnd()
    }
} 