#!/ C:\Python37\python.exe

import numpy as np
import rclpy
import socketio
import cv2
import base64
import json

from rclpy.node import Node
from sensor_msgs.msg import CompressedImage
from geometry_msgs.msg import Twist,Point
from squaternion import Quaternion
from nav_msgs.msg import Odometry,Path
from std_msgs.msg import Int8MultiArray, String
from ssafy_msgs.msg import TurtlebotStatus,EnviromentStatus
from math import pi,cos,sin,sqrt,atan2


sio = socketio.Client()
global robot_control
robot_control=0

global auto_switch
auto_switch=0

global search_switch
search_switch = 0

global m_control_num
m_control_num = 0

global m_control_cmd
m_control_cmd=0

global map_create
map_create = False

global map_save
map_save = False

global map_Auto
map_Auto = False

# 사진찍는거 관련
flag = False

@sio.event
def connect():
    print('connection established')

@sio.event
def disconnect():
    print('disconnected from server')

## 맵 조작 관련 method
# 맵 만들기 중지버튼
@sio.on('stopCreatemap')
def stop_createmap():
    # print('stopCreatemap')
    global map_create
    global map_save
    
    map_save = True
    map_create = False

# 맵 만들기 시작버튼
@sio.on('startCreatemap')
def start_createmap():
    # print('startCreatemap')
    global map_create
    global map_save

    map_create = True

# 맵 만들때 automode ON
@sio.on('mapAutoOn')
def autoMap():
    # print('mapAutoOn')
    global map_Auto
    map_Auto = True

# 맵 만들때 automode off
@sio.on('mapAutoOff')
def autoMap():
    # print('mapAutOff')
    global map_Auto
    map_Auto = False

def get_map_create():
    return map_create, map_save

def get_map_auto():
    return map_Auto

## 터틀봇 조작관련 method
# 왼쪽
@sio.on('turnleft')
def turn_left(data):
    global robot_control
    robot_control = data
    print('turnleft')

# 앞
@sio.on('gostraight')
def go_straight(data):
    global robot_control
    robot_control = data
    print('gostraight')

# 뒤
@sio.on('goback')
def back(data):
    global robot_control
    robot_control = data
    print('back')

# 왼쪽
@sio.on('turnright')
def turn_right(data):
    global robot_control
    robot_control = data
    print('turnright')

def get_robot_var():
    return robot_control

def reset_robot_var():
    global robot_control
    robot_control = 0


