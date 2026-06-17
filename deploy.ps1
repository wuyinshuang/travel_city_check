$password = ConvertTo-SecureString "wuyinshuang@11" -AsPlainText -Force
$credential = New-Object System.Management.Automation.PSCredential ("root", $password)

Write-Host "Stopping Tomcat..."
Invoke-Command -ComputerName "47.101.153.130" -Credential $credential -ScriptBlock {
    /usr/local/tomcat11/bin/shutdown.sh
    Start-Sleep -Seconds 5
}

Write-Host "Removing old files..."
Invoke-Command -ComputerName "47.101.153.130" -Credential $credential -ScriptBlock {
    rm -rf /usr/local/tomcat11/webapps/travel-city-checkin
    rm -rf /usr/local/tomcat11/webapps/travel-city-checkin.war
}

Write-Host "Copying WAR file..."
$session = New-PSSession -ComputerName "47.101.153.130" -Credential $credential
Copy-Item -Path "d:\trae\tra_projects\travel_city_check\backend\target\travel-city-checkin.war" -Destination "/usr/local/tomcat11/webapps/" -ToSession $session

Write-Host "Starting Tomcat..."
Invoke-Command -ComputerName "47.101.153.130" -Credential $credential -ScriptBlock {
    /usr/local/tomcat11/bin/startup.sh
    Start-Sleep -Seconds 10
}

Write-Host "Deployment completed!"
Remove-PSSession $session