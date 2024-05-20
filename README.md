# Medicine Management System with Temperature Monitoring

This project utilizes an Arduino UNO microcontroller and a DHT22 sensor to monitor temperature in a medicine storage environment. The system ensures that medicines are stored at optimal temperatures to maintain their efficacy.

## Features
- **Temperature Monitoring**: The DHT22 sensor continuously monitors the temperature of the storage environment.
- **Real-time Display**: Temperature readings are displayed in real-time on an attached display, providing immediate feedback on the storage conditions.
- **Alert System**: If the temperature exceeds predefined thresholds, the system triggers an alert to notify users of potential issues with the storage conditions.
- **Data Logging**: Temperature data is logged at regular intervals, allowing users to track temperature fluctuations over time.
- **Medicine Management**: In addition to temperature monitoring, the system provides features for managing medicines, such as tracking expiration dates and quantities.

## Components
- Arduino UNO: Microcontroller responsible for data processing and control logic.
- DHT22 Sensor: Digital sensor used for measuring temperature and humidity.

## Setup
1. DHT22 Sensor: Connect the sensor to the Arduino UNO with the following pin configuration:
    VCC to 5V
    GND to GND
    Data to a digital pin (e.g., D2) with a 10k pull-up resistor.
2. Upload the Arduino sketch provided in the repository to the Arduino UNO.
3. Ensure that all components are powered and properly connected.
4. Calibrate the system if necessary, setting appropriate temperature thresholds for triggering alerts.
5. Monitor the display for real-time temperature readings and respond to any alerts as needed.

## Usage
1. **Temperature Monitoring**: The system will continuously monitor the temperature of the storage environment.
2. **Alerts**: If the temperature exceeds predefined thresholds, the system will trigger an alert.
3. **Data Logging**: Temperature data is logged at regular intervals, allowing users to track temperature fluctuations over time.
4. **Medicine Management**: Use the provided interface to manage medicines, including tracking expiration dates and quantities.

## Contributors
- [Warushika-Wijayarathna](https://github.com/Warushika-Wijayarathna)

## Acknowledgements
- [Arduino](https://www.arduino.cc/) for providing the hardware and development environment.
- [Adafruit](https://www.adafruit.com/) for their DHT22 sensor library.
- [IJSE-Institute of Software Engineering](https://www.ijse.lk) for supporting the development of this project.

## Support
For any questions, issues, or feature requests, please contact [https://github.com/Warushika-Wijayarathna/medicine-management-system/issues].
