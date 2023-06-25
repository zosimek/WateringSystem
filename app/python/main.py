import busio
import digitalio
import board
import json
import datetime
import adafruit_mcp3xxx.mcp3008 as MCP
from adafruit_mcp3xxx.analog_in import AnalogIn
from time import sleep
import RPi.GPIO as GPIO
import random
from soil_moisture import raw_to_percent
from database import sendSensorCurrentMoisture, sendSensorHistory, sendMode, getMoistureThreshold, getMode, getWaterAmount, sendPumpHistory, getPumpOn
#from relay_controller import pumpActive, on_space
#from relay_controller import pumpActive

# create the spi bus
spi = busio.SPI(clock=board.SCK, MISO=board.MISO, MOSI=board.MOSI)

# create the cs (chip select)
cs = digitalio.DigitalInOut(board.D5)

# create the mcp object
mcp = MCP.MCP3008(spi, cs)

# create an analog input channels
chan0 = AnalogIn(mcp, MCP.P0)
chan1 = AnalogIn(mcp, MCP.P1)
chan2 = AnalogIn(mcp, MCP.P2)
chan3 = AnalogIn(mcp, MCP.P3)

# pump (relay module GPIO numbers)
relay1 = 12
relay2 = 11
relay3 = 13
relay4 = 15

GPIO.setup(relay1, GPIO.OUT)
GPIO.setup(relay2, GPIO.OUT)
GPIO.setup(relay3, GPIO.OUT)
GPIO.setup(relay4, GPIO.OUT)

# load calibration coifig to json
with open("capacitive_sensor_config.json") as json_data_file:
    config_data = json.load(json_data_file)

if __name__ == '__main__':
    date_last = datetime.datetime(1975, 3, 10)
    date_last_str = date_last.strftime("%Y-%m-%d %H:%M")
    
    while True:
        date_new = datetime.datetime.now()# save date for sensor history
        date_new_str = date_new.strftime("%Y-%m-%d %H:%M")
        try:
             sensor1, sensor2, sensor3, sensor4 = raw_to_percent(chan0.value, chan1.value, chan2.value, chan3.value)
            
            
            sendSensorCurrentMoisture('plant1', sensor11)
            sendSensorCurrentMoisture('plant2', sensor22)
            sendSensorCurrentMoisture('plant3', sensor33)
            sendSensorCurrentMoisture('plant4', sensor44)
        
            
            if date_new_str != date_last_str:
                sendSensorHistory('plant1', sensor11, date_new_str)
                sendSensorHistory('plant2', sensor22, date_new_str)
                sendSensorHistory('plant3', sensor33, date_new_str)
                sendSensorHistory('plant4', sensor44, date_new_str)
        except Exception as error:
            raise error
        except KeyboardInterrupt:
            print("exiting script")
         print(str(date_new_str) + "-----" + str(sensor33))
        
        # modes and pumps
        if getMode('plant1') == 'auto':
            if int(getMoistureThreshold('plant1')) > sensor1:
		pump, wateringTime = pumpActive(relay1, getWaterAmount("plant1"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF

                sendPumpHistory('plant1', date_last_str)
 
        elif getMode('plant1') == 'manual':
		pump, wateringTime = pumpActive(relay1, getWaterAmount("plant1"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF

        	sendPumpHistory('plant1', date_last_str)
            	sendMode('plant1', 'none')
            
        if getMode('plant2') == 'auto':
            if int(getMoistureThreshold('plant2')) > sensor2:
		pump, wateringTime = pumpActive(relay2, getWaterAmount("plant2"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF

                sendPumpHistory('plant2', date_last_str)

        elif getMode('plant2') == 'manual':
		pump, wateringTime = pumpActive(relay2, getWaterAmount("plant2"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF
            	sendPumpHistory('plant2', date_last_str)
            	sendMode('plant2', 'none')
            
        if getMode('plant3') == 'auto':
            if int(getMoistureThreshold('plant3')) > sensor3:
		pump, wateringTime = pumpActive(relay3, getWaterAmount("plant3"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF
                sendPumpHistory('plant3', date_last_str)

        elif getMode('plant3') == 'manual':
		pump, wateringTime = pumpActive(relay3, getWaterAmount("plant3"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF

            	sendPumpHistory('plant3', date_last_str)
            	sendMode('plant3', 'nome')

            
        if getMode('plant4') == 'auto':
            if int(getMoistureThreshold('plant4')) > sensor4:
		pump, wateringTime = pumpActive(relay4, getWaterAmount("plant4"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF

                sendPumpHistory('plant4', date_last_str)

        elif getMode('plant4') == 'manual':
		pump, wateringTime = pumpActive(relay4, getWaterAmount("plant4"))
		GPIO.output(pump, GPIO.HIGH)# turn the pump ON  
		sleep(wateringTime)# wait till the water will be supplied
		GPIO.output(pump, GPIO.LOW)# turn the pump back OFF

            	sendPumpHistory('plant4', date_last_str)
            	sendMode('plant4', 'none')
            
        date_last_str = date_new_str
        sleep(1)