## iot 제어관련 method
@sio.on('AirConditionerOn')
def aircon_on(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('AirConditionerOff')
def aircon_off(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('AirCleanerOn')
def aircleaner_on(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('AirCleanerOff')
def aircleaner_on(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('TvOn')
def tv_on(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('TvOff')
def tv_off(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('LightOn')
def light_on(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('LightOff')
def light_off(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('CurtainOn')
def curtain_on(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

@sio.on('CurtainOff')
def curtain_off(data):
    global m_control_cmd, m_control_num
    m_control_cmd = data['ctr_cmd']
    m_control_num = data['ctr_num']
    print('message received with ', data)

def get_global_var():
    return m_control_cmd, auto_switch

def reset_global_var():
    global m_control_cmd
    m_control_cmd = 0

def get_global_num():
    return m_control_num

def reset_global_num():
    global m_control_num
    m_control_num = 0

## 방범모드 관련 method
@sio.on('patrolOn')
def patrol_on(data):
    global auto_switch
    global map_Auto
    auto_switch = data
    map_Auto = True

@sio.on('patrolOff')
def patrol_on(data):
    global auto_switch
    global map_Auto

    auto_switch = data
    map_Auto = False


## 물건찾기 관련
@sio.on('findWallet')
def findWallet(data):
    global search_switch
    search_switch = 1
    print('지갑찾기')

@sio.on('findBag')
def findBackpack(data):
    print('가방찾기')
    global search_switch
    search_switch = 2

@sio.on('findKey')
def findKey(data):
    print('키 찾기')
    global search_switch
    search_switch = 3

@sio.on('findRemote')
def findRemote(data):
    print('리모컨 찾기')
    global search_switch
    search_switch = 4


# 방범모드시에 사람을 인식했을때 박스를 위한 함수
def non_maximum_supression(bboxes, threshold=0.5):
    
    bboxes = sorted(bboxes, key=lambda detections: detections[3],
            reverse=True)
    new_bboxes=[]
    
    new_bboxes.append(bboxes[0])
    
    bboxes.pop(0)

    for _, bbox in enumerate(bboxes):

        for new_bbox in new_bboxes:

            x1_tl = bbox[0]
            x2_tl = new_bbox[0]
            x1_br = bbox[0] + bbox[2]
            x2_br = new_bbox[0] + new_bbox[2]
            y1_tl = bbox[1]
            y2_tl = new_bbox[1]
            y1_br = bbox[1] + bbox[3]
            y2_br = new_bbox[1] + new_bbox[3]
            
            x_overlap = max(0, min(x1_br, x2_br)-max(x1_tl, x2_tl))
            y_overlap = max(0, min(y1_br, y2_br)-max(y1_tl, y2_tl))
            overlap_area = x_overlap * y_overlap
            
            area_1 = bbox[2] * new_bbox[3]
            area_2 = new_bbox[2] * new_bbox[3]
            
            total_area = area_1 + area_2 - overlap_area

            overlap_area = overlap_area / float(total_area)

            if overlap_area < threshold:
                
                new_bboxes.append(bbox)

    return new_bboxes

class clientFromServer(Node):

    def __init__(self):
        super().__init__('client')
        ## 메시지 송신을 위한 publisher
        # 맵 [맵 만들기 시작, 맵 저장 여부]를 publish
        self.map_publisher = self.create_publisher(Int8MultiArray, 'map_status', 10) 
        
        # 맵 [autoMode] 동작을 publish
        self.automap_publisher = self.create_publisher(Int8MultiArray,'map_auto',10)

        # 터틀봇 조작 관련 publisher
        self.cmd_publisher = self.create_publisher(Twist, 'cmd_vel', 10)

        # iot 조작 관련 publisher
        self.app_control_pub = self.create_publisher(Int8MultiArray, 'app_control', 10)
        
        # iot 제품의 위치를 publish
        self.pose_pub = self.create_publisher(Twist, 'iot_pose', 10)

        # 방범모드 상태를 publish
        # self.patrol_status = self.create_publisher(Int8MultiArray, 'patrol_status', 10)


        ## 메시지 수신을 위한 subscriber
        # 실시간 맵정보를 subscription
        self.map_status = self.create_subscription(String, '/map_string', self.map_string_callback, 10)
        
        # 터틀봇 상태를 subscription
        self.turtlebot_status_sub = self.create_subscription(TurtlebotStatus,'/turtlebot_status',self.listener_callback,10)
        
        # 환경정보를 subscription
        self.envir_status_sub = self.create_subscription(EnviromentStatus,'/envir_status',self.envir_callback,10)
        
        # 기기정보를 subscription
        self.app_status_sub = self.create_subscription(Int8MultiArray,'/app_status',self.app_callback,10)

        # 방범용 global_path2 받기
        self.path_sub = self.create_subscription(Path,'/global_path2',self.path_callback,10)

        # odom 정보 받기
        self.subscription = self.create_subscription(Odometry,'/odom',self.odom_callback,10)

        # 카메라 정보 받기
        self.subscription = self.create_subscription(CompressedImage, '/image_jpeg/compressed',self.img_callback,10)
        self.timer_period = 0.1
        self.timer = self.create_timer(self.timer_period, self.timer_callback)
        
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
        
        sio.connect('http://j5d201.p.ssafy.io:12001/')

        self.m_control_interval = 10
        self.m_control_iter = 0
        self.ctr_status = 0
        self.ctr_num = 0
        self.check_cmd = 0
        # iot 좌표 및 상태
        self.iot_data_x = [-4.3863, -13.0347, -4.4073, -2.0108, -11.6842, -9.4368, -6.8314, -12.4872, -5.11772, -2.6384, -7.28014, -9.3725, -7.47112, -12.3701, -5.10873, -2.49999, -8.26499]
        self.iot_data_y = [-7.6372, -5.0711, -5.5053, -5.4144, -8.40308, -7.4848, -5.7857, -3.3074, -3.85194, -4.1554, -0.883887, -5.0080, -2.48537, -2.53426, -3.1643, -3.71085, -1.30507]
        self.iot_status = ['LightStatus','LightStatus','LightStatus','LightStatus','LightStatus','LightStatus','LightStatus','AirConditionerStatus','AirConditionerStatus','AirConditionerStatus','AirConditionerStatus','AirCleanerStatus', 'TvStatus', 'CurtainStatus','CurtainStatus','CurtainStatus','CurtainStatus']

        # 방범 관련 변수
        self.lfd=0.1
        self.idx_wp = 0
        self.len_wp = None
        self.check_1_wp = False

        self.byte_data = None
        self.img_bgr = None   
    
        self.pedes_detector = cv2.HOGDescriptor()
        self.pedes_detector.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
        
        # 물건찾기 관련 변수
        self.wal_detected = False
        self.key_detected = False
        self.bp_detected = False
        self.rc_detected = False

    # 터틀봇 상태 전송(배터리에 활용)
    def listener_callback(self, msg):
        self.is_turtlebot_status=True
        self.turtlebot_status_msg=msg
        sio.emit('BotStatus', self.turtlebot_status_msg.battery_percentage)


    # 환경 정보에 사용(현재는 구현 X)
    def envir_callback(self, msg):
        self.is_envir_status=True
        self.envir_status_msg=msg
    

    # iot 상태를 전송
    def app_callback(self, msg):
        self.is_app_status=True
        self.app_status_msg=msg  
        M = dict(zip(range(0, len(self.app_status_msg.data)),self.app_status_msg.data))
        sio.emit('ApplianceStatus', json.dumps(M))
    
    # 맵 실시간 정보 함수
    def map_string_callback(self, msg):
        self.map_string_msg = msg
        sio.emit('mapStreaming', self.map_string_msg.data )
    
    # # 전체 제품 on (사용 x)
    # def app_all_on(self):
    #     for i in range(17):
    #         self.app_control_msg.data[i]=1
    #     self.app_control_pub.publish(self.app_control_msg)

    # # 전체 제품 off (사용 x)    
    # def app_all_off(self):
    #     for i in range(17):
    #         self.app_control_msg.data[i]=2
    #     self.app_control_pub.publish(self.app_control_msg)


    # 특정 가전 제품 ON
    def app_on_select(self,num):
        self.app_control_msg.data[num]=1
        self.app_control_pub.publish(self.app_control_msg)


    # 특정 가전 제품 OFF
    def app_off_select(self,num):
        self.app_control_msg.data[num]=2
        self.app_control_pub.publish(self.app_control_msg)


    # global_path2가 들어왔을때 실행
    def path_callback(self, msg):
        self.is_path=True
        self.path_msg=msg

    
    # odom정보가 들어왔을때 실행
    def odom_callback(self, msg):
        self.is_odom=True
        self.odom_msg=msg
        q=Quaternion(msg.pose.pose.orientation.w,msg.pose.pose.orientation.x,msg.pose.pose.orientation.y,msg.pose.pose.orientation.z)
        _,_,self.robot_yaw=q.to_euler()
    

    # 카메라 정보가 들어왔을때 실행
    def img_callback(self, msg):
    
        self.byte_data = msg.data

        np_arr = np.frombuffer(msg.data, np.uint8)

        self.img_bgr = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

    # 터틀봇 앞
    def turtlebot_go(self) :
        self.cmd_msg.linear.x=0.3
        self.cmd_msg.angular.z=0.0

    # 뒤
    def turtlebot_back(self) :
        self.cmd_msg.linear.x=-0.3
        self.cmd_msg.angular.z=0.0

    # 멈춤
    def turtlebot_stop(self) :
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=0.0

    # 오른쪽
    def turtlebot_cw_rot(self) :
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=0.3

    # 왼쪽
    def turtlebot_cww_rot(self) :
        self.cmd_msg.linear.x=0.0
        self.cmd_msg.angular.z=-0.3

    # 방범모드 실행시 시작점 찾기
    def search_start_point(self) :

        self.len_wp = len(self.path_msg.poses)

        print("searching the start points ... ")

        dis_list = []

        for wp in self.path_msg.poses:

            robot_pose_x=self.odom_msg.pose.pose.position.x
            robot_pose_y=self.odom_msg.pose.pose.position.y

            self.current_point=wp.pose.position

            dx= self.current_point.x - robot_pose_x
            dy= self.current_point.y - robot_pose_y

            dis=sqrt(pow(dx,2)+pow(dy,2))

            dis_list.append(dis)

        self.idx_wp = np.argmin(dis_list)

        self.check_1_wp = True


    # 방범모드 때 사람 찾기 위한 함수
    def detect_human(self):
    
        img_pre = cv2.cvtColor(self.img_bgr, cv2.COLOR_BGR2GRAY)

        (rects_temp, _) = self.pedes_detector.detectMultiScale(img_pre, winStride=(2, 2), padding=(8, 8), scale=2)

        if len(rects_temp) != 0:

            rects = non_maximum_supression(rects_temp)

            self.human_detected = True

            for (x,y,w,h) in rects:

                cv2.rectangle(self.img_bgr, (x,y),(x+w,y+h),(0,255,255), 2)
        
        else:
            self.human_detected = False
    

    # 물건찾기 함수
    def find_bbox(self):
        global flag

        # print(flag)
        """
        # 로직 3. bgr 이미지의 binarization
        # 지갑, 키 등의 물체에 대한 bgr 값을 알고, 이 값 범위에 해당되는
        # cv2.inRange 함수를 써서 각 물체에 대해 binarization 하십시오.
        """
        
        lower_wal = (100, 245, 255)
        upper_wal = (110, 255, 255)
        lower_bp = (100, 210, 235)
        upper_bp = (110, 220, 255)
        lower_rc = (100, 210, 200)
        upper_rc = (110, 220, 220)
        lower_key = (100, 240, 200)
        upper_key = (110, 250, 220)

        self.img_wal = cv2.inRange(self.img_bgr, lower_wal, upper_wal)

        self.img_bp = cv2.inRange(self.img_bgr, lower_bp, upper_bp)

        self.img_rc = cv2.inRange(self.img_bgr, lower_rc, upper_rc)

        self.img_key = cv2.inRange(self.img_bgr, lower_key, upper_key)


        """
        # 로직 4. 물체의 contour 찾기
        # 지갑, 키 등의 물체들이 차지한 픽셀만 흰색으로 이진화되어 있는 이미지에 대해서,
        # 흰색 영역을 감싸는 contour들을 구하십시오.
        # cv2.findContours를 가지고 
        """
        # cv2.findContours()를 이용하여 이진화 이미지에서 윤곽선(컨투어)를 검색합니다.
        # cv2.findContours(이진화 이미지, 검색 방법, 근사화 방법)을 의미합니다.
        # 반환값으로 윤곽선, 계층 구조를 반환합니다.
        contours_wal, _ = cv2.findContours(self.img_wal, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        contours_bp, _ = cv2.findContours(self.img_bp, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        contours_rc, _ = cv2.findContours(self.img_rc, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        contours_key, _ = cv2.findContours(self.img_key, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

        if contours_wal :
            self.wal_detected = True
        else :
            self.wal_detected = False

        if contours_bp :
            self.bp_detected = True
        else :
            self.bp_detected = False

        if contours_rc :
            self.rc_detected = True
        else : 
            self.rc_detected = False
        
        if contours_key :
            self.key_detected = True
        else : 
            self.key_detected = False

        
        # 물건 찾기 실행했을 때
        if search_switch:
            # 지갑 찾기
            if search_switch == 1 and self.wal_detected and flag:
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('walletStreaming', b64data.decode( 'utf-8' ) )
            # 가방 찾기
            elif search_switch == 2 and self.bp_detected and flag:
                # cv2.imwrite('../web/client/backpack.jpg', self.img_bgr)
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('backpackStreaming', b64data.decode( 'utf-8' ) )
            # 키 찾기
            elif search_switch == 3 and self.key_detected and flag:
                # cv2.imwrite('../web/client/key.jpg', self.img_bgr)
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('keyStreaming', b64data.decode( 'utf-8' ) )
            # 리모콘 찾기
            elif search_switch == 4 and self.rc_detected and flag:
                # cv2.imwrite('../web/client/wallet.jpg', self.img_bgr)
                self.byte_data = cv2.imencode('.jpg', self.img_bgr*255)[1].tobytes()
                b64data = base64.b64encode(self.byte_data)
                sio.emit('remoteStreaming', b64data.decode( 'utf-8' ) )



        # print(contours_bp)
        # if contours_bp:
        #     cv2.imwrite()

        """
        # 로직 5. 물체의 bounding box 좌표 찾기
        """
        
        self.find_cnt(contours_wal)
        
        self.find_cnt(contours_bp)
        
        self.find_cnt(contours_rc)
        
        self.find_cnt(contours_key)

    
    # 찾는 물품이 카메라의 특정범위(중앙)에 들어오면 캡처
    def find_cnt(self, contours):
        global flag
        # print("좌표가 있나?")

        """
        # 로직 5. 물체의 bounding box 좌표 찾기
        # 지갑, 키 등의 물체들의 흰색 영역을 감싸는 contour 결과를 가지고
        # bbox를 원본 이미지에 draw 하십시오.
        """ 
        
        for cnt in contours:
            cnt = cnt.reshape( len(cnt) ,2).T
            x, y, w, h = min(cnt[0]), min(cnt[1]), max(cnt[0]), max(cnt[1])
            # print(x)
            if x >=100 and x <= 500:
                flag = True
            else:
                flag = False
            # print(x)

            cv2.rectangle(self.img_bgr, (x, y), (w, h), (0,0,255), 1 )

        # countours들로 바로 그려줌
        # cv2.drawContours(self.img_bgr, contours, -1, (0,0,255), 3)
    def timer_callback(self):
        global auto_switch
        ## 맵 만들기 관련
        global map_save
        msg = Int8MultiArray()
        map_create_state, map_save_state = get_map_create()
        msg.data = [map_create_state, map_save_state]
        map_save = False
        self.map_publisher.publish(msg)

        ## 맵 만들기 autoMode 관련
        global map_Auto
        msg_auto = Int8MultiArray()
        map_auto_state = get_map_auto()
        msg_auto.data = [map_auto_state]
        self.automap_publisher.publish(msg_auto)

        ## 터틀봇 조작관련

        

            
        robot_cmd = get_robot_var()
        _,auto_switch = get_global_var()

        # patrol_msg = Int8MultiArray()
        # patrol_msg.data = [auto_switch]
        # self.patrol_status.publish(patrol_msg)

        if auto_switch == 0:

            sio.emit('PatrolStatus', 'Off')

            if robot_cmd == 1:

                # turn left

                self.turtlebot_cww_rot()

            elif robot_cmd == 2:
                
                # go straight

                self.turtlebot_go()
            
            elif robot_cmd == 3:

                # back

                self.turtlebot_back()

            elif robot_cmd == 4:
                
                # turn right

                self.turtlebot_cw_rot()
            
            else:

                self.turtlebot_stop()
            
            # if not (self.check_cmd == 0 and robot_cmd == 0):
            self.cmd_publisher.publish(self.cmd_msg)
            
            # self.check_cmd = robot_cmd
            
            if robot_cmd!=0: 

                self.m_control_iter += 1

            if self.m_control_iter % self.m_control_interval == 0:

                self.m_control_iter = 0

                reset_robot_var()
            
            self.check_1_wp = False

        else:

            # auto patrol mode on

            sio.emit('PatrolStatus', 'On')

            # if self.is_odom == True and self.is_path==True:

            #     if not self.check_1_wp: 
                    
            #         self.search_start_point()

            #     rotated_point=Point()
            #     robot_pose_x=self.odom_msg.pose.pose.position.x
            #     robot_pose_y=self.odom_msg.pose.pose.position.y

            #     waypoint = self.path_msg.poses[self.idx_wp]

            #     self.current_point=waypoint.pose.position

            #     dx= self.current_point.x - robot_pose_x
            #     dy= self.current_point.y - robot_pose_y
            #     rotated_point.x=cos(self.robot_yaw)*dx + sin(self.robot_yaw)*dy
            #     rotated_point.y= -sin(self.robot_yaw)*dx + cos(self.robot_yaw)*dy

            #     theta=atan2(rotated_point.y,rotated_point.x)

            #     dis=sqrt(pow(rotated_point.x,2)+pow(rotated_point.y,2))

            #     print(dis)

            #     if abs(theta) < pi/10:
                    
            #         self.cmd_msg.linear.x=dis*2
            #         self.cmd_msg.angular.z=-theta*0.3
                
            #     else:
                    
            #         self.cmd_msg.linear.x=0.0
            #         self.cmd_msg.angular.z=-theta*0.3

            #     if dis <= self.lfd and  self.idx_wp < self.len_wp-1:

            #         self.idx_wp += 1

            #     elif dis <= self.lfd and  self.idx_wp == self.len_wp-1:

            #         self.idx_wp = 0
            # else:

            #     self.turtlebot_stop()

            # self.cmd_publisher.publish(self.cmd_msg)
        
        ## iot 조작 관련
        # m_control_cmd 리턴 (켜는지 끄는지 : 1 또는 2)
        ctrl_cmd,_ = get_global_var()
        # m_control_num 리턴 (가전제품 번호 : 0 ~ 16)
        ctrl_num = get_global_num()

        # 킬때
        if ctrl_cmd == 1:       
            self.ctr_status = 1
            self.ctr_num = ctrl_num
            self.app_on_select(self.ctr_num)
            self.pose_msg.angular.x= self.iot_data_x[self.ctr_num]
            self.pose_msg.angular.y= self.iot_data_y[self.ctr_num]
            if self.cmd_msg.linear.x == 0:
                self.pose_pub.publish(self.pose_msg)
            
        # 끌때
        elif ctrl_cmd == 2:
            self.ctr_status = 2
            self.ctr_num = ctrl_num
            self.app_off_select(self.ctr_num)
            self.pose_msg.angular.x= self.iot_data_x[self.ctr_num]
            self.pose_msg.angular.y= self.iot_data_y[self.ctr_num]
            if self.cmd_msg.linear.x == 0:
                self.pose_pub.publish(self.pose_msg)
        

        # 현재 상태 보내고 리셋
        if ctrl_cmd != 0: 
            print(self.app_status_msg.data)
            if self.app_status_msg.data[self.ctr_num] == self.ctr_status:
                print(self.app_status_msg.data)
                if self.app_status_msg.data[self.ctr_num] == 1:
                    print('On')
                    sio.emit(self.iot_status[self.ctr_num], 'On')
                elif self.app_status_msg.data[self.ctr_num] == 2:
                    print('Off')
                    sio.emit(self.iot_status[self.ctr_num], 'Off')

                self.ctr_num = 0
                self.ctr_status = 0
                # m_control_cmd을 0으로 리셋 
                reset_global_var()
                # m_control_num을 0으로 리셋 
                reset_global_num()

        if self.img_bgr is not None:

            print("subscribed")

            self.detect_human()

            self.find_bbox()

            b64data = base64.b64encode(self.byte_data)

            if self.human_detected:

                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()

                sio.emit('humanDetectToServer', b64data.decode( 'utf-8' ) )
            
            if self.wal_detected:
                auto_switch = 0
                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()
            
            if self.key_detected:
                auto_switch = 0
                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()
            
            if self.bp_detected:
                auto_switch = 0
                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()
            
            if self.rc_detected:
                auto_switch = 0
                self.byte_data = cv2.imencode('.jpg', self.img_bgr)[1].tobytes()

            sio.emit('streaming', b64data.decode( 'utf-8' ) )


def main(args=None):
    
    rclpy.init(args=args)

    client = clientFromServer()

    rclpy.spin(client)
    
    rclpy.shutdown()

    sio.disconnect()


if __name__ == '__main__':
    main()

