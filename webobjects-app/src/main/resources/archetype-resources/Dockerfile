# Build with something like
# docker build --build-arg projname=wodocker --build-arg version=0.0.1-SNAPSHOT -t wodocker:0.0.1-SNAPSHOT -t wodocker:latest .
# Run with something like
# docker run -it -p 8080:8080 --rm wodocker
FROM openjdk:17-slim
ARG version
ARG projname
ENV NEXT_ROOT=/
ENV exec=/opt/woapps/${projname}.woa/${projname}
RUN mkdir -p /opt/woapps/; \
	useradd appserver;
RUN apt-get update && apt-get install -y \
    curl \
 && rm -rf /var/lib/apt/lists/*
ADD target/${projname}-${version}.woa /opt/woapps/${projname}.woa
RUN chown -R appserver /opt/woapps;
USER appserver
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 CMD curl -f http://localhost:8080/ || exit 1
ENTRYPOINT exec ${exec} -DWOPort=8080 -DWOAllowRapidTurnaround=true -DWODirectConnectEnabled=true -DWOAllowsConcurrentRequestHandling=true -DWOCachingEnabled=true
