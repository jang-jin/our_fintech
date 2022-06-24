import socket

host = '168.131.154.137'  # 호스트 ip
port = 8080            # 포트번호

server_sock = socket.socket(socket.AF_INET) 
server_sock.bind((host, port)) 
server_sock.listen(1) 
print("기다리는 중")
out_data = int(1122332211)

client_sock, addr = server_sock.accept()
if client_sock:
    print('Connected by', addr)
    in_data = client_sock.recv(1024)
    print(in_data.decode("utf-8"), len(in_data))

    salary = int(in_data.decode("utf-8")[2:])
    print("salary : ", salary)

    client_sock.send(str(out_data).encode('utf-8'))
    print('send : ', out_data)

client_sock.close()
server_sock.close()