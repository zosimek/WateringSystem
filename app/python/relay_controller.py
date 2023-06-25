from time import sleep
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BOARD)

relay1 = 12
relay2 = 11
relay3 = 13
relay4 = 15

GPIO.setup(relay1, GPIO.OUT)
GPIO.setup(relay2, GPIO.OUT)
GPIO.setup(relay3, GPIO.OUT)
GPIO.setup(relay4, GPIO.OUT)

def pumpActive(pump, waterAmount):
    if pump == 11:
        wateringTime = (waterAmount + 55) * 3 / 100
    elif pump == 13:
        wateringTime = (waterAmount + 10) * 3 / 100
    elif pump == 15:
        wateringTime = (waterAmount + 20) * 3 / 100
    else:
        wateringTime = waterAmount * 3 / 100 # calculate working time of the pump to supply wright ammount of water
    return pump, wateringTime