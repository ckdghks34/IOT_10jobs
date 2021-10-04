import numpy as np
import cv2
import os
import rclpy
import socketio
import base64

from rclpy.node import Node
from geometry_msgs.msg import Twist,Point
from ssafy_msgs.msg import TurtlebotStatus,EnviromentStatus
from std_msgs.msg import Float32,Int8MultiArray



# client 는 socketio의 기본 API로 구성된 노드입니다. 서버와 연결을 시도해서 서버와 통신을 합니다.

# 각자의 서버 주소에 맞게 connect 함수 안을 바꿔주고, server 스켈레톤코드를 이용해 서비스를 하고 있다면, 연결이 됩니다.
# 버튼을 누르면 해당 키값에 맞는 함수들이 호출이 됩니다. 연결이 된 후에는 emit 함수를 이용해 서버로 키값과 데이터를 보냅니다.
# 이 노드는 AWS EC2에 구축한 서버와 통신만 하는 노드이고, ROS2와 연동하여 사용하면 스마트홈에서 얻은 데이터들을 서버로 보내고, 웹서버로부터의 명령을 ROS2로 전달할 수 있습니다.

# 노드 로직 순서
# 1. 클라이언트 소켓 생성
# 2. 데이터 수신 콜백함수
# 3. 서버 연결
# 4. 데이터 송신

# 로직 1. 클라이언트 소켓 생성
sio = socketio.Client()

global m_control_cmd
m_control_cmd=0

global m_control_num
m_control_num = 0

@sio.event
def connect():
    print('connection established')


