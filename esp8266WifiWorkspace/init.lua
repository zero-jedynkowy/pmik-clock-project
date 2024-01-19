local varSSID = "Uwu"
local varPassword = "makaroniarz123"
local apiQuestion = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s"
local apiKey = "2085dd69af79e62bdb50e4448b053eef"
local city = "Warszawa"

print("START")


wifi.setmode(wifi.STATION)
station_cfg={}
station_cfg.ssid = varSSID
station_cfg.pwd = varPassword
station_cfg.save = false
wifi.sta.config(station_cfg)

local myTimer = tmr.create()
local decoder = sjson.decoder()


myTimer:alarm(5000, tmr.ALARM_AUTO, function()
    if wifi.sta.getip() == nil then
        print("Connecting to WiFi...")
    else
         print("Connected to WiFi. IP address: " .. wifi.sta.getip())
         http.request("http://api.openweathermap.org/data/2.5/weather?q=Warszawa&appid=2085dd69af79e62bdb50e4448b053eef", "GET", "", "", x)
              
        myTimer:stop()
    end
end)



function x(code, data)
    if (code < 0) then
        print("HTTP request failed")
    else
        t = sjson.decode(data)
        x = t["main"]
        print(t["name"])
        x["temp"] = x["temp"] - 273.15
        x["feels_like"] = x["feels_like"] - 273.15
        x["temp_min"] = x["temp_min"] - 273.15
        x["temp_max"] = x["temp_max"] - 273.15
       for k,v in pairs(x) do print(k,v) end
    end
end