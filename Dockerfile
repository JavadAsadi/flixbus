FROM gradle:7.1.1 as build
WORKDIR /work/app

MAINTAINER JavadAsadi


COPY src src
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN gradle bootJar --no-daemon -x test  #-x excluding tests
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM openjdk:8-jre-alpine


VOLUME /tmp
ARG DEPENDENCY=work/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.flixbus.agencyManagement.AgencyManagementApplicationKt"]