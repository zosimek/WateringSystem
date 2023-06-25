import busio
import digitalio
import board
import json
import adafruit_mcp3xxx.mcp3008 as MCP
from adafruit_mcp3xxx.analog_in import AnalogIn
from time import sleep

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

def calibrate_sensor_dry(chan):
    max_val = None
    min_val = None
    baseline_check = input("Is capative sensor dry? (enter 'y' to procede):")
    if baseline_check == "y":
        max_val = chan.value
        print("------{:>5}\t{:>5}".format("raw", "v"))
        for i in range(0, 10):
            if chan.value > max_val:
                max_val = chan.value
            print("CHAN: " + "{:>5}\t{:>5.3}".format(chan.value, chan.voltage))
            sleep(0.5)
    return max_val

def calibrate_sensor_wet(chan):
    water_check = input("Is capative sensor wet? (enter 'y' to procede):")
    if water_check == "y":
        min_val = chan.value
        print("------{:>5}\t{:>5}".format("raw", "v"))
        for i in range(0, 10):
            if chan.value > min_val:
                min_val = chan.value
            print("CHAN: " + "{:>5}\t{:>5.3}".format(chan.value, chan.voltage))
            sleep(0.5)
    return min_val

print("Sensor 1")
max_value_1 = calibrate_sensor_dry(chan0)
print("Sensor 2")
max_value_2 = calibrate_sensor_dry(chan1)
print("Sensor 3")
max_value_3 = calibrate_sensor_dry(chan2)
print("Sensor 4")
max_value_4 = calibrate_sensor_dry(chan3)

print("Sensor 1")
min_value_1 = calibrate_sensor_wet(chan0)
print("Sensor 2")
min_value_2 = calibrate_sensor_wet(chan1)
print("Sensor 3")
min_value_3 = calibrate_sensor_wet(chan2)
print("Sensor 4")
min_value_4 = calibrate_sensor_wet(chan3)

sensor1= {
    "full_saturation": min_value_1,
    "zero_saturation": max_value_1
    }
print("Sensor 2 \n")
sensor2= {
    "full_saturation": min_value_2,
    "zero_saturation": max_value_2
    }
print("Sensor 3 \n")
sensor3= {
    "full_saturation": min_value_3,
    "zero_saturation": max_value_3
    }
print("Sensor 4 \n")
sensor4= {
    "full_saturation": min_value_4,
    "zero_saturation": max_value_4
    }

config_data = {
    "sensor1":sensor1,
    "sensor2":sensor2,
    "sensor3":sensor3,
    "sensor4":sensor4
    }

with open("capacitive_sensor_config.json", "w") as outfile:
    json.dump(config_data, outfile)

print('\n')
print(config_data)