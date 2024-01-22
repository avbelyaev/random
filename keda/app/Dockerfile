FROM python:3.9-alpine

WORKDIR /app

COPY requirements.txt /app
RUN apk update
RUN pip install -r requirements.txt --no-cache-dir

COPY main.py /app

EXPOSE 5000

ENTRYPOINT ["python", "main.py"]