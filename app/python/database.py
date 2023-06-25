import datetime
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

# Fetch the service account key JSON file contents
cred = credentials.Certificate('serviceAccountKey.json')

# Initialize the app with a service account, granting admin privileges
firebase_admin.initialize_app(cred, {
    'databaseURL':'https://dbtry-fd6d7-default-rtdb.firebaseio.com/'
    })

# save data
'''ref = db.reference('plant1')
ref.update({
    'mode': 'manual'
    })'''

def getMode(plant):
    dbPlant = db.reference("10032311/" + plant).get()
    return dbPlant['mode']
def getPumpOn(plant):
    dbPlant = db.reference("10032311/" + plant)
    dbPump = dbPlant.child('pump').get()
    return dbPump['on'] 

def getMoistureThreshold(plant):
    dbPlant = db.reference("10032311/" + plant)
    dbSensor = dbPlant.child('sensor').get()
    return dbSensor['moistureThreshold']

def getWaterAmount(plant):
    dbPlant = db.reference("10032311/" + plant)
    dbPump = dbPlant.child('pump').get()
    return dbPump['waterAmount']

############ SEND ##############

def sendSensorCurrentMoisture(plant, sensor):    
    dbPlant = db.reference("10032311/" + plant)
    dbSensor = dbPlant.child('sensor')
    #dbSensorCurrentValue = dbSensor.child('currentMoistureValue')
    
    dbSensor.update({
        'currentMoistureLevel' : sensor
        })

def sendSensorHistory(plant, sensor, dt):
    dbPlant = db.reference("10032311/" + plant)
    dbSensor = dbPlant.child('sensor')
    dbSensorHistory = dbSensor.child('history')
    dbHistoryDate = dbSensorHistory.child(dt)
    
    dbHistoryDate.set(sensor)

def sendPumpHistory(plant, dt):
    dbPlant = db.reference("10032311/" + plant)
    dbPump = dbPlant.child('pump')
    dbPumpHistory = dbPump.child('history')
    dbHistoryDate = dbPumpHistory.child(dt)
    
    dbHistoryDate.set(getWaterAmount(plant))

def sendMode(plant, mode):
    dbPlant = db.reference("10032311/" + plant)
    dbPlant.update({
        'mode' : mode
        })

print(getWaterAmount('plant1'))
