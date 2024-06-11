// DHT Temperature & Humidity Sensor
// Unified Sensor Library Example
// Written by Tony DiCola for Adafruit Industries
// Released under an MIT license.

// REQUIRES the following Arduino libraries:
// - DHT Sensor Library: https://github.com/adafruit/DHT-sensor-library
// - Adafruit Unified Sensor Lib: https://github.com/adafruit/Adafruit_Sensor

#include <DHT.h> // Include the DHT sensor library

#define DHTPIN 7         // Define the pin where your sensor is connected
#define DHTTYPE DHT22   // Define the type of sensor you're using, could be DHT11, DHT22, etc.
#define SAMPLE_INTERVAL 1000    // Define the interval for sampling temperature readings in milliseconds (e.g., 1 minute)

DHT TemperatureMonitor(DHTPIN, DHTTYPE); // Instantiate the DHT object

void setup() {
  Serial.begin(9600);
  TemperatureMonitor.begin(); // Initialize the sensor
}

void loop() {
  // Read temperature from the sensor
  float temperature = TemperatureMonitor.readTemperature();

  // Check if any reads failed and exit early (to try again).
  if (isnan(temperature)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }


  Serial.println(temperature);



  // Delay for the sampling interval
  delay(SAMPLE_INTERVAL);
}
