# Usa uma imagem do Maven para compilar o projeto
FROM maven:3.9.9-eclipse-temurin-23 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY . .

# Faz o build do projeto
RUN mvn clean package -DskipTests

# Usa uma imagem base com JDK e MySQL
FROM eclipse-temurin:23-jdk

# Instala o MySQL
RUN apt-get update && apt-get install -y mysql-server && \
    apt-get clean

# Define o diretório de trabalho dentro do container
WORKDIR /app



# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe as portas para o MySQL e a aplicação
EXPOSE 8080 3306


# Copia o script de inicialização do MySQL
COPY init.sql /docker-entrypoint-initdb.d/

# Define as variáveis de ambiente para o MySQL (sem senha)
ENV MYSQL_DATABASE=api-rest
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dserver.address=0.0.0.0"

# Inicia o MySQL e depois a aplicação
CMD service mysql start && sleep 5 && mysql -u root -p$MYSQL_ROOT_PASSWORD < /docker-entrypoint-initdb.d/init.sql && java -jar app.jar