# 로직 2. 데이터 수신 콜백함수
@sio.on('AirConditionerOn')
def aircon_on(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('AirConditionerOff')
def aircon_off(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('AirCleanerOn')
def aircleaner_on(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('AirCleanerOff')
def aircleaner_on(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('TvOn')
def tv_on(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('TvOff')
def tv_off(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('LightOn')
def light_on(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('LightOff')
def light_off(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('CurtainOn')
def curtain_on(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('CurtainOff')
def curtain_off(data):
    global m_control_cmd
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.event
def disconnect():
    print('disconnected from server')

# global 변수 초기화 및 리셋
def get_global_var():
    return m_control_cmd

def reset_global_var():
    global m_control_cmd
    m_control_cmd = 0

def get_global_num():
    return m_control_num

def reset_global_num():
    global m_control_cmd
    m_control_num = 0

class ControllFromServer(Node):

    def __init__(self):
        super().__init__('client')

        ## 메시지 송신을 위한 PUBLISHER 생성
        self.cmd_publisher = self.create_publisher(Twist, 'cmd_vel', 10)
        self.app_control_pub = self.create_publisher(Int8MultiArray, 'app_control', 10)
        self.pose_pub = self.create_publisher(Twist, 'iot_pose', 10)

        ## 메시지 수신을 위한 SUBSCRIBER 생성
        self.turtlebot_status_sub = self.create_subscription(TurtlebotStatus,'/turtlebot_status',self.listener_callback,10)
        self.envir_status_sub = self.create_subscription(EnviromentStatus,'/envir_status',self.envir_callback,10)
        self.app_status_sub = self.create_subscription(Int8MultiArray,'/app_status',self.app_callback,10)
        self.timer = self.create_timer(1, self.timer_callback)

        ## 제어 메시지 변수 생성 
        self.cmd_msg=Twist()
        self.pose_msg=Twist()
        
        self.app_control_msg=Int8MultiArray()
        for i in range(17):
            self.app_control_msg.data.append(0)


        self.turtlebot_status_msg=TurtlebotStatus()
        self.envir_status_msg=EnviromentStatus()
        self.app_status_msg=Int8MultiArray()
        self.is_turtlebot_status=False
        self.is_app_status=False
        self.is_envir_status=False

        sio.connect("http://j5d201.p.ssafy.io:12001")



        self.m_control_interval = 10
        self.m_control_iter = 0
        self.m_control_mode = 0
        self.ctr_status = 0
        self.ctr_num = 0
        self.iot_data_x = [-4.3863, -13.0347, -4.4073, -2.0108, -11.6842, -9.4368, -6.8314, -12.4872, -5.11772, -2.6384, -7.28014, -9.3725, -7.47112, -12.3701, -5.10873, -2.49999, -8.26499]
        self.iot_data_y = [-7.6372, -5.0711, -5.5053, -5.4144, -8.40308, -7.4848, -5.7857, -3.3074, -3.85194, -4.1554, -0.883887, -5.0080, -2.48537, -2.53426, -3.1643, -3.71085, -1.30507]
        self.iot_status = ['LightStatus','LightStatus','LightStatus','LightStatus','LightStatus','LightStatus','LightStatus','AirConditionerStatus','AirConditionerStatus','AirConditionerStatus','AirConditionerStatus','AirCleanerStatus', 'TvStatus', 'CurtainStatus','CurtainStatus','CurtainStatus','CurtainStatus']
        self.lfd=0.1
        self.idx_wp = 0
        self.len_wp = None
        self.check_1_wp = False

        sio.emit('sendTime','TEST')
        sio.emit('sendWeather',self.envir_status_msg.weather)
        sio.emit('sendTemperature', self.envir_status_msg.temperature)


    def listener_callback(self, msg):
        self.is_turtlebot_status=True
        self.turtlebot_status_msg=msg

    def envir_callback(self, msg):
        self.is_envir_status=True
        self.envir_status_msg=msg

    def app_callback(self, msg):
        self.is_app_status=True
        self.app_status_msg=msg  

    def app_all_on(self):
        for i in range(17):
            self.app_control_msg.data[i]=1
        self.app_control_pub.publish(self.app_control_msg)
        
    def app_all_off(self):
        for i in range(17):
            self.app_control_msg.data[i]=2
        self.app_control_pub.publish(self.app_control_msg)
        
    def app_on_select(self,num):
        '''
        로직 2. 특정 가전 제품 ON
        '''
        self.app_control_msg.data[num]=1
        self.app_control_pub.publish(self.app_control_msg)


    def app_off_select(self,num):
        '''
        로직 3. 특정 가전 제품 OFF
        '''
        self.app_control_msg.data[num]=2
        self.app_control_pub.publish(self.app_control_msg)

    def turtlebot_go(self) :
        self.cmd_msg.linear.x=0.3
        self.cmd_msg.angular.z=0.0

    def turtlebot_stop(self) :
        '''
        로직 4. 터틀봇 정지
        '''
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=0.0

    def turtlebot_cw_rot(self) :
        '''
        로직 5. 터틀봇 시계방향 회전
        '''
        self.cmd_msg.angular.z=1.0

    def turtlebot_cww_rot(self) :
        '''
        로직 6. 터틀봇 반시계방향 회전
        '''
        self.cmd_msg.angular.z=-1.0
    
    def timer_callback(self):
        # m_control_cmd 리턴 (켜는지 끄는지 : 1 또는 2)
        ctrl_cmd = get_global_var()
        # m_control_num 리턴 (가전제품 번호 : 0 ~ 16)
        ctrl_num = get_global_num()
        
        if ctrl_cmd == 1:       
            self.ctr_status = 1
            self.ctr_num = ctrl_num
            self.app_on_select(self.ctr_num)
            self.pose_msg.angular.x= self.iot_data_x[self.ctr_num]
            self.pose_msg.angular.y= self.iot_data_y[self.ctr_num]
            if self.cmd_msg.linear.x == 0:
                self.pose_pub.publish(self.pose_msg)
            

        elif ctrl_cmd == 2:
            self.ctr_status = 2
            self.ctr_num = ctrl_num
            self.app_off_select(self.ctr_num)
            self.pose_msg.angular.x= self.iot_data_x[self.ctr_num]
            self.pose_msg.angular.y= self.iot_data_y[self.ctr_num]
            if self.cmd_msg.linear.x == 0:
                self.pose_pub.publish(self.pose_msg)
            
        
        self.cmd_publisher.publish(self.cmd_msg)

        if ctrl_cmd != 0: 
            if self.app_status_msg.data[self.ctr_num] == self.ctr_status:
                print(self.app_status_msg.data)
                if self.ctr_status == 1:
                    sio.emit(self.iot_status[self.ctr_num], 'On')
                elif self.ctr_status == 2:
                    sio.emit(self.iot_status[self.ctr_num], 'Off')
                self.ctr_num = 0
                self.ctr_status = 0
                # m_control_cmd을 0으로 리셋 
                reset_global_var()
                # m_control_num을 0으로 리셋 
                reset_global_num()
        
            
        # if ctrl_cmd!=0: 

        #     self.m_control_iter += 1

        # if self.m_control_iter % self.m_control_interval == 0:

        #     self.m_control_iter = 0

        #     reset_global_var()

        self.check_1_wp = False


# 로직 3. 서버 연결
# sio.connect('http://ec2-3-34-134-166.ap-northeast-2.compute.amazonaws.com:12001/')

# sio.connect("http://127.0.0.1:12001")

# 로직 4. 데이터 송신
# sio.emit('sendTime','TEST')
# sio.emit('sendWeather','TEST')
# sio.emit('sendTemperature','TEST')

# sio.wait()

def main(args=None):
    
    rclpy.init(args=args)

    client = ControllFromServer()

    rclpy.spin(client)
    
    rclpy.shutdown()

    sio.disconnect()

if __name__ == '__main__':
    main()