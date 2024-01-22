varSSID = "Uwu"
varPassword = "makaroniarz123"
city = "Warszawa"
dataToSend = {}
dataGot = {}
myTimer = tmr.create()
decoder = sjson.decoder()

dataGot["wifi"] = false
dataGot["weather"] = false
dataGot["dateTime"] = false

weatherStatus = false
timezoneStatus = false
timeStatus = false

function createWeatherQueston(city)
    part1 = "http://api.openweathermap.org/data/2.5/weather?q="
    part2 = "&appid=2085dd69af79e62bdb50e4448b053eef"
    return part1 .. city .. part2;
end

function createContinentQuestion(city)
    part1 = "https://api.opencagedata.com/geocode/v1/json?q="
    part2 = "&key=0b7457cafd7d46fc9cbb768341822da3"
    return part1 .. city .. part2
end

function createTimeDateQuestion(timezone)
    part1 = "http://worldtimeapi.org/api/timezone/"
    return part1 .. timezone
end

function createWifiConfig(login, password)
    station_cfg={}
    station_cfg.ssid = login
    station_cfg.pwd = password
    station_cfg.save = false
    return station_cfg
end

function weatherCallback(code, data)
    if (code < 0) then
        print("HTTP request failed")
    else
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
             print(key, value)
        end
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

function continentCallback(code, data)
    if (code < 0) then
        print("HTTP request failed")
    else
        convertedData = sjson.decode(data)
        
        dataToSend["timeOffset"] = tonumber(string.sub(convertedData["utc_offset"], 1, 3))
        tempData = unixToTime(tonumber(convertedData["unixtime"]), dataToSend["timeOffset"])
        
        for key, value in pairs(tempData) do
             dataToSend[key] = tempData[key]
        end
        http.request(createWeatherQueston(city), "GET", "", "", weatherCallback)
    end
end

function start()
    print("START")
    wifi.setmode(wifi.STATION)
    wifi.sta.config(createWifiConfig(varSSID, varPassword))
    myTimer:alarm(5000, tmr.ALARM_AUTO, function()
    if wifi.sta.getip() == nil then
        print("Connecting to WiFi...")
    else
        print("Connected to WiFi. IP address: " .. wifi.sta.getip())
        http.request("http://worldtimeapi.org/api/ip/", "GET", "", "", continentCallback)

        myTimer:stop()
    end
end)

end

start()