[CmdletBinding()]
param(
    [string]$GrafanaUrl = "http://localhost:3001",
    [string]$DatabaseHost = "localhost:3306",
    [string]$DatabaseName = "db_barbershop",
    [string]$DatabaseUser = "barbershop_grafana",
    [PSCredential]$GrafanaCredential,
    [Security.SecureString]$DatabasePassword
)

$ErrorActionPreference = "Stop"
$DatasourceUid = "barbershop-mysql"
$DashboardPath = Join-Path $PSScriptRoot "dashboards/rastreabilidade.json"

if (-not $GrafanaCredential) {
    $GrafanaCredential = Get-Credential -Message "Credenciais de administrador do Grafana"
}

if (-not $DatabasePassword) {
    $DatabasePassword = Read-Host "Senha MySQL de $DatabaseUser" -AsSecureString
}

function ConvertFrom-SecureStringPlainText {
    param([Security.SecureString]$Value)

    $pointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($Value)
    try {
        [Runtime.InteropServices.Marshal]::PtrToStringBSTR($pointer)
    }
    finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($pointer)
    }
}

function Invoke-GrafanaApi {
    param(
        [ValidateSet("GET", "POST", "PUT")]
        [string]$Method,
        [string]$Path,
        [object]$Body
    )

    $request = @{
        Method = $Method
        Uri = "$($GrafanaUrl.TrimEnd('/'))$Path"
        Headers = $script:Headers
        ContentType = "application/json"
    }

    if ($null -ne $Body) {
        $request.Body = $Body | ConvertTo-Json -Depth 100 -Compress
    }

    Invoke-RestMethod @request
}

$grafanaPassword = ConvertFrom-SecureStringPlainText $GrafanaCredential.Password
$basicToken = [Convert]::ToBase64String(
    [Text.Encoding]::UTF8.GetBytes("$($GrafanaCredential.UserName):$grafanaPassword")
)
$Headers = @{ Authorization = "Basic $basicToken" }
$grafanaPassword = $null

$health = Invoke-RestMethod -Uri "$($GrafanaUrl.TrimEnd('/'))/api/health" -TimeoutSec 5
if ($health.database -ne "ok") {
    throw "O Grafana respondeu, mas o banco interno não está saudável."
}

# Confirma a autenticação antes de alterar qualquer configuração.
Invoke-GrafanaApi -Method GET -Path "/api/user" | Out-Null

$databasePasswordPlainText = ConvertFrom-SecureStringPlainText $DatabasePassword
$datasource = @{
    name = "Barbershop MySQL"
    uid = $DatasourceUid
    type = "mysql"
    access = "proxy"
    url = $DatabaseHost
    database = $DatabaseName
    user = $DatabaseUser
    isDefault = $true
    jsonData = @{
        database = $DatabaseName
        maxOpenConns = 10
        maxIdleConns = 5
        connMaxLifetime = 14400
        mysqlVersionOption = "8.0"
        timeInterval = "1s"
        timezone = "+00:00"
    }
    secureJsonData = @{ password = $databasePasswordPlainText }
}

$existingDatasource = $null
try {
    $existingDatasource = Invoke-GrafanaApi -Method GET -Path "/api/datasources/uid/$DatasourceUid"
}
catch {
    $statusCode = [int]$_.Exception.Response.StatusCode
    if ($statusCode -ne 404) {
        throw
    }
}

if ($existingDatasource) {
    $datasource.id = $existingDatasource.id
    Invoke-GrafanaApi -Method PUT -Path "/api/datasources/uid/$DatasourceUid" -Body $datasource | Out-Null
    Write-Host "Datasource atualizado: Barbershop MySQL"
}
else {
    Invoke-GrafanaApi -Method POST -Path "/api/datasources" -Body $datasource | Out-Null
    Write-Host "Datasource criado: Barbershop MySQL"
}
$databasePasswordPlainText = $null

$datasourceHealth = Invoke-GrafanaApi -Method GET -Path "/api/datasources/uid/$DatasourceUid/health"
if ($datasourceHealth.status -ne "OK") {
    throw "O datasource foi salvo, mas o teste de conexão falhou: $($datasourceHealth.message)"
}
Write-Host "Conexão MySQL validada."

$dashboard = Get-Content -Raw -Encoding UTF8 $DashboardPath | ConvertFrom-Json
$importBody = @{
    dashboard = $dashboard
    folderUid = ""
    overwrite = $true
    message = "Dashboard de observabilidade provisionada pelo projeto Barbershop"
}
$result = Invoke-GrafanaApi -Method POST -Path "/api/dashboards/db" -Body $importBody

$dashboardUrl = "$($GrafanaUrl.TrimEnd('/'))$($result.url)"
Write-Host "Dashboard configurada com sucesso: $dashboardUrl"
