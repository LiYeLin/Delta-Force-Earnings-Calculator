# 选择一个包含 JRE 21 的精简基础镜像
FROM eclipse-temurin:21-jre-jammy

# 设置工作目录
WORKDIR /app

# 复制打包好的 JAR 文件
COPY target/*.jar app.jar

# 声明端口
EXPOSE 8080

# 定义启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]