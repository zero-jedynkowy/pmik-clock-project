import requests

def getWeather(apiKey, city):
    apiQuestionTemplate = f"http://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}"
    print(apiQuestionTemplate)
    try:
        response = requests.get(apiQuestionTemplate)
        response.raise_for_status()  # Raise an exception for bad responses (4xx or 5xx)
        weather_data = response.json()
        return weather_data
    except Exception as error:
        print("ERROR!")
   
    

myData = ["2085dd69af79e62bdb50e4448b053eef",
        "Warszawa"]


if __name__ == "__main__":
    receivedData = getWeather(*myData)
    for i in receivedData:
        print(str(i) + "\t" + str(receivedData[i]))