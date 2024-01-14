import requests

def getWeather(apiKey, lat, lon):
    apiQuestionTemplate = f"https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}"
    try:
        response = requests.get(apiQuestionTemplate)
        response.raise_for_status()  # Raise an exception for bad responses (4xx or 5xx)
        weather_data = response.json()
        return weather_data
    except Exception as error:
        print("ERROR!")
   
    

myData = ["2085dd69af79e62bdb50e4448b053eef",
        "40.7128",
         "-74.0060"]


if __name__ == "__main__":
    receivedData = getWeather(*myData)
    for i in receivedData:
        print(str(i) + "\t" + str(receivedData[i]))