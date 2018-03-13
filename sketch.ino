#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <NewPing.h>
#include <ThingSpeak.h>

 
const char* ssid = "samad";
const char* password = "samad123";
 
uint8_t echo_Pin = D7;       // declare LED pin on NodeMCU Dev Kit
uint8_t trigger_Pin = D6;
uint8_t red_Led = D2;
uint8_t green_Led = D5;

#define MAX_DISTANCE 600 

int sensorvalue=0;
unsigned long myChannelNumber =447862 ;  
const char * myWriteAPIKey = "36058FPXJSPK5NI";
NewPing sonar(trigger_Pin, echo_Pin, MAX_DISTANCE); 
 
void setup () {
 
  Serial.begin(115200);
  WiFi.begin(ssid, password);
  pinMode(echo_Pin,INPUT);
  pinMode(trigger_Pin, OUTPUT);
  pinMode(red_Led,OUTPUT);
  pinMode(green_Led,OUTPUT);
  digitalWrite(red_Led,LOW);

  while (WiFi.status() != WL_CONNECTED) {
 
    delay(5000);
    
    Serial.print("Connecting..");
 
  }
  Serial.println("Wifi Connected");
  ThingSpeak.begin(client); 
}
 
void loop() {
  Serial.println("Entered Loop");
 
 // Serial.println("D5 pins led started");
  Serial.print("Ping: ");
  sensorvalue=20-sonar.ping_cm();
  Serial.print(sensorvalue); // Send ping, get distance in cm and print result (0 = outside set distance range)
  Serial.println("cm");
  
  ThingSpeak.writeField(myChannelNumber, 3, sensorvalue, myWriteAPIKey);
  
 if(digitalRead(echo_Pin)==HIGH){
      digitalWrite(red_Led,HIGH);
      digitalWrite(green_Led,LOW);
      if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
            HTTPClient http;  //Declare an object of class HTTPClient
            http.begin("https://tsgarbagemonitor.000webhostapp.com/api/full/2","70c8bcb5f41a245ebcb2e386bebfe0169c48fc0b");  //Specify request destination
            int httpCode = http.GET(); //Send the request
            Serial.print("HTTP GET called status code is ");
            Serial.println(httpCode);
            if (httpCode > 0) { //Check the returning code
 
                String payload = http.getString();   //Get the request response payload
                Serial.println(payload);                     //Print the response payload
            }
            http.end();   //Close connection
    }
  }
  else{
    digitalWrite(green_Led,HIGH);
 digitalWrite(red_Led,LOW);
  }
 delay(3000);    //Send a request every 30 seconds
 
}
