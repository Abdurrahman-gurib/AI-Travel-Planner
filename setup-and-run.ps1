$ErrorActionPreference = "Stop"

Write-Host "== AI Travel Planner setup ==" -ForegroundColor Cyan

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot

$java17 = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
if (!(Test-Path $java17)) {
    Write-Host "Java 17 was not found at: $java17" -ForegroundColor Yellow
    Write-Host "Install Temurin 17 or update this script with your exact Java 17 path, then run again." -ForegroundColor Yellow
    exit 1
}

$env:JAVA_HOME = $java17
$env:Path = "$env:JAVA_HOMEin;$env:Path"

Write-Host "Using Java:" -ForegroundColor Green
java -version

Write-Host "Starting the app with Gradle Wrapper..." -ForegroundColor Cyan
& .\gradlew.bat bootRun
