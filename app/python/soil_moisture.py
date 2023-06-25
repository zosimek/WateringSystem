import json

# load calibration coifig to json
with open("capacitive_sensor_config.json") as json_data_file:
    config_data = json.load(json_data_file)

def raw_to_percent(raw_val1, raw_val2, raw_val3, raw_val4):
    """Translates raw value to percentage of moisture in soil"""
    perc_val1 = abs((raw_val1 - config_data["sensor1"]["zero_saturation"])/(config_data["sensor1"]["full_saturation"] - config_data["sensor1"]["zero_saturation"]))*100
    perc_val2 = abs((raw_val2 - config_data["sensor2"]["zero_saturation"])/(config_data["sensor2"]["full_saturation"] - config_data["sensor2"]["zero_saturation"]))*100
    perc_val3 = abs((raw_val3 - config_data["sensor3"]["zero_saturation"])/(config_data["sensor3"]["full_saturation"] - config_data["sensor3"]["zero_saturation"]))*100
    perc_val4 = abs((raw_val4 - config_data["sensor4"]["zero_saturation"])/(config_data["sensor4"]["full_saturation"] - config_data["sensor4"]["zero_saturation"]))*100
    
    return(round(perc_val1), round(perc_val2), round(perc_val3), round(perc_val4))