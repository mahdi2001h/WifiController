#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>

#define SSID "node mcu"
#define PASS "12345678"
#define PORT 8888

#define LED LED_BUILTIN
#define OUT 16
#define BTN D3

WiFiServer server(PORT);
WiFiClient client;

char res[20];
int val;
int c;
int x;
int y = 0;

int compare(char *a, char *b)
{
    for (int i = 0; i < 200; i++)
    {
        if (a[i] != b[i])
        {
            return 0;
        }

        if (a[i] == '\r' && b[i] == '\r')
        {
            return 1;
        }
    }
}

void setup()
{
    pinMode(LED, OUTPUT);
    pinMode(OUT, OUTPUT);
    pinMode(BTN, INPUT);
    digitalWrite(LED, LOW);
    digitalWrite(OUT, LOW);
    digitalWrite(BTN, LOW);

    Serial.begin(115200);

    WiFi.mode(WIFI_AP);
    WiFi.softAP(SSID, PASS);
    server.begin();

    Serial.println("Starting the access point...");
    Serial.print("ssid : ");
    Serial.println(SSID);
    Serial.print("pass : ");
    Serial.println(PASS);
    Serial.print("IP address: ");
    Serial.println(WiFi.softAPIP());
    Serial.print("on port ");
    Serial.println(PORT);
}

void loop()
{
    client = server.available();

    if (client)
    {
        if (client.connected())
        {
            digitalWrite(LED, LOW);
            Serial.println("Client Connected");
        }

        while (client.connected())
        {
            if (client.available())
            {
                String line = client.readStringUntil('\n');
                Serial.println(line);
                if (line =="state:0\r")
                {
                    Serial.println("seted low");
                    digitalWrite(OUT, LOW);
                    client.write("state:0\n");
                }
                if (line=="state:1\r")
                {
                    Serial.println("seted high");
                    client.write("state:1\n");
                    digitalWrite(OUT, HIGH);
                }
            }

            if (digitalRead(BTN) == LOW)
            {
                if (digitalRead(OUT) == HIGH)
                {
                    digitalWrite(OUT, LOW);
                    client.write("state:0\n");
                }
                else
                {
                    digitalWrite(OUT, HIGH);
                    client.write("state:1\n");
                }

                while (digitalRead(BTN) == LOW)
                    delay(10);
            }
        }
        client.stop();
        Serial.println("Client Disconect");
    }
    else
    {
        digitalWrite(LED, HIGH);
        delay(100);
        digitalWrite(LED, LOW);
        delay(100);
    }
}
