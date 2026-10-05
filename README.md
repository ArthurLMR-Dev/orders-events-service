# Orders Events Service

Serviço de pedidos orientado a eventos, construído com Java 21, Spring Boot,
Kafka em modo KRaft, PostgreSQL, JPA e Flyway.

## Executando localmente

Pré-requisitos: Docker Desktop e Java 21.

```bash
docker compose up -d
./mvnw spring-boot:run
```

O `POST /orders` retorna `202 Accepted` porque a criação é assíncrona:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"total": 99.90}'
```

Use o UUID retornado em `GET /orders/{id}` e em
`PATCH /orders/{id}/status`, com um corpo como
`{"status":"PAID"}`. O histórico fica disponível em
`GET /orders/{id}/history?type=ORDER_PAID&page=0&size=20`.

O event store e a projeção usam grupos Kafka independentes. O tópico
`orders-events` tem três partições e usa `aggregateId` como chave, preservando
a ordem dos eventos de cada pedido. Falhas transitórias usam backoff
exponencial e, após o limite, são encaminhadas para `orders-events-dlt`.

## Testes

Os testes de integração usam Testcontainers para Kafka e PostgreSQL:

```bash
./mvnw test
```
