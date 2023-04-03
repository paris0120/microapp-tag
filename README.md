# Tag Server

This application is a "microservice" application intended to be part of a microservice architecture.
It provides Tag management service to the other apps through Kafka. There is no user interface for users.
Only admin can directly manage tags through the API. All the other operations are through Kafka command with microapp.tag.domain.KafkaCmd.

## Rest Api

## Dependencies

This application is configured for Service Discovery and Configuration with Consul.
On launch, it will refuse to start if it is not able to connect to the following services.

1. Consul: http://localhost:8500
2. Kafka: http://localhost:9092
3. Oauth2: http://localhost:9080

## Gateway Frontend Dependencies

Gateway needs the following libraries to properly load the app.

1. "antd": "^5.2.1",
2. "antd-colorpicker": "^1.0.0",

## Microfrontend Expose

1. './entities-admin-menu': './src/main/webapp/app/entities/menu',
2. './entities-admin-routes': './src/main/webapp/app/entities/routes',

## Commands

1. TAGS [server name] [type name] [parent id]: list all the tags for the selected server and type.

## Configurations

1. Consul:
2. Kafka:
3. Oauth2:
