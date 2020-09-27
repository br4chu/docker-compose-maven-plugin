FROM python:3.8.6-alpine3.12
COPY index.html /
EXPOSE 7000
RUN apk --no-cache add curl
CMD python -m http.server 7000