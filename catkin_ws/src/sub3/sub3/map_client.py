#!/ C:\Python37\python.exe

import numpy as np
import rclpy
import socketio
import cv2
import base64

from rclpy.node import Node
from geometry_msgs.msg import Twist,Point
from squaternion import Quaternion
from nav_msgs.msg import Odometry,Path
from std_msgs.msg import Int8MultiArray
from math import pi,cos,sin,sqrt,atan2


sio = socketio.Client()
global m_control_cmd
m_control_cmd=0

global map_create
map_create = False

global map_save
map_save = False

@sio.event
def connect():
    print('connection established')

@sio.event
def disconnect():
    print('disconnected from server')

# 맵 만들기 중지버튼
@sio.on('stopCreatemap')
def stop_createmap():
    print('stopCreatemap')
    global map_create
    global map_save
    
    map_save = True
    map_create = False

# 맵 만들기 시작버튼
@sio.on('startCreatemap')
def start_createmap():
    print('startCreatemap')
    global map_create
    global map_save

    map_create = True

def get_map_create():
    return map_create, map_save

@sio.on('turnleft')
def turn_left(data):
    global m_control_cmd
    m_control_cmd = data
    print('turnleft')

@sio.on('gostraight')
def go_straight(data):
    global m_control_cmd
    m_control_cmd = data
    print('gostraight')

@sio.on('back')
def back(data):
    global m_control_cmd
    m_control_cmd = data
    print('back')

@sio.on('turnright')
def turn_right(data):
    global m_control_cmd
    m_control_cmd = data
    print('turnright')

def get_global_var():
    return m_control_cmd

def reset_global_var():
    global m_control_cmd
    m_control_cmd = 0

class MapFromServer(Node):

    def __init__(self):
        super().__init__('map_client')
        self.map_publisher = self.create_publisher(Int8MultiArray, 'map_status', 10)
        self.subscription = self.create_subscription(Odometry,'/odom',self.odom_callback,10)
        self.cmd_publisher = self.create_publisher(Twist, 'cmd_vel', 10)
        self.cmd_msg=Twist()

        self.timer_period = 0.1
        self.timer = self.create_timer(self.timer_period, self.timer_callback)

        self.m_control_interval = 10
        self.m_control_iter = 0

        sio.connect('http://j5d201.p.ssafy.io:12001/')


    def odom_callback(self, msg):
        self.is_odom=True
        self.odom_msg=msg
        q=Quaternion(msg.pose.pose.orientation.w,msg.pose.pose.orientation.x,msg.pose.pose.orientation.y,msg.pose.pose.orientation.z)
        _,_,self.robot_yaw=q.to_euler()


    def turtlebot_go(self) :
        self.cmd_msg.linear.x=0.2
        self.cmd_msg.angular.z=0.0


    def turtlebot_back(self) :
        self.cmd_msg.linear.x= -0.2
        self.cmd_msg.angular.z=0.0


    def turtlebot_stop(self) :
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=0.0


    def turtlebot_cw_rot(self) :
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=0.2


    def turtlebot_cww_rot(self) :
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=-0.2


    def timer_callback(self):
        global map_save
        msg = Int8MultiArray()
        map_create_state, map_save_state = get_map_create()
        msg.data = [map_create_state, map_save_state]
        map_save = False
        self.map_publisher.publish(msg)

        # 터틀봇 조작관련 
        ctrl_cmd = get_global_var()

        if ctrl_cmd == 1:

            # turn left

            self.turtlebot_cww_rot()

        elif ctrl_cmd == 2:
            
            # go straight

            self.turtlebot_go()
        
        elif ctrl_cmd == 3:

            # back

            self.turtlebot_back()

        elif ctrl_cmd == 4:
            
            # turn right

            self.turtlebot_cw_rot()
        
        else:

            self.turtlebot_stop()

        self.cmd_publisher.publish(self.cmd_msg)
        
        if ctrl_cmd!=0: 

            self.m_control_iter += 1

        if self.m_control_iter % self.m_control_interval == 0:

            self.m_control_iter = 0

            reset_global_var()
        

def main(args=None):
    
    rclpy.init(args=args)

    map_client = MapFromServer()

    rclpy.spin(map_client)
    
    rclpy.shutdown()

    sio.disconnect()


if __name__ == '__main__':
    main()

