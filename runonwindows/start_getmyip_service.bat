"C:\Program Files (x86)\Windows Resource Kits\Tools\instsrv.exe" GetMyIp "C:\Program Files (x86)\Windows Resource Kits\Tools\srvany.exe"

sc.exe description GetMyIp "Send current ip-address"
REM sc.exe failure GetMyIp reset= 600 actions= restart/5000
sc.exe config GetMyIp DisplayName= "GetMyIp ver 1.1" 
