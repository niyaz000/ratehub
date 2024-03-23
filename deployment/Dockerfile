FROM maven:3.9.6-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests clean package

FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless

WORKDIR /app

COPY --from=builder /app/target/ratehub-*.jar .

EXPOSE 8080

CMD ["java", "-jar", "ratehub-*.jar"]
