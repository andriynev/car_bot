# Car Bot
## Setup project
### **Requirements :** 
- MongoDB 4.4.5+

To setup all services need to execute the command (need ensure that you have installed docker and docker-compose):
```
docker-compose -f docker-compose.yml up -d
```

To test connection to Mongo you can use command:
```
mongo 127.0.0.1:27018 -u "car_bot" -p "car_bot" --authenticationDatabase "car_bot_db"
```