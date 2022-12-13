FROM python:latest
COPY ./requirements.txt /app/requirements.txt
COPY ./exchange.py /app/exchange.py
WORKDIR /app
RUN pip3 install -r requirements.txt
COPY . /app
EXPOSE 5000
ENTRYPOINT [ "python3" ]
CMD ["exchange.py" ]
