FROM maven:3.9.9-eclipse-temurin-21 AS construcao

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /work

COPY --from=construcao --chown=1001:0 /workspace/target/quarkus-app/ ./
RUN mkdir -p /work/data \
    && chown -R 1001:0 /work \
    && chmod -R g+rwX /work

ENV QUARKUS_HTTP_HOST=0.0.0.0

EXPOSE 8080
USER 1001

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
