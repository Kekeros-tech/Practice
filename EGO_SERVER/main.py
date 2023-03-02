import socket
import numpy as np
from smt.applications import EGO
from sys import argv
from smt.applications.mixed_integer import (
    INT,
)

pathToFile = ""

def function(x):
    x1 = int(x[:, 0][0])
    x2 = int(x[:, 1][0])
    #response = bytes(str(x1) + " " + str(x2) + "\n", "ascii")
    #conn.send(response)
    #print(response.decode())
    duration = bytes(str(x1)+"\n", "ascii")
    conn.send(duration)
    strategy = bytes(str(x2)+"\n", "ascii")
    conn.send(strategy)
    while True:
        dataForRes = conn.recv(1024)
        resStr = str(dataForRes)
        if "\\n" in resStr:
            break
        if not resStr:
            break
    return int(dataForRes.decode())

def work_of_algorithm(path, n_iter):
    xtypes_my = [INT, INT]
    f = open(path, 'r')
    window_time_bounds = [float(border) for border in f.readline().split()]
    sorting_method = [float(border) for border in f.readline().split()]
    xlimits2 = np.array(
        [window_time_bounds, sorting_method]
    )
    criterion = "LCB"  #'EI' or 'SBO' or 'LCB'
    qEI = "KB"

    b = []
    y = []
    while True:
            current_str = f.readline()
            if current_str == '':
                break
            else:
                a = [float(current_point) for current_point in current_str.split()]
                b.append(a)
                point = float(f.readline())
                y.append(point)

    ydoe_my = np.array(y)
    xdoe_my = np.array(b)
    f.close()

    ego = EGO(
        n_iter=int(n_iter),
        criterion=criterion,
        xdoe=xdoe_my,
        ydoe=ydoe_my,
        xtypes=xtypes_my,
        xlimits=xlimits2,
        qEI=qEI,
    )

    x_opt, y_opt, _, x_data, y_data = ego.optimize(fun=function)
    return "success"

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    sock = socket.socket()
    sock.bind(('localhost', 500))
    sock.listen(1)
    conn, addr = sock.accept()
    print('connected:', addr)

    while True:
        dataForPath = conn.recv(1024)
        path_to_file = str(dataForPath)
        if "\\n" in path_to_file:
            break
        if not path_to_file:
            break

    while True:
        dataForIter = conn.recv(1024)
        number_of_iter = str(dataForIter)
        if "\\n" in number_of_iter:
            break
        if not number_of_iter:
            break

    work_of_algorithm(dataForPath.decode().removesuffix("\n"), dataForIter.decode().removesuffix("\n"))
    conn.close()
