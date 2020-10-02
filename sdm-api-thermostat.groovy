/**
 *
 *  Copyright 2020 David Kilgore. All Rights Reserved
 *
 *  This software is free for Private Use. You may use and modify the software without distributing it.
 *  If you make a fork, and add new code, then you should create a pull request to add value, there is no
 *  guarantee that your pull request will be merged.
 *
 *  You may not grant a sublicense to modify and distribute this software to third parties without permission
 *  from the copyright holder
 *  Software is provided without warranty and your use of it is at your own risk.
 *
 */

metadata {
    definition(name: 'Google Nest Thermostat', namespace: 'dkilgore90', author: 'David Kilgore', importUrl: 'https://raw.githubusercontent.com/dkilgore90/google-sdm-api/master/sdm-api-thermostat.groovy') {
        capability 'Thermostat'
        capability 'RelativeHumidityMeasurement'
        capability 'Refresh'

        attribute 'room', 'string'
        attribute 'connectivity', 'string'
        attribute 'fanTimeout', 'string'
        attribute 'ecoMode', 'string'
        attribute 'ecoCoolPoint', 'number'
        attribute 'ecoHeatPoint', 'number'
        attribute 'tempScale', 'string'

        command 'fanOn', [[name: 'duration', type: 'NUMBER', description: 'length of time, in seconds']]
        command 'setThermostatFanMode', [[name: 'fanmode', type: 'ENUM', constraints: ['auto', 'on']], [name: 'duration', type: 'NUMBER', description: 'length of time, in seconds']]
        command 'setEcoMode', [[name: 'ecoMode*', type: 'ENUM', constraints: ['OFF', 'MANUAL_ECO']]]
        command 'setHeatCoolSetpoint', [[name: 'heatPoint*', type: 'NUMBER'], [name: 'coolPoint*', type: 'NUMBER']]
    }
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def uninstalled() {

}

def initialize() {

}

def refresh() {
    parent.getDeviceData(device)
}

def auto() {
    parent.deviceSetThermostatMode(device, 'HEATCOOL')
}

def cool() {
    parent.deviceSetThermostatMode(device, 'COOL')
}

def heat() {
    parent.deviceSetThermostatMode(device, 'HEAT')
}

def fanAuto() {
    parent.deviceSetFanMode(device, 'OFF')
}

def fanOn(duration=900) {
    def sDuration = duration.toString() + 's'
    parent.deviceSetFanMode(device, 'ON', sDuration)
}

def off() {
    parent.deviceSetThermostatMode(device, 'OFF')
}

def setCoolingSetpoint(temp) {
    def mode = device.currentValue('thermostatMode')
    if (mode == 'cool') {
        parent.deviceSetTemperatureSetpoint(device, null, temp)
    } else {
        log.warn("Cannot setCoolingSetpoint in thermostatMode: ${mode}")
    }
}

def setHeatingSetpoint(temp) {
    def mode = device.currentValue('thermostatMode')
    if (mode == 'heat') {
        parent.deviceSetTemperatureSetpoint(device, temp, null)
    } else {
        log.warn("Cannot setHeatingSetpoint in thermostatMode: ${mode}")
    }
}

def setHeatCoolSetpoint(heat, cool) {
    def mode = device.currentValue('thermostatMode')
    if (mode == 'auto') {
        parent.deviceSetTemperatureSetpoint(device, heat, cool)
    } else {
        log.warn("Cannot setHeatCoolSetpoint in thermostatMode: ${mode}")
    }
}

def setThermostatMode(mode) {
    parent.deviceSetThermostatMode(device, mode == 'auto' ? 'HEATCOOL' : mode.toUpperCase())
}

def setThermostatFanMode(mode, duration=null) {
    def sDuration = duration ? duration.toString() + 's' : null
    parent.deviceSetFanMode(device, mode == 'auto' ? 'OFF' : 'ON', duration)
}

def setEcoMode(mode) {
    parent.deviceSetEcoMode(device, mode)
}

def setSchedule(sched) {
    log.info("setSchedule is not currently supported")
}

def fanCirculate() {
    log.info("fanCirculate is not currently supported")
}

def emergencyHeat() {
    log.info("emergencyHeat is not currently supported")
}


