varSSID = "Uwu"
varPassword = "makaroniarz123"
city = "Warszawa"
dataToSend = {}
dataGot = {}
myTimer = tmr.create()
decoder = sjson.decoder()

dataGot["login"] = false
dataGot["password"] = false
dataGot["city"] = false


function createWeatherQueston(city)
    part1 = "http://api.openweathermap.org/data/2.5/weather?q="
    part2 = "&appid=2085dd69af79e62bdb50e4448b053eef"
    return part1 .. city .. part2
end

function createWifiConfig(login, password)
    station_cfg={}
    station_cfg.ssid = login
    station_cfg.pwd = password
    station_cfg.save = false
    return station_cfg
end

function weatherCallback(code, data)
    if (code > 0) then
        
        convertedData = sjson.decode(data)
        dataToSend["weather"] = (convertedData["weather"])[1]["main"]
        dataToSend["temp"] = (convertedData["main"])["temp"] - 273.15
        dataToSend["feels_like"] = (convertedData["main"])["feels_like"] - 273.15
        dataToSend["pressure"] = (convertedData["main"])["pressure"]
        dataToSend["humidity"] = (convertedData["main"])["humidity"]
        dataToSend["windSpeed"] = (convertedData["wind"])["speed"]
        dataToSend["sunrise"] = tonumber((convertedData["sys"])["sunrise"])
        dataToSend["sunset"] = tonumber((convertedData["sys"])["sunset"])
        dataToSend["city"] = convertedData["name"]
    end
    temp1 = unixToTime(dataToSend["sunrise"], dataToSend["timeOffset"])["hour"]
    temp2 = unixToTime(dataToSend["sunrise"], dataToSend["timeOffset"])["minutes"]
    dataToSend["sunrise"] = string.format("%02d:%02d", temp1, temp2)
    temp1 = unixToTime(dataToSend["sunset"], dataToSend["timeOffset"])["hour"]
    temp2 = unixToTime(dataToSend["sunset"], dataToSend["timeOffset"])["minutes"]
    dataToSend["sunset"] = string.format("%02d:%02d", temp1, temp2)
    for key, value in pairs(dataToSend) do
        dataToSend[key] = tostring(dataToSend[key])
    end
    myStr = sjson.encode(dataToSend)
    if string.len(myStr) < 500 then
        for k = string.len(myStr), 499 do
            myStr = myStr .. "_"
            end
    end
    uart.write(0, myStr)
end

function unixToTime(unixTimestamp, offset)
    
    table = {}
    local dateTable = rtctime.epoch2cal(unixTimestamp + 3600*offset)
    table["hour"] = dateTable["hour"]
    table["minutes"] = dateTable["min"]
    table["seconds"] = dateTable["sec"]
    table["day"] = dateTable["day"]
    table["month"] = dateTable["mon"]
    table["year"] = dateTable["year"]

    return table
end

function timeCallback(code, data)
    if (code > 0) then
      
        convertedData = sjson.decode(data)
        
        dataToSend["timeOffset"] = tonumber(string.sub(convertedData["utc_offset"], 1, 3))
        tempData = unixToTime(tonumber(convertedData["unixtime"]), dataToSend["timeOffset"])
        
        for key, value in pairs(tempData) do
             dataToSend[key] = tempData[key]
        end
        http.request(createWeatherQueston(city), "GET", "", "", weatherCallback)
    end
end

function uartReceive(data)
    myTimer:stop()
    convertedData = sjson.decode(data)
    wifi.sta.config(createWifiConfig(convertedData["login"], convertedData["password"]))
    city = convertedData["city"]
    getHTTP()
    myTimer:start()
end

function getHTTP()
    if wifi.sta.getip() ~= nil then
        http.request("http://worldtimeapi.org/api/ip/", "GET", "", "", timeCallback)
    end
end


function start()
    wifi.setmode(wifi.STATION)
    wifi.sta.config(createWifiConfig(varSSID, varPassword))
    uart.setup(0, 115200, 8, uart.PARITY_NONE, uart.STOPBITS_1, 1)
    uart.on("data", "}", uartReceive)
    
    myTimer:alarm(1000*60, tmr.ALARM_AUTO, function()
        getHTTP()
        
    end)
end

start()
