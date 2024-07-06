# icc-services

Spring boot app which exposes a couple of services as below:

1. Get all Order details
2. Create Order
3. Get order by ID
4. Generate PDF Invoice by ID

Above services are consumed by angular front-end app 'icc-ui'.

#### Build App:
> docker-compose --env-file env/dev.env up --build