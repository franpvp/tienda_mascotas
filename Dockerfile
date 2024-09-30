# Dockerfile
FROM openjdk:21-ea-24-oracle

WORKDIR /app

COPY target/tienda_mascotas-0.0.1-SNAPSHOT.jar app.jar
COPY Wallet_MGCJ9HJZNDC7F1RK /app/oracle_wallet/
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

