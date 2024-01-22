-- Replace with your own values
local url = "https://api.opencagedata.com/geocode/v1/json?q=Warszawa&key=0b7457cafd7d46fc9cbb768341822da3"

-- HTTP GET request function
function httpsGet(url, callback)
    local conn = tls.createConnection()

    conn:on("receive", function(conn, payload)
        callback(payload)
        conn:close()
    end)

    conn:on("connection", function(conn)
        local request = "GET " .. url .. " HTTP/1.1\r\n" ..
                        "Host: " .. string.match(url, "https://([^/]+)") .. "\r\n" ..
                        "Connection: close\r\n\r\n"

        conn:send(request)
    end)

    conn:connect(443, string.match(url, "https://([^/]+)"))
end

-- Example usage
httpsGet(url, function(response)
    print("Response:\n" .. response)
end)
