FROM openjdk:22-rc-jdk-oraclelinux9
WORKDIR /opt/app
COPY /target/game_bot.jar game_bot.jar
ENTRYPOINT ["java", "-jar", "game_bot.jar"]