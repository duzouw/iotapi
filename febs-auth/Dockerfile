FROM openjdk:8u212-jre

COPY ./target/febs-auth-2.1-RELEASE.jar /febs/febs-auth-2.1-RELEASE.jar
ADD agent/ /agent

ENTRYPOINT ["java", "-javaagent:/agent/skywalking-agent.jar", "-Dskywalking.agent.service_name=febs-auth", "-Dskywalking.collector.backend_service=skywalkingIp:11800", "-jar", "/febs/febs-auth-2.1-RELEASE.jar"]