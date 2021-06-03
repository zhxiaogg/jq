# Object Query

A lightweight SQL Engine on Java Objects.

## Examples (Ignore it for now)

Simple Streaming:
```java
Relation r = Relation.create("orders", Order.class);
DataSource ds = DataSource.builder().add(r).build();
Streaming streaming = ds.streamQuery("select id, cat from orders where price > 100");
ResultSet results = streaming.fire(new Order(1, "books", 101)); // [1,"books"]
```

Sliding Window Streaming:
```java
String query = "select id, price, avg(price) over past_1h from orders having price > avg(price) " +
        "WINDOW past_1h AS (PARTITION BY id RANGE '1' HOUR PRECEDING)";
Streaming streaming = ds.streamQuery(query);
ResultSet results = streaming.fire(new Order(1, "books", 101)); 
```

Sliding Window + Join Streaming:
```java
String query = "select o.id, p.name, o.price, avg(o.price) over past_1h from "
        "orders as o join proudcts as p on o.pid = p.id  having price > avg(price) " +
        "WINDOW past_1h AS (PARTITION BY id RANGE '1' HOUR PRECEDING)";
Relation orders = Relation.create("orders", Order.class);
Relation products = Relation.create("orders", Order.class);
DataSource ds = DataSource.builder().add(orders).add(products).build();
Streaming streaming = ds.streamQuery(query);
ResultSet results = streaming.fire(new Product(1, 1, 101));
ResultSet results = streaming.fire(new Order(1, 1, 101)); 
